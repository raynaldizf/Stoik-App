package com.app.stoikapp.view.home.psikolog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.data.model.Psikolog
import com.app.stoikapp.databinding.FragmentPsikologBinding
import com.app.stoikapp.view.home.psikolog.adapter.PsikologAdapter
import com.google.firebase.database.*

class PsikologFragment : Fragment() {
    private lateinit var binding: FragmentPsikologBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var psikologAdapter: PsikologAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPsikologBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("psikolog")

        psikologAdapter = PsikologAdapter(requireContext(), mutableListOf())

        binding.rvPsikolog.layoutManager = LinearLayoutManager(requireContext())

        binding.rvPsikolog.adapter = psikologAdapter

        readDataFromFirebase()

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun readDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val psikologList = mutableListOf<Psikolog>()

                for (snapshot in dataSnapshot.children) {
                    val psikolog = snapshot.getValue(Psikolog::class.java)
                    psikolog?.let { psikologList.add(it) }
                }

                psikologAdapter.updateData(psikologList)
                Log.d("Datas : ", "Number of psychologists: ${psikologList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Datas : ", "Failed to read value.", databaseError.toException())
            }
        })
    }
}
