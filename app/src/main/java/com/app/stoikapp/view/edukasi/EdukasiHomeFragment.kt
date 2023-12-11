package com.app.stoikapp.view.edukasi

import MeditasiAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.R
import com.app.stoikapp.data.model.Edukasi
import com.app.stoikapp.data.model.MeditasiSong
import com.app.stoikapp.databinding.FragmentEdukasiHomeBinding
import com.app.stoikapp.view.edukasi.adapter.EdukasiAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EdukasiHomeFragment : Fragment() {
    lateinit var binding : FragmentEdukasiHomeBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var meditasiAdapter: MeditasiAdapter
    private lateinit var edukasiAdapter: EdukasiAdapter
    lateinit var edukasiReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEdukasiHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("meditasi")
        edukasiReference = FirebaseDatabase.getInstance().getReference("edukasi")

        meditasiAdapter = MeditasiAdapter(requireContext(), mutableListOf())
        edukasiAdapter = EdukasiAdapter(requireContext(), mutableListOf())

        binding.soudMeditasi.adapter = meditasiAdapter

        binding.rvEdukasi.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEdukasi.adapter = edukasiAdapter

        readDataFromFirebase()
        readEdukasiDataFromFirebase()

        binding.btnEdukasi.setOnClickListener{
            findNavController().navigate(R.id.action_edukasiHomeFragment_to_edukasiMentalFragment)
        }

        binding.btnDetailSound.setOnClickListener{
         findNavController().navigate(R.id.action_edukasiHomeFragment_to_soundMeditasiFragment)
        }



    }

    private fun readDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val meditasiList = mutableListOf<MeditasiSong>()

                for (snapshot in dataSnapshot.children) {
                    val meditasiSong = snapshot.getValue(MeditasiSong::class.java)
                    meditasiSong?.let { meditasiList.add(it) }
                }

                meditasiAdapter.updateData(meditasiList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun readEdukasiDataFromFirebase() {
        edukasiReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val edukasiList = mutableListOf<Edukasi>()

                for (snapshot in dataSnapshot.children) {
                    val edukasiData = snapshot.getValue(Edukasi::class.java)
                    edukasiData?.let { edukasiList.add(it) }
                }

                edukasiAdapter.updateData(edukasiList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        })
    }

}