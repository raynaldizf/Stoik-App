package com.app.stoikapp.view.edukasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentEdukasiQuestAnswerBinding
import com.bumptech.glide.Glide

class EdukasiQuestAnswerFragment : Fragment() {
    lateinit var binding : FragmentEdukasiQuestAnswerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEdukasiQuestAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val judul = arguments?.getString("judul")
        val penjelasan = arguments?.getString("penjelasan")
        val gambar = arguments?.getString("gambar")

        binding.textViewTitle.text = judul
        binding.textViewDesc.text = penjelasan
        Glide.with(requireContext()).load(gambar).into(binding.imageView)
    }

}