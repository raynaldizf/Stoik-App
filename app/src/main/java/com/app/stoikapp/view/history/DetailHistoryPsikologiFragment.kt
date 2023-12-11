package com.app.stoikapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentDetailHistoryPsikologiBinding

class DetailHistoryPsikologiFragment : Fragment() {
    lateinit var binding : FragmentDetailHistoryPsikologiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailHistoryPsikologiBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nama.text = arguments?.getString("nama")
        binding.psikolog.text = arguments?.getString("namaPsikolog")
        binding.kodeBook.text = arguments?.getString("kodeBook")
        binding.hari.text = arguments?.getString("hari")
        binding.waktu.text = arguments?.getString("waktu")
        binding.biaya.text = arguments?.getString("biaya")
        binding.catatan .text = arguments?.getString("catatan")

    }
}