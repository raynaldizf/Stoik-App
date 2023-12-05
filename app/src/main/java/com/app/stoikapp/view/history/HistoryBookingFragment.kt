package com.app.stoikapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.Booking
import com.app.stoikapp.databinding.FragmentHistoryBookingBinding
import com.app.stoikapp.view.history.adapter.HistoryBookingAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class HistoryBookingFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBookingBinding
    private lateinit var adapter: HistoryBookingAdapter
    private lateinit var database: DatabaseReference
    private var userId = ""
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryBookingAdapter(requireContext(), mutableListOf())
        binding.rvHistoryBooking.adapter = adapter
        binding.rvHistoryBooking.layoutManager = LinearLayoutManager(requireContext())

        database = FirebaseDatabase.getInstance().getReference("history/booking")

        sharedPref = SharedPref(requireContext())

        lifecycleScope.launch {
            sharedPref.getUserId.collect { id ->
                userId = id

                database.child(userId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val bookingList = mutableListOf<Booking>()

                        for (bookingSnapshot in snapshot.children) {
                            val booking = bookingSnapshot.getValue(Booking::class.java)
                            if (booking != null) {
                                bookingList.add(booking)
                            }
                        }

                        adapter.updateData(bookingList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
}
