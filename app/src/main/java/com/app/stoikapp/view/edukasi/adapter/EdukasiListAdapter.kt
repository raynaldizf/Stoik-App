package com.app.stoikapp.view.edukasi.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.data.model.Edukasi
import com.app.stoikapp.databinding.CustomEdukasiHomeBinding
import com.app.stoikapp.databinding.CustomLayoutEdukasiBinding
import com.bumptech.glide.Glide

class EdukasiListAdapter(private val context: Context, private val materiList: MutableList<Edukasi>) :
    RecyclerView.Adapter<EdukasiListAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomLayoutEdukasiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomLayoutEdukasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materi = materiList[position]
        holder.binding.deskripsi.text = truncateDescription(materi.deskripsi!!)
        holder.binding.judul.text = materi.judul
        Glide.with(context).load(materi.gambar).into(holder.binding.imageView)
        holder.binding.cardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("materi", materi.path)
            bundle.putString("judul", materi.judul)
            Navigation.findNavController(it).navigate(com.app.stoikapp.R.id.action_edukasiMentalFragment_to_edukasiQuestFragment, bundle)
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
