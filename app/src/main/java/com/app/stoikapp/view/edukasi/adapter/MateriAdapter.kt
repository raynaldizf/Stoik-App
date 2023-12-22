package com.app.stoikapp.view.edukasi.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.data.model.Materi
import com.app.stoikapp.data.model.SubMateri
import com.app.stoikapp.databinding.CustomListQuesttionBinding
import com.bumptech.glide.Glide

class MateriAdapter(private val context: Context, private val subMateriList: MutableList<Materi>) :
    RecyclerView.Adapter<MateriAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomListQuesttionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomListQuesttionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subMateri = subMateriList[position]
        holder.binding.cardView.setOnClickListener{
          // Test Log
            Log.d("MateriAdapter", "onBindViewHolder: ${subMateri.judul}")
            Log.d("MateriAdapter", "onBindViewHolder: ${subMateri.penjelasan}")
            Log.d("MateriAdapter", "onBindViewHolder: ${subMateri.gambar}")
            val bundle = Bundle()
            bundle.putString("judul", subMateri.judul)
            bundle.putString("penjelasan", subMateri.penjelasan)
            bundle.putString("gambar", subMateri.gambar)
            Navigation.findNavController(it).navigate(com.app.stoikapp.R.id.action_edukasiQuestFragment_to_edukasiQuestAnswerFragment, bundle)
        }

        holder.binding.materiJudul.text = subMateri.judul
        Glide.with(context).load(subMateri.gambar).into(holder.binding.imageView)
    }

    override fun getItemCount(): Int {
        return subMateriList.size
    }

    fun updateData(newSubMateriList: List<Materi>) {
        subMateriList.clear()
        subMateriList.addAll(newSubMateriList)
        notifyDataSetChanged()
    }
}
