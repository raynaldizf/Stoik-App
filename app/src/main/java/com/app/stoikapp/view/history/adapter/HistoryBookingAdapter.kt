package com.app.stoikapp.view.history.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.R
import com.app.stoikapp.data.model.Booking
import com.app.stoikapp.data.model.History
import com.app.stoikapp.databinding.CustomLayoutHistoryBinding

class HistoryBookingAdapter(private val context: Context, private val bookingList: MutableList<Booking>) :
    RecyclerView.Adapter<HistoryBookingAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomLayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomLayoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookings = bookingList[position]
        holder.binding.header.text = "Psikolog"
        holder.binding.deskrpsi.text = "Pembayaran kamu sudah kami verifikasi. Cek bukti pemb..."
        holder.binding.cardView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("nama", bookings.nama)
            bundle.putString("namaPsikolog", bookings.namaPsikolog)
            bundle.putString("kodeBook", bookings.kodeBook)
            bundle.putString("hari", bookings.hari)
            bundle.putString("waktu", bookings.waktu)
            bundle.putString("biaya", bookings.biaya)
            bundle.putString("catatan", bookings.catatan)
            Navigation.findNavController(it).navigate(R.id.action_historyFragment_to_detailHistoryPsikologiFragment, bundle)
        }
    }
    fun updateData(newData: List<Booking>) {
        bookingList.clear()
        bookingList.addAll(newData)
        notifyDataSetChanged()
    }

}
