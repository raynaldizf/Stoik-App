package com.app.stoikapp.view.edukasi

import MeditasiAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.stoikapp.data.model.MeditasiSong
import com.app.stoikapp.databinding.FragmentSoundMeditasiBinding
import com.google.firebase.database.*

class SoundMeditasiFragment : Fragment() {
    lateinit var binding: FragmentSoundMeditasiBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var meditasiAdapter: MeditasiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSoundMeditasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("meditasi")

        meditasiAdapter = MeditasiAdapter(requireContext(), mutableListOf())

        binding.soundMeditasi.adapter = meditasiAdapter

        readDataFromFirebase()
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
}
