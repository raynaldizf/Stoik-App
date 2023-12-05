package com.app.stoikapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentHistoryBinding
import com.app.stoikapp.databinding.FragmentHistoryDiagnosisBinding

class HistoryDiagnosisFragment : Fragment() {
    lateinit var binding : FragmentHistoryDiagnosisBinding
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
    }

}