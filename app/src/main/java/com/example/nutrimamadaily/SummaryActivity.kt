package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val selectedHealthyItems = intent.getStringExtra("selected_healthy_items")?.split("\n") ?: emptyList()
        val selectedUnhealthyItems = intent.getStringExtra("selected_unhealthy_items")?.split("\n") ?: emptyList()
        val selectedPorsi = intent.getStringExtra("selected_porsi")

        Log.d("SummaryActivity", "Selected Healthy Items: $selectedHealthyItems")
        Log.d("SummaryActivity", "Selected Unhealthy Items: $selectedUnhealthyItems")
        Log.d("SummaryActivity", "Selected Porsi: $selectedPorsi")

        val selectedHealthyItems2 = intent.getStringExtra("selected_healthy_items")
            ?.split("\n")
            ?.map { it.trim() } // Remove extra spaces
            ?.filter { it.isNotEmpty() } // Exclude empty items
            ?: emptyList()

        val selectedUnhealthyItems2= intent.getStringExtra("selected_unhealthy_items")
            ?.split("\n")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()


        val allSelectedItems = selectedHealthyItems + selectedUnhealthyItems

        val foodType = when {
            selectedHealthyItems2.isNotEmpty() && selectedUnhealthyItems2.isEmpty() -> "Healthy"
            selectedUnhealthyItems2.isNotEmpty() && selectedHealthyItems2.isEmpty() -> "Unhealthy"
            selectedHealthyItems2.isNotEmpty() && selectedUnhealthyItems2.isNotEmpty() -> "Mixed"
            else -> "None"
        }

        updateSummary(allSelectedItems, selectedPorsi, foodType)

        val buttonOke = findViewById<Button>(R.id.buttonoke)
        buttonOke.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveSummaryToFirestore(summary: String, date: String, categoryMakanan: String) {
        val summaryData = mapOf(
            "summary" to summary,
            "date" to date,
            "category" to categoryMakanan
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

    private fun updateSummary(allSelectedItems: List<String>, selectedPorsi: String?, categoryMakanan:String) {
        val summaryBuilder = StringBuilder()
        summaryBuilder.append("Ringkasan Nutrisi Anda Hari Ini:\n\n")

        if (allSelectedItems.isNotEmpty()) {
            summaryBuilder.append("Makanan yang dipilih:\n")
            allSelectedItems.forEach { item -> summaryBuilder.append("- $item\n") }
        } else {
            summaryBuilder.append("Tidak ada makanan yang dipilih.\n")
            summaryTextView.text = summaryBuilder.toString()
            return
        }

        selectedPorsi?.let {
            summaryBuilder.append("\nPorsi yang dipilih: $it\n")
        } ?: run {
            summaryBuilder.append("\nPorsi tidak dipilih.\n")
        }

        val currentDate = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        fetchNutritionData(allSelectedItems, summaryBuilder) {
            saveSummaryToFirestore(summaryBuilder.toString(), currentDate, categoryMakanan)
        }
    }

    private fun fetchNutritionData(
        allSelectedItems: List<String>,
        summaryBuilder: StringBuilder,
        onComplete: () -> Unit
    ) {
        Log.d("SummaryActivity", "Fetching nutrition data for: $allSelectedItems")
        val chunkedItems = allSelectedItems.chunked(10)
        var totalZatBesi = 0
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
                        val zatBesi = document.getLong("zatBesi")?.toInt() ?: 0
                        Log.d("SummaryActivity", "Item: $namaMakanan, Zat Besi: $zatBesi")
                        totalZatBesi += zatBesi
                        nutritionDetails.add("$namaMakanan - Zat Besi: $zatBesi mg")
                    }
                }

                if (nutritionDetails.isNotEmpty()) {
                    summaryBuilder.append("\n\nRincian Zat Besi:\n")
                    nutritionDetails.forEach { detail -> summaryBuilder.append("$detail\n") }
                    summaryBuilder.append("\nTotal Zat Besi: $totalZatBesi mg")
                } else {
                    summaryBuilder.append("\n\nTidak ada rincian zat besi yang ditemukan.")
                }

                summaryTextView.text = summaryBuilder.toString()
                onComplete()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mengambil data makanan!", Toast.LENGTH_SHORT).show()
                Log.e("SummaryActivity", "Error fetching data: ${exception.localizedMessage}")
            }
    }
}

