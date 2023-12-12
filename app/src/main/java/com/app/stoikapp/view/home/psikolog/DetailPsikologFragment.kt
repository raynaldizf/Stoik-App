package com.app.stoikapp.view.home.psikolog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.Booking
import com.app.stoikapp.data.model.ResponseGetToken
import com.app.stoikapp.data.network.ApiClient
import com.app.stoikapp.databinding.FragmentDetailPsikologBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

            btnBookNow.setOnClickListener {
                lifecycleScope.launchWhenStarted {
                    sharedPref.getUserId.collect { id ->
                        userId = id

                        val bookingId = FirebaseDatabase.getInstance().getReference("history/booking").child(userId)
                            .push().key

                        val selectedTime = editWaktu.text.toString()
                        val catatan = editCatatan.text.toString()

                        val time = extractTime(selectedTime)

                        val booking = Booking(
                            nama = namaUser,
                            namaPsikolog = nama,
                            kodeBook = bookingId,
                            hari = formattedDate,
                            waktu = time,
                            biaya = harga.toString(),
                            catatan = catatan
                        )

                        val databaseReference =
                            FirebaseDatabase.getInstance().getReference("history/booking").child(userId)
                                .child(bookingId ?: "")
                        databaseReference.setValue(booking).addOnSuccessListener {
                            getSnapToken()
                            Log.d("Booking", "Booking added to history with ID: $bookingId")
                        }.addOnFailureListener {
                            Log.e("Booking", "Failed to add booking to history")
                        }
                    }
                }
            }
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

    private fun initiatePayment(snapToken: String) {
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-nH08O1W0uYJz1fRg")
            .setContext(requireContext())
            .setTransactionFinishedCallback { result ->
                handleTransactionResult(result)
            }
            .setMerchantBaseUrl("https://app.sandbox.midtrans.com/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        val transactionRequest = TransactionRequest(System.currentTimeMillis().toString(), 10000.0)

        val detail = com.midtrans.sdk.corekit.models.ItemDetails("1", 10000.0, 1, "Psikolog")
        val itemDetails = ArrayList<com.midtrans.sdk.corekit.models.ItemDetails>()
        itemDetails.add(detail)
        uiKitDetails(transactionRequest)
        transactionRequest.itemDetails = itemDetails

        MidtransSDK.getInstance().transactionRequest = transactionRequest

        MidtransSDK.getInstance().startPaymentUiFlow(requireContext(), snapToken)
    }

    private fun uiKitDetails(transactionRequest: TransactionRequest) {
        val customerDetail = com.midtrans.sdk.corekit.models.CustomerDetails()
        customerDetail.firstName = "Stoik"
        customerDetail.lastName = "App"
        customerDetail.email = "raynaldizoel@gmail.com"
        customerDetail.phone = "081234567890"

        transactionRequest.customerDetails = customerDetail
    }

    private fun getSnapToken() {
        ApiClient.instance.getMidtrans().enqueue(object : Callback<ResponseGetToken> {
            override fun onResponse(
                call: Call<ResponseGetToken>,
                response: Response<ResponseGetToken>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Log.d("token", token.toString())

                    // Call function to initiate payment
                    token?.let { initiatePayment(it) }
                }
            }

            override fun onFailure(call: Call<ResponseGetToken>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleTransactionResult(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    logTransactionDetails(result)
                    showToast("Payment successful")
                }

                TransactionResult.STATUS_PENDING -> {
                    logTransactionDetails(result)
                    showToast("Payment pending")
                }

                TransactionResult.STATUS_FAILED -> {
                    logTransactionDetails(result)
                    showToast("Payment failed")
                }

                TransactionResult.STATUS_INVALID -> {
                    logTransactionDetails(result)
                    showToast("Invalid payment status")
                }
            }
            result.response.transactionStatus.toString()
        } else if (result.isTransactionCanceled) {
            logTransactionDetails(result)
            showToast("Transaction canceled")
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, ignoreCase = true)) {
                logTransactionDetails(result)
                showToast("Invalid transaction status")
            } else {
                logTransactionDetails(result)
                showToast("Error during transaction")
            }
        }
    }

    private fun logTransactionDetails(result: TransactionResult) {
        val transactionStatus = result.response?.transactionStatus ?: "Unknown"
        val orderId = result.response?.transactionId ?: "Unknown"

        val logMessage = "Transaction Status: $transactionStatus, Order ID: $orderId"
        println(logMessage)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
