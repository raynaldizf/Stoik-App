package com.app.stoikapp.view.authentication.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.User
import com.app.stoikapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        databaseRef = FirebaseDatabase.getInstance().getReference("users")

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email dan Password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun saveSession(
        userId: String,
        fullName: String,
        email: String,
        birthDate: String,
        gender: String,
        phoneNumber: String,
        password: String,
        profilePicture: String
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                sharedPref.saveUserData(
                    userId,
                    fullName,
                    email,
                    birthDate,
                    gender,
                    phoneNumber,
                    password,
                    profilePicture
                )
            } catch (e: Exception) {
                Log.e("LoginFragment", "Error saving user data: ${e.message}")
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        databaseRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            if (user?.password == password) {
                                saveSession(
                                    user!!.userId.toString(),
                                    user!!.fullName.toString(),
                                    user!!.email.toString(),
                                    user!!.birthDate.toString(),
                                    user!!.gender.toString(),
                                    user!!.phoneNumber.toString(),
                                    user!!.password.toString(),
                                    user!!.profilePicture.toString()
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Login berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()

                                findNavController().apply {
                                    popBackStack(R.id.loginFragment, false)
                                    navigate(R.id.action_loginFragment_to_homeFragment)
                                }
                            } else {
                                showLoginError()
                            }
                            return
                        }
                    } else {
                        showLoginError()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoginError()
                }
            })
    }

    private fun showLoginError() {
        Toast.makeText(
            requireContext(),
            "Email atau Password salah",
            Toast.LENGTH_SHORT
        ).show()
    }
}
