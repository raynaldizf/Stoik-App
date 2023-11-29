package com.app.stoikapp.view.diagnosis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentDiagnosisBinding

class DiagnosisFragment : Fragment() {
    lateinit var binding : FragmentDiagnosisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }
}