package com.app.stoikapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentDetailHistoryDiagnosisBinding

class DetailHistoryDiagnosisFragment : Fragment() {
    lateinit var binding : FragmentDetailHistoryDiagnosisBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailHistoryDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//                  bundle.putString("diagnosa", diagnosis.diagnosa)
//            bundle.putString("solusi", diagnosis.solusi)

        binding.txtHasilDiagnosa.text = arguments?.getString("diagnosa")
        binding.txtSolusi.text = arguments?.getString("solusi")
        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }
}