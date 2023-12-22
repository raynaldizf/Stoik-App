package com.app.stoikapp.view.edukasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.R
import com.app.stoikapp.data.model.Edukasi
import com.app.stoikapp.databinding.FragmentEdukasiMentalBinding
import com.app.stoikapp.view.edukasi.adapter.EdukasiAdapter
import com.app.stoikapp.view.edukasi.adapter.EdukasiListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EdukasiMentalFragment : Fragment() {
    lateinit var binding: FragmentEdukasiMentalBinding
    lateinit var edukasiReference: DatabaseReference
    private lateinit var edukasiAdapter: EdukasiListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEdukasiMentalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edukasiReference = FirebaseDatabase.getInstance().getReference("edukasi")

        edukasiAdapter = EdukasiListAdapter(requireContext(), mutableListOf())

        binding.rvEdukasi.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvEdukasi.adapter = edukasiAdapter

        readEdukasiDataFromFirebase()

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }


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