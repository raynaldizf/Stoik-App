package com.app.stoikapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.R
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.Booking
import com.app.stoikapp.data.model.Diagnosis
import com.app.stoikapp.databinding.FragmentHistoryBinding
import com.app.stoikapp.databinding.FragmentHistoryDiagnosisBinding
import com.app.stoikapp.view.history.adapter.HistoryBookingAdapter
import com.app.stoikapp.view.history.adapter.HistoryDiagnosisAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HistoryDiagnosisFragment : Fragment() {
    lateinit var binding : FragmentHistoryDiagnosisBinding
    private lateinit var adapter: HistoryDiagnosisAdapter
    private lateinit var database: DatabaseReference
    private var userId = ""
    private lateinit var sharedPref: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryDiagnosisAdapter(requireContext(), mutableListOf())
        binding.diagnosisRv.adapter = adapter
        binding.diagnosisRv.layoutManager = LinearLayoutManager(requireContext())

        database = FirebaseDatabase.getInstance().getReference("history/diagnosis")

        sharedPref = SharedPref(requireContext())

        lifecycleScope.launch {
            sharedPref.getUserId.collect { id ->
                userId = id

                database.child(userId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val diagnosaList = mutableListOf<Diagnosis>()

                        for (diagnosaSnapshot in snapshot.children) {
                            val diagnosa = diagnosaSnapshot.getValue(Diagnosis::class.java)
                            if (diagnosa != null) {
                                diagnosaList.add(diagnosa)
                            }
                        }

                        adapter.updateData(diagnosaList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

}