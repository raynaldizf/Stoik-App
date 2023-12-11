package com.app.stoikapp.view.edukasi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        // Inisialisasi database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("edukasi/${path}/materi")

        // Inisialisasi RecyclerView dan adapter
        val materiAdapter = MateriAdapter(requireContext(), mutableListOf()) // Sesuaikan dengan struktur adapter Anda
        binding.recyclerViewQuest.adapter = materiAdapter
        binding.recyclerViewQuest.layoutManager = LinearLayoutManager(requireContext())

        // Mendapatkan data dari Firebase dan memasukkannya ke dalam adapter
        readDataFromFirebase()
    }

    private fun readDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val subMateriList = mutableListOf<Materi>()

                for (materiSnapshot in dataSnapshot.children) {
                    val judul = materiSnapshot.child("judul").getValue(String::class.java)
                    val penjelasan = materiSnapshot.child("penjelasan").getValue(String::class.java)

                    // Tambahkan data ke dalam list subMateri
                    subMateriList.add(Materi("https://firebasestorage.googleapis.com/v0/b/stoik-app.appspot.com/o/edukasi%2Focd%2Fimgocd5.png?alt=media&token=c7a49418-14d8-403f-ab60-3e4c5408a641",judul, penjelasan))  // Gantilah "" dengan URL gambar jika diperlukan
                }

                // Set adapter ke RecyclerView
                val materiAdapter = MateriAdapter(requireContext(), subMateriList)
                binding.recyclerViewQuest.adapter = materiAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        })
    }

}
