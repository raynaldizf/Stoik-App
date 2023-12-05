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
import com.app.stoikapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth  // Import FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var sharedPref: SharedPref
    lateinit var firebaseAuth: FirebaseAuth  // Add this line

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            binding.editProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }

            binding.logout.setOnClickListener {
                clearData()
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

}
