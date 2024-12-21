package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Tasks
import java.text.SimpleDateFormat
import java.util.*

data class ItemRiwayat(
    val makan: String,
    val date: String
)

class SummaryActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var summaryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        db = FirebaseFirestore.getInstance()
        summaryTextView = findViewById(R.id.textView)

        val selectedHealthyItems =
            intent.getStringExtra("selected_healthy_items")?.split("\n") ?: emptyList()
        val selectedUnhealthyItems =
            intent.getStringExtra("selected_unhealthy_items")?.split("\n") ?: emptyList()

        val allSelectedItems = selectedHealthyItems + selectedUnhealthyItems

        val foodType = when {
            selectedHealthyItems.size > selectedUnhealthyItems.size -> "Healthy"
            selectedUnhealthyItems.size > selectedHealthyItems.size -> "Unhealthy"
            selectedUnhealthyItems.size == selectedHealthyItems.size -> "Mixed"
            else -> "None"
        }

        updateSummary(allSelectedItems, foodType)

        val buttonOke = findViewById<Button>(R.id.buttonoke)
        buttonOke.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateSummary(allSelectedItems: List<String>, foodType: String) {
        val summaryBuilder = StringBuilder()
        summaryBuilder.append("Ringkasan Nutrisi Anda Hari Ini:\n\n")

        // Tambahkan daftar makanan yang dipilih
        summaryBuilder.append("Makanan yang Dipilih:\n")
        allSelectedItems.forEach { item -> summaryBuilder.append(" $item\n") }

        // Tambahkan kategori makanan
        summaryBuilder.append("\nKategori Makanan: $foodType")

        // Ambil tanggal saat ini
        val currentDate = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        // Fetch nutrition data dan tambahkan ke summary
        fetchNutritionData(allSelectedItems, summaryBuilder) {
            saveSummaryToFirestore(summaryBuilder.toString(), currentDate, foodType)
            summaryTextView.text = summaryBuilder.toString()
        }
    }

    private fun fetchNutritionData(
        allSelectedItems: List<String>,
        summaryBuilder: StringBuilder,
        onComplete: () -> Unit
    ) {
        val chunkedItems = allSelectedItems.chunked(10)
        var totalZatBesi = 0.0
        val nutritionDetails = mutableListOf<String>()

        val tasks = chunkedItems.map { chunk ->
            db.collection("makanan")
                .whereIn("Nama", chunk)
                .get()
        }

        Tasks.whenAllSuccess<QuerySnapshot>(tasks)
            .addOnSuccessListener { results ->
                results.forEach { result ->
                    result.forEach { document ->
                        val namaMakanan = document.getString("Nama") ?: "Tidak diketahui"
                        val zatBesi = (document.get("zatBesi") as? Number)?.toDouble() ?: 0.0

                        totalZatBesi += zatBesi
                        nutritionDetails.add("$namaMakanan - Zat Besi: $zatBesi mg")
                    }
                }

                // Simpan total zat besi ke SharedPreferences
                val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                val existingZatBesi = sharedPref.getFloat("TOTAL_ZAT_BESI", 0f)
                val newTotalZatBesi = existingZatBesi + totalZatBesi.toFloat()

                with(sharedPref.edit()) {
                    putFloat("TOTAL_ZAT_BESI", newTotalZatBesi)
                    apply()
                }

                // Tambahkan rincian zat besi ke summary
                if (nutritionDetails.isNotEmpty()) {
                    summaryBuilder.append("\n\nRincian Zat Besi:\n")
                    nutritionDetails.forEach { detail -> summaryBuilder.append("$detail\n") }
                    summaryBuilder.append("\nTotal Zat Besi: $totalZatBesi mg")
                } else {
                    summaryBuilder.append("\n\nTidak ada rincian zat besi yang ditemukan.")
                }

                onComplete()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mengambil data makanan!", Toast.LENGTH_SHORT).show()
                Log.e("SummaryActivity", "Error fetching data: ${exception.localizedMessage}")
            }
    }

    private fun saveSummaryToFirestore(summary: String, date: String, foodType: String) {
        val summaryData = mapOf(
            "summary" to summary,
            "date" to date,
            "category" to foodType
        )

        db.collection("riwayat")
            .add(summaryData)
            .addOnSuccessListener {
                Log.d("SummaryActivity", "Summary saved to Firestore successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("SummaryActivity", "Failed to save summary to Firestore: ", e)
            }
    }
}
