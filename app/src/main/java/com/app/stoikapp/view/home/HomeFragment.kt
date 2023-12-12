package com.app.stoikapp.view.home

import SoundAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.R
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.MeditasiSong
import com.app.stoikapp.data.model.Psikolog
import com.app.stoikapp.data.model.User
import com.app.stoikapp.databinding.FragmentHomeBinding
import com.app.stoikapp.view.home.adapter.HomePsikologAdapter
import com.app.stoikapp.view.home.psikolog.adapter.PsikologAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var sharedPref: SharedPref
    private lateinit var databaseReferenceMeditasi: DatabaseReference
    private lateinit var databaseReferencePsikolog: DatabaseReference
    private lateinit var soundAdapter: SoundAdapter
    private lateinit var psikologAdapter: HomePsikologAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())

        lifecycleScope.launch {
            // Collect the userId value
            sharedPref.getUserId.collect { id ->
                fetchUserDetails(id)
            }
        }
        databaseReferenceMeditasi = FirebaseDatabase.getInstance().getReference("meditasi")
        databaseReferencePsikolog = FirebaseDatabase.getInstance().getReference("psikolog")

        binding.soundRelax.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.psikologTerdekat.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        soundAdapter = SoundAdapter(requireContext(), mutableListOf())
        psikologAdapter = HomePsikologAdapter(requireContext(), mutableListOf())

        binding.soundRelax.adapter = soundAdapter
        binding.psikologTerdekat.adapter = psikologAdapter

        fetchDataFromFirebaseMeditasi()
        fetchDataFromFirebasePsikolog()

        binding.btnDetailSound.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_soundMeditasiFragment)
        }

        binding.btnDetailPsikolog.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_psikologFragment)
        }
    }

    private fun fetchDataFromFirebaseMeditasi() {
        databaseReferenceMeditasi.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meditasiList = mutableListOf<MeditasiSong>()

                for (childSnapshot in snapshot.children) {
                    val meditasiData = childSnapshot.getValue(MeditasiSong::class.java)
                    meditasiData?.let {
                        meditasiList.add(it)
                    }
                }

                soundAdapter.updateData(meditasiList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fetchDataFromFirebasePsikolog() {
        databaseReferencePsikolog.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val psikologList = mutableListOf<Psikolog>()

                for (childSnapshot in snapshot.children) {
                    val psikologData = childSnapshot.getValue(Psikolog::class.java)
                    psikologData?.let {
                        psikologList.add(it)
                    }
                }

                psikologAdapter.updateData(psikologList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fetchUserDetails(userId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.txtUser.text = user.fullName
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
