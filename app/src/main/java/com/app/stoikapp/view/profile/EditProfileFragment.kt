package com.app.stoikapp.view.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.databinding.FragmentEditProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.collect
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    lateinit var binding: FragmentEditProfileBinding
    private lateinit var databaseRef: DatabaseReference
    private val STORAGE_PERMISSION_CODE = 100
    private lateinit var selectedImageUri: String
    private lateinit var currentPhotoPath: String
    private var selectedImg: Uri? = null
    private var getFile: File? = null
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = storage.reference
    lateinit var sharedPref: SharedPref

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseRef = FirebaseDatabase.getInstance().getReference("users")
        sharedPref = SharedPref(requireContext())

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.editTextTanggalLahir.setOnClickListener {
            dateDialog()
        }

        binding.btnEditProfile.setOnClickListener {
            startGallery()
        }

        binding.btnSimpan.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                sharedPref.getUserId.collect{ userId ->

                    val userId = userId
                    val fullName = binding.editTextNamaLengkap.text.toString()
                    val email = binding.editTextEmail.text.toString()
                    val birthDate = binding.editTextTanggalLahir.text.toString()
                    val gender = if (binding.radioButtonLakiLaki.isChecked) "Laki-Laki" else "Perempuan"
                    val phoneNumber = binding.editTextNomorTelepon.text.toString()
                    val password = binding.editTextPassword.text.toString()
                    val confirmPassword = binding.editTextKonfirmasiPassword.text.toString()

                    if (selectedImg != null && validateInput(
                            fullName,
                            email,
                            birthDate,
                            gender,
                            phoneNumber,
                            password,
                            confirmPassword,
                            selectedImg!!
                        )
                    ) {
                        updateProfileImageInFirebase(
                            userId,
                            fullName,
                            email,
                            birthDate,
                            phoneNumber,
                            gender,
                            password,
                            selectedImg
                        )
                    } else {
                        Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    }

    private fun startGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openImagePicker()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            launcherIntentGallery.launch(intent)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcherIntentGallery.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImg = result.data?.data as Uri
            binding.btnEditProfile.setImageURI(selectedImg)
        }
    }

    private fun validateInput(
        fullName: String,
        email: String,
        birthDate: String,
        gender: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        imageUri: Uri
    ): Boolean {
        if (fullName.isEmpty() || email.isEmpty() || birthDate.isEmpty() || gender.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Password tidak sama", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun dateDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)

        binding.editTextTanggalLahir.setText(formattedDate)
    }

    private fun updateProfileImageInFirebase(
        userId: String,
        name: String,
        email: String,
        dateOfBirth: String,
        phoneNumber: String,
        gender: String,
        password: String,
        imageUri: Uri?
    ) {
        val storageRef = FirebaseStorage.getInstance().getReference("profile_images").child(userId)
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl
                    .addOnSuccessListener { downloadUrl ->

                        println("File uploaded successfully. Download URL: $downloadUrl")
                        val userDatabaseRef = FirebaseDatabase.getInstance().getReference("users")
                            .child(userId)

                        val updatedUserData = HashMap<String, Any>()
                        updatedUserData["fullName"] = name
                        updatedUserData["email"] = email
                        updatedUserData["birthDate"] = dateOfBirth
                        updatedUserData["phoneNumber"] = phoneNumber
                        updatedUserData["gender"] = gender
                        updatedUserData["password"] = password
                        updatedUserData["profilePicture"] = downloadUrl.toString()

                        userDatabaseRef.updateChildren(updatedUserData)
                            .addOnSuccessListener {
                                Log.d("Firebase Database", "User data updated successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firebase Database", "Error updating user data", e)
                            }
                    }
                    .addOnFailureListener { exception ->
                        println("Failed to get download URL. Error: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                println("File upload failed. Error: ${exception.message}")
            }
    }
}