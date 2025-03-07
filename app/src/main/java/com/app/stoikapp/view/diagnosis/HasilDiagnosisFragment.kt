package com.app.stoikapp.view.diagnosis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentHasilDiagnosisBinding

class HasilDiagnosisFragment : Fragment() {
    lateinit var binding : FragmentHasilDiagnosisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHasilDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hasil = arguments?.getString("hasilDiagnosa")
        binding.hasilDiagnosis.text = hasil
        val solusi = arguments?.getString("solusi")
        binding.solusi.text = solusi

        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_hasilDiagnosisFragment_to_diagnosisOnBoardingFragment)
        }
    }
}