package com.app.stoikapp.view.edukasi.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.data.model.Edukasi
import com.app.stoikapp.databinding.CustomEdukasiHomeBinding
import com.bumptech.glide.Glide

class EdukasiAdapter(private val context: Context, private val materiList: MutableList<Edukasi>) :
    RecyclerView.Adapter<EdukasiAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomEdukasiHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomEdukasiHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materi = materiList[position]
        holder.binding.tvEdukasiDesc.text = truncateDescription(materi.deskripsi!!)
        holder.binding.tvEdukasi.text = materi.judul
        Glide.with(context).load(materi.gambar).into(holder.binding.ivEdukasi)
        holder.binding.cvEdukasi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("materi", materi.path)
            bundle.putString("judul", materi.judul)
            Navigation.findNavController(it).navigate(com.app.stoikapp.R.id.action_edukasiHomeFragment_to_edukasiQuestFragment, bundle)

        }
    }

    private fun truncateDescription(description: String): String {
        val words = description.split(" ")
        val truncatedWords = if (words.size > 4) words.subList(0, 4) else words
        val truncatedText = truncatedWords.joinToString(" ") { it }
        return if (words.size > 4) "$truncatedText ..." else truncatedText
    }

    override fun getItemCount(): Int {
        return materiList.size
    }

    fun updateData(newData: MutableList<Edukasi>) {
        materiList.clear()
        materiList.addAll(newData)
        notifyDataSetChanged()
    }
}
