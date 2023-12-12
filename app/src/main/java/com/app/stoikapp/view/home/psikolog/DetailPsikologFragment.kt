package com.app.stoikapp.view.home.psikolog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.databinding.FragmentDetailPsikologBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DetailPsikologFragment : Fragment() {
    private lateinit var sharedPref: SharedPref
    private var namaUser = ""
    private var userId = ""
    private var formattedDate = ""
    private lateinit var binding: FragmentDetailPsikologBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPsikologBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())


        lifecycleScope.launchWhenStarted {
            sharedPref.getFullName.collect { fullName ->
                namaUser = fullName
            }
            sharedPref.getUserId.collect { id ->
                userId = id
            }
        }

        setupUI()
    }

    private fun setupUI() {
        binding.editTextHariTanggal.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }

        binding.editWaktu.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showTimePicker()
            }
        }

        binding.apply {
            val id = arguments?.getString("id")
            val nama = arguments?.getString("nama")
            val alamatPraktek = arguments?.getString("alamat_praktek")
            val deskripsi = arguments?.getString("deskripsi")
            val nomorTelepon = arguments?.getString("nomor_telepon")
            val lihatAlamat = arguments?.getString("lihat_alamat")
            val profile = arguments?.getString("profile")
            val seninJumat = arguments?.getString("senin_jumat")
            val sabtuMinggu = arguments?.getString("sabtu_minggu")
            val harga = arguments?.getInt("harga")

            namaPsikolog.text = nama
            txtAlamat.text = alamatPraktek
            txtDeskripsi.text = deskripsi
            txtNomorTelepon.text = nomorTelepon
            seninJumatTxt.text = seninJumat
            sabtuMingguTxt.text = sabtuMinggu
            Glide.with(requireContext()).load(profile).into(btnEditProfile)

            btnBookNow.setOnClickListener{

            }

//            btnBookNow.setOnClickListener {
//                lifecycleScope.launchWhenStarted {
//                    sharedPref.getUserId.collect { id ->
//                        userId = id
//
//                        val bookingId = FirebaseDatabase.getInstance().getReference("history/booking").child(userId)
//                            .push().key
//
//                        val selectedTime = editWaktu.text.toString()
//                        val catatan = editCatatan.text.toString()
//
//                        val time = extractTime(selectedTime)
//
//                        val booking = Booking(
//                            nama = namaUser,
//                            namaPsikolog = nama,
//                            kodeBook = bookingId,
//                            hari = formattedDate,
//                            waktu = time,
//                            biaya = harga.toString(),
//                            catatan = catatan
//                        )
//
//                        val databaseReference =
//                            FirebaseDatabase.getInstance().getReference("history/booking").child(userId)
//                                .child(bookingId ?: "")
//                        databaseReference.setValue(booking).addOnSuccessListener {
//                            Log.d("Booking", "Booking added to history with ID: $bookingId")
//                        }.addOnFailureListener {
//                            Log.e("Booking", "Failed to add booking to history")
//                        }
//                    }
//                }
//            }
        }
    }

    private fun extractTime(selectedTime: String): String {
        return selectedTime
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.editTextHariTanggal.setText(formattedDate)
                Log.d("Date Picker", "Selected date: $formattedDate")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    private fun formatDate(day: Int, month: Int, year: Int): String {
        val dayName =
            SimpleDateFormat("EEEE", Locale("id", "ID")).format(Date(year - 1900, month - 1, day))
        val monthName =
            SimpleDateFormat("MMMM", Locale("id", "ID")).format(Date(year - 1900, month - 1, day))
        return "$dayName, $day $monthName $year"
    }
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = "$selectedHour:$selectedMinute"
                binding.editWaktu.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }
}
