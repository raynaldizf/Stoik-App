package com.app.stoikapp.view.history.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.R
import com.app.stoikapp.data.model.Diagnosis
import com.app.stoikapp.databinding.CustomLayoutHistoryBinding

class HistoryDiagnosisAdapter(private val context: Context, private val diagnosaList: MutableList<Diagnosis>) :
    RecyclerView.Adapter<HistoryDiagnosisAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomLayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomLayoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diagnosaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diagnosis = diagnosaList[position]
        holder.binding.header.text = diagnosis.diagnosa
        holder.binding.deskrpsi.text = diagnosis.solusi
        holder.binding.cardView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("diagnosa", diagnosis.diagnosa)
            bundle.putString("solusi", diagnosis.solusi)
            Navigation.findNavController(it).navigate(R.id.action_historyFragment_to_detailHistoryDiagnosisFragment, bundle)
        }
    }
    fun updateData(newData: List<Diagnosis>) {
        diagnosaList.clear()
        diagnosaList.addAll(newData)
        notifyDataSetChanged()
    }

}
