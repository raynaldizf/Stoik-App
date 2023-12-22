package com.app.stoikapp.view.edukasi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.data.model.Materi
import com.app.stoikapp.data.model.SubMateri
import com.app.stoikapp.databinding.FragmentEdukasiQuestBinding
import com.app.stoikapp.view.edukasi.adapter.MateriAdapter

import com.google.firebase.database.*

class EdukasiQuestFragment : Fragment() {
    lateinit var binding: FragmentEdukasiQuestBinding
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEdukasiQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val judul = arguments?.getString("judul")
        val path = arguments?.getString("materi")
        binding.textViewTitle.text = judul

        databaseReference = FirebaseDatabase.getInstance().reference.child("edukasi/${path}/materi")

        val materiAdapter = MateriAdapter(requireContext(), mutableListOf())
        // Use GridLayoutManager instead of LinearLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewQuest.adapter = materiAdapter
        binding.recyclerViewQuest.layoutManager = layoutManager

        readDataFromFirebase()

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun readDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val subMateriList = mutableListOf<Materi>()

                for (materiSnapshot in dataSnapshot.children) {
                    val judul = materiSnapshot.child("judul").getValue(String::class.java)
                    val penjelasan = materiSnapshot.child("penjelasan").getValue(String::class.java)
                    val gambar = materiSnapshot.child("gambar").getValue(String::class.java)
                    subMateriList.add(Materi(gambar,judul, penjelasan))
                }

                val materiAdapter = MateriAdapter(requireContext(), subMateriList)
                binding.recyclerViewQuest.adapter = materiAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
