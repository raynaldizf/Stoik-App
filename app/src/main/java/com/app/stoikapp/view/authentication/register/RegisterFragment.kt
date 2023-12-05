package com.app.stoikapp.view.authentication.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.data.model.User
import com.app.stoikapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("users")

        binding.editTextTanggalLahir.setOnClickListener {
            dateDialog()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.editTextNamaLengkap.text.toString()
            val email = binding.editTextEmail.text.toString()
            val birthDate = binding.editTextTanggalLahir.text.toString()
            val gender = if (binding.radioButtonLakiLaki.isChecked) "Laki-Laki" else "Perempuan"
            val phoneNumber = binding.editTextNomorTelepon.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextKonfirmasiPassword.text.toString()

            if (validateInput(
                    fullName,
                    email,
                    birthDate,
                    gender,
                    phoneNumber,
                    password,
                    confirmPassword
                )
            ) {
                signUp(email, password)
            }
        }
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

    private fun validateInput(
        fullName: String,
        email: String,
        birthDate: String,
        gender: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty() || email.isEmpty() || birthDate.isEmpty() || gender.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Password tidak sama", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    saveUserData(user.uid)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Registrasi gagal: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveUserData(userId: String) {
        val fullName = binding.editTextNamaLengkap.text.toString()
        val email = binding.editTextEmail.text.toString()
        val birthDate = binding.editTextTanggalLahir.text.toString()
        val gender = if (binding.radioButtonLakiLaki.isChecked) "Laki-Laki" else "Perempuan"
        val phoneNumber = binding.editTextNomorTelepon.text.toString()
        val profilePicture = "https://firebasestorage.googleapis.com/v0/b/stoik-app.appspot.com/o/profile.png?alt=media&token=9e550398-76a6-4eaa-9859-d5d88c8e4591"
        val password = binding.editTextPassword.text.toString()

        val user = User(
            userId,
            fullName,
            email,
            birthDate,
            gender,
            phoneNumber,
            password,
            profilePicture
        )

        databaseRef.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Berhasil mendaftar", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gagal menyimpan data pengguna: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}