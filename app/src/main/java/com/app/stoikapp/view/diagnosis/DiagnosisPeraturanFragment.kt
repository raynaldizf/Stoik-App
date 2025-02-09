package com.app.stoikapp.view.diagnosis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentDiagnosisPeraturanBinding

class DiagnosisPeraturanFragment : Fragment() {
    lateinit var binding : FragmentDiagnosisPeraturanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiagnosisPeraturanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMulai.setOnClickListener{
            findNavController().navigate(R.id.action_diagnosisPeraturanFragment_to_diagnosisFragment)

        }

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

}