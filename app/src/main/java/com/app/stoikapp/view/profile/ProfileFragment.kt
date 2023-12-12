package com.app.stoikapp.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.User
import com.app.stoikapp.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())

        lifecycleScope.launch {
            // Collect the userId value
            sharedPref.getUserId.collect { id ->
                if (id.isNullOrEmpty()) {
                    // Handle the case where user ID is not available
                    Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Fetch user details from Firebase Realtime Database
                    fetchUserDetails(id)

                    binding.editProfile.setOnClickListener {
                        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
                    }

                    binding.logout.setOnClickListener {
                        clearData()
                    }
                }
            }
        }
    }

    private fun clearData() {
        lifecycleScope.launch(Dispatchers.IO) {
            sharedPref.removeSession()
        }
        findNavController().navigate(
            R.id.action_profileFragment_to_loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build()
        )
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
    }

    private fun fetchUserDetails(userId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.userName.text = user.fullName
                    binding.userEmail.text = user.email
                    Glide.with(requireContext()).load(user.profilePicture).into(binding.images)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
