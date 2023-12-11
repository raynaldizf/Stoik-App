package com.app.stoikapp.view.diagnosis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.stoikapp.R
import com.app.stoikapp.data.datastore.SharedPref
import com.app.stoikapp.data.model.BasisPengetahuanModel
import com.app.stoikapp.databinding.FragmentDiagnosisBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding
    private var hasil: String = ""
    lateinit var sharedPref: SharedPref
    private val gejalaList = listOf(
        "GJ01", "GJ02", "GJ03", "GJ04", "GJ05", "GJ06", "GJ07", "GJ08",
        "GJ09", "GJ10", "GJ11", "GJ12", "GJ13", "GJ14", "GJ15", "GJ16",
        "GJ17", "GJ18", "GJ19", "GJ20", "GJ21", "GJ22", "GJ23", "GJ24",
        "GJ25", "GJ26", "GJ27", "GJ28", "GJ29"
    )

    private val basisPengetahuan: List<BasisPengetahuanModel> = listOf(
        BasisPengetahuanModel("GJ01", 0.8, 0.0, 0.0, 0.0),
        BasisPengetahuanModel("GJ02", 0.6, 0.6, 0.0, 0.0),
        BasisPengetahuanModel("GJ03", 0.4, 0.4, 0.6, 0.0),
        BasisPengetahuanModel("GJ04", 0.6, 0.0, 0.0, 0.0),
        BasisPengetahuanModel("GJ05", 0.0, 0.0, 0.4, 0.8),
        BasisPengetahuanModel("GJ06", 0.0, 0.8, 0.0, 0.0),
        BasisPengetahuanModel("GJ07", 0.0, 0.6, 0.0, 0.0),
        BasisPengetahuanModel("GJ08", 0.0, 0.0, 0.0, 1.0),
        BasisPengetahuanModel("GJ09", 0.0, 0.0, 1.0, 0.0),
        BasisPengetahuanModel("GJ10", 0.0, 0.0, 0.8, 0.8),
        BasisPengetahuanModel("GJ11", 0.0, 1.0, 0.0, 0.0),
        BasisPengetahuanModel("GJ12", 0.8, 0.8, 0.4, 0.4),
        BasisPengetahuanModel("GJ13", 0.0, 0.0, 1.0, 0.0),
        BasisPengetahuanModel("GJ14", 0.0, 1.0, 0.0, 0.0),
        BasisPengetahuanModel("GJ15", 0.0, 0.6, 0.8, 0.4),
        BasisPengetahuanModel("GJ16", 0.0, 0.8, 0.0, 0.0),
        BasisPengetahuanModel("GJ17", 0.4, 0.0, 0.6, 0.6),
        BasisPengetahuanModel("GJ18", 0.0, 0.0, 0.0, 1.0),
        BasisPengetahuanModel("GJ19", 0.0, 0.0, 0.4, 0.8),
        BasisPengetahuanModel("GJ20", 1.0, 0.0, 0.0, 0.8),
        BasisPengetahuanModel("GJ21", 0.0, 0.8, 0.4, 0.8),
        BasisPengetahuanModel("GJ22", 0.0, 0.4, 0.0, 0.4),
        BasisPengetahuanModel("GJ23", 0.0, 0.0, 0.8, 0.0),
        BasisPengetahuanModel("GJ24", 0.0, 0.0, 0.8, 0.8),
        BasisPengetahuanModel("GJ25", 0.0, 0.0, 0.0, 1.0),
        BasisPengetahuanModel("GJ26", 0.0, 0.0, 0.0, 0.6),
        BasisPengetahuanModel("GJ27", 0.0, 0.0, 0.6, 0.0),
        BasisPengetahuanModel("GJ28", 0.0, 0.0, 0.6, 0.0),
        BasisPengetahuanModel("GJ29", 0.0, 0.0, 0.0, 0.8)
    )
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private val jawabanMap: MutableMap<String, Double> = mutableMapOf()
    private var gejalaIndex = 0
    private var currentGejala = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        setupFirebase()
        setupButtonListeners()
        showNextQuestion()
    }
    private fun setupFirebase() {
        lifecycleScope.launch {
            sharedPref.getUserId.collect{userId ->
                Log.d("DiagnosisFragment", "User ID: $userId")
                database = FirebaseDatabase.getInstance()
                databaseReference = database.getReference("history/diagnosis").child(userId)
            }
        }

    }

    private fun pushDiagnosisToFirebase(hasilDiagnosa: String, solusi: String) {
        val diagnosisKey = databaseReference.push().key
        if (diagnosisKey != null) {
            val diagnosisDetails = mapOf(
                "diagnosa" to hasilDiagnosa,
                "solusi" to solusi,
            )

            databaseReference.child(diagnosisKey).setValue(diagnosisDetails)
                .addOnSuccessListener {
                    Log.d("DiagnosisFragment", "Diagnosis result pushed to Firebase successfully")
                }
                .addOnFailureListener {
                    Log.e("DiagnosisFragment", "Failed to push diagnosis result to Firebase: $it")
                }
        }
    }
    private fun setupButtonListeners() {
        binding.tidak.setOnClickListener { onButtonClicked("tidak") }
        binding.mungkin.setOnClickListener { onButtonClicked("mungkin") }
        binding.kemungkinanBesar.setOnClickListener { onButtonClicked("kemungkinanBesar") }
        binding.hampirPasti.setOnClickListener { onButtonClicked("hampirPasti") }
        binding.pasti.setOnClickListener { onButtonClicked("pasti") }
    }

    private fun onButtonClicked(jawaban: String) {
        jawabanMap[currentGejala] = cfFromJawaban(jawaban)
        Log.d("DiagnosisFragment", "Jawaban untuk $currentGejala: $jawaban")
        Log.d("DiagnosisFragment", "jawabanMap: $jawabanMap")


        if (gejalaIndex < gejalaList.size - 1) {
            gejalaIndex++
            showNextQuestion()
        } else {
            val hasilDiagnosa = hitungCFCombine(jawabanMap)
            Log.d("DiagnosisFragment", "Hasil diagnosa: $hasilDiagnosa")

            tampilkanHasilDiagnosa(hasilDiagnosa)
        }
    }

    private fun showNextQuestion() {
        currentGejala = gejalaList[gejalaIndex]
        binding.textViewPertanyaan.text = pertanyaanDariGejala(currentGejala)
    }

    private fun pertanyaanDariGejala(gejala: String): String {
        return when (gejala) {
            "GJ01" -> "1. Apakah Anda sering merasa sedih atau memiliki mood yang buruk?"
            "GJ02" -> "2. Mudah merasa lelah dalam beraktivitas?"
            "GJ03" -> "3. Apakah konsentrasi dan perhatian Anda berkurang?"
            "GJ04" -> "4. Apakah Anda mudah bosan dan kesal?"
            "GJ05" -> "5. Apakah kadang-kala Anda melamun atau menghayal berkepanjangan?"
            "GJ06" -> "6. Apakah Anda tidak berantusiasme?"
            "GJ07" -> "7. Apakah Anda selalu merasa khawatir atau pesimis?"
            "GJ08" -> "8. Apakah Anda merasa putus asa dan kehilangan harapan masa depan?"
            "GJ09" -> "9. Apakah wajah Anda terlihat sedih dan kadang-kala berlinang air mata?"
            "GJ10" -> "10. Apakah Anda mengalami insomnia atau kesulitan tidur?"
            "GJ11" -> "11. Apakah Anda selalu gelisah?"
            "GJ12" -> "12. Apakah Anda tidak puas dengan diri sendiri dan merasa tidak berharga?"
            "GJ13" -> "13. Apakah Anda terusik dengan segala hal?"
            "GJ14" -> "14. Apakah Anda selalu terlihat lesu dan tidak bertenaga?"
            "GJ15" -> "15. Apakah Anda menjadi tidak tertarik dengan minat dan hobi yang disenangi?"
            "GJ16" -> "16. Apakah Anda sering merasa kesunyian?"
            "GJ17" -> "17. Apakah Anda memiliki pikiran rasa bersalah?"
            "GJ18" -> "18. Apakah Anda memiliki pikiran bahwa Anda dianiaya?"
            "GJ19" -> "19. Apakah Anda merasa benci dan selalu mengkritik diri sendiri?"
            "GJ20" -> "20. Apakah Anda gampang tersinggung dan tersentuh?"
            "GJ21" -> "21. Apakah nafsu makan Anda hilang?"
            "GJ22" -> "22. Apakah Anda cemas akan performa Anda?"
            "GJ23" -> "23. Apakah Anda sangat sensitive dan tidak terkontrol emosi?"
            "GJ24" -> "24. Apakah Anda introvert?"
            "GJ25" -> "25. Apakah Anda selalu berpikir untuk mengakhiri hidup?"
            "GJ26" -> "26. Apakah Anda tidak mudah menentukan pilihan?"
            "GJ27" -> "27. Apakah Anda tidak mudah beraktivitas dengan baik?"
            "GJ28" -> "28. Apakah Anda memiliki berat badan yang tidak stabil?"
            "GJ29" -> "29. Apakah Anda insecure atau kurang percaya diri?"
            else -> "Pertanyaan tidak ditemukan"
        }
    }

    private fun hitungCFCombine(jawabanMap: Map<String, Double>): Double {
        val cfMap: MutableMap<String, Double> = mutableMapOf()

        for ((gejala, jawaban) in jawabanMap) {
            val basisPengetahuanModel = basisPengetahuan.find { it.gejala == gejala }
            if (basisPengetahuanModel != null) {
                cfMap[gejala] = jawaban
            }
        }

        return if (cfMap.isNotEmpty()) combineCF(cfMap.values.toDoubleArray()) else 0.0
    }

    private fun combineCF(cfArray: DoubleArray): Double {
        val n = cfArray.size
        var cfCombine = cfArray[0]

        for (i in 1 until n) {
            cfCombine = cfCombine + cfArray[i] * (1 - cfCombine)
        }

        return cfCombine
    }

    private fun tampilkanHasilDiagnosa(hasilDiagnosa: Double) {
        val bundle = Bundle()
        var solusi = ""
        when {
            hasilDiagnosa >= 0.1 && hasilDiagnosa <= 0.2 -> {
                hasil = "Anda mengalami Gangguan Mood. Segera konsultasikan dengan dokter."
                solusi =
                    "Berusahalah untuk tidak menarik diri dari orang-orang terdekat. Tetaplah berkomunikasi dengan keluarga dan para sahabat, karena mereka yang mencintaimu pasti akan siap membantu kamu mengatasi depresi."

            }

            hasilDiagnosa >= 0.3 && hasilDiagnosa <= 0.4 -> {
                hasil = "Anda mengalami Depresi Ringan. Tetap perhatikan kesehatan Anda."
                solusi =
                    "Untuk mengatasi depresi, kamu perlu melakukan hal-hal yang membuatmu rileks dan berenergi. Hal itu termasuk gaya hidup sehat, belajar untuk mengelola stres dengan baik, dan menjadwalkan kegiatan yang menyenangkan."

            }

            hasilDiagnosa >= 0.5 && hasilDiagnosa <= 0.7 -> {
                hasil = "Anda mengalami Depresi Sedang. Segera konsultasikan dengan dokter."
                solusi =
                    "Tingkatkan suasana hati kamu dengan makanan yang kaya akan asam lemak omega-3. Nutrisi tersebut berperan penting dalam menstabilkan mood. Sumber terbaik untuk asam lemak omega-3 adalah salmon, sarden, tuna, atau suplemen minyak ikan."

            }

            hasilDiagnosa >= 0.8 && hasilDiagnosa <= 1.0 -> {
                hasil = "Anda mengalami Depresi Berat. Segera dapatkan pertolongan medis segera."
                solusi =
                    "Tidur atau istirahat yang cukup untuk mencegah mood swing. Menerapkan pola makan sehat dan bergizi seimbang. Bila perlu, perbanyak konsumsi makanan untuk mengatasi depresi. Melakukan aktivitas fisik dan berolahraga secara teratur. Melakukan cara sehat untuk mengelola stres, seperti menjalankan hobi, pijat, meditasi, atau bermain dengan hewan peliharaan. Menghindari konsumsi alkohol dan obat-obatan terlarang."
            }

            else -> {
                hasil = "Hasil diagnosa tidak dapat ditentukan. Segera konsultasikan dengan dokter."
                solusi =
                    "Hasil diagnosa tidak dapat ditentukan. Segera konsultasikan dengan dokter."
            }
        }

        pushDiagnosisToFirebase(hasil, solusi)

        bundle.putString("hasilDiagnosa", hasil)
        bundle.putString("solusi", solusi)
        Log.d("hasilDiagnosa", hasilDiagnosa.toString())
        Toast.makeText(context, "Diagnosis Berhasil", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            R.id.action_diagnosisFragment_to_hasilDiagnosisFragment,
            bundle
        )

    }

    private fun cfFromJawaban(jawaban: String): Double {
        return when (jawaban) {
            "tidak" -> 0.0
            "mungkin" -> 0.4
            "kemungkinanBesar" -> 0.6
            "hampirPasti" -> 0.8
            "pasti" -> 1.0
            else -> 0.0
        }
    }
}
