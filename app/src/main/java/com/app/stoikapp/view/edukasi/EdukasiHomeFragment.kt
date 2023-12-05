package com.app.stoikapp.view.edukasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentEdukasiHomeBinding

class EdukasiHomeFragment : Fragment() {
    lateinit var binding : FragmentEdukasiHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEdukasiHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetailPsikolog.setOnClickListener{
            findNavController().navigate(R.id.action_edukasiHomeFragment_to_psikologFragment)
        }

        binding.btnDetailSound.setOnClickListener{
         findNavController().navigate(R.id.action_edukasiHomeFragment_to_soundMeditasiFragment)
        }



    }

}