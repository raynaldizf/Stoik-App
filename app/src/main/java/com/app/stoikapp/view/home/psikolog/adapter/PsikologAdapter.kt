package com.app.stoikapp.view.home.psikolog.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.R
import com.app.stoikapp.data.model.Psikolog
import com.app.stoikapp.databinding.CustomLayoutPsikologBinding
import com.bumptech.glide.Glide

class PsikologAdapter(private val context: Context, private val psikologList: MutableList<Psikolog>) :
    RecyclerView.Adapter<PsikologAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomLayoutPsikologBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomLayoutPsikologBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return psikologList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val psikolog = psikologList[position]
        holder.binding.namaPsikolog.text = psikolog.nama
        holder.binding.hargaPsikolog.text = psikolog.harga.toString()
        Glide.with(holder.itemView.context)
            .load(psikolog.profile)
            .into(holder.binding.imgProfile)
        holder.binding.btnDetail.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", psikolog.id)
            bundle.putString("nama", psikolog.nama)
            bundle.putString("alamat_praktek", psikolog.alamat_praktek)
            bundle.putString("deskripsi", psikolog.deskripsi)
            bundle.putString("nomor_telepon", psikolog.nomor_telepon)
            bundle.putString("lihat_alamat", psikolog.lihat_alamat)
            bundle.putString("profile", psikolog.profile)
            bundle.putString("senin_jumat", psikolog.senin_jumat)
            bundle.putString("sabtu_minggu", psikolog.sabtu_minggu)
            psikolog.harga?.let { it1 -> bundle.putInt("harga", it1) }
            Navigation.findNavController(it).navigate(R.id.action_psikologFragment_to_detailPsikologFragment, bundle)
        }
    }
    fun updateData(newData: List<Psikolog>) {
        psikologList.clear()
        psikologList.addAll(newData)
        notifyDataSetChanged()
    }

}
