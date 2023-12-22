package com.app.stoikapp.view.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    lateinit var binding : FragmentOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing or show a message if needed
        }
        binding.btnMulai.setOnClickListener{
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

}