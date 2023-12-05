package com.app.stoikapp.view.edukasi

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.app.stoikapp.databinding.FragmentMusicPlayBinding
import com.bumptech.glide.Glide
import java.io.IOException

class MusicPlayFragment : Fragment() {
    private lateinit var binding: FragmentMusicPlayBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val link = arguments?.getString("url")
        val judul = arguments?.getString("judul")
        val pencipta = arguments?.getString("pencipta")
        val banner = arguments?.getString("banner")

        binding.apply {
            judulLagu.text = judul
            penciptaTxt.text = pencipta
            Glide.with(requireContext())
                .load(banner)
                .into(binding.imageView)
            toolbarJudul.text = judul
        }

        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer?.setDataSource(link)
            mediaPlayer?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.button.setOnClickListener {
            onPlayButtonClick()
        }

        binding.seekbar.max = mediaPlayer?.duration ?: 0
        updateSeekBar()

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    binding.startTime.text = formatTime(progress)
                    binding.remainingTime.text = formatTime(mediaPlayer?.duration?.minus(progress) ?: 0)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun onPlayButtonClick() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            binding.button.setImageResource(android.R.drawable.ic_media_play)
        } else {
            mediaPlayer?.start()
            binding.button.setImageResource(android.R.drawable.ic_media_pause)
            updateSeekBar()
        }
    }

    private fun updateSeekBar() {
        handler.postDelayed({
            val currentPosition = mediaPlayer?.currentPosition ?: 0
            binding.seekbar.progress = currentPosition
            binding.startTime.text = formatTime(currentPosition)
            binding.remainingTime.text = formatTime(mediaPlayer?.duration?.minus(currentPosition) ?: 0)
            updateSeekBar()
        }, 1000)
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}
