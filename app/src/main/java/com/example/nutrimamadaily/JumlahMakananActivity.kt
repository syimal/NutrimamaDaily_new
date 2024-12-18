package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class JumlahMakananActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var porsiAdapter: PorsiAdapter
    private var selectedPorsi: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jumlah_makanan)

        recyclerView = findViewById(R.id.recyclerViewPorsi)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val healthyProgress = intent.getIntExtra("progress_healthy", 0)
        val selectedHealthyItems = intent.getStringExtra("selected_healthy_items") ?: ""

        val unhealthyProgress = intent.getIntExtra("progress_unhealthy", 0)
        val selectedUnealthyItems = intent.getStringExtra("selected_unhealthy_items") ?: ""

        Log.d("Selected Unhealthy", selectedUnealthyItems)
        Log.d("Unhealty Progress", unhealthyProgress.toString())
        db.collection("jumlahPorsi")
            .get()
            .addOnSuccessListener { documents ->
                val porsiList = documents.mapNotNull { it.getString("Porsi") }
                setupRecyclerView(porsiList)
            }
            .addOnFailureListener { exception ->
                Log.e("JumlahMakananActivity", "Error fetching data", exception)
                Toast.makeText(this, "Gagal memuat data dari database!", Toast.LENGTH_SHORT).show()
            }

        btnSubmit.setOnClickListener {
            if (selectedPorsi.isNullOrEmpty()) {
                Toast.makeText(this, "Pilih jumlah porsi terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedProgress = healthyProgress + 75
            val intent = Intent(this, WonPage::class.java).apply {
                putExtra("progress", updatedProgress)
                putExtra("selected_healthy_items", selectedHealthyItems)
                putExtra("selected_porsi", selectedPorsi)
                putExtra("progress_unhealthy", unhealthyProgress)
                putExtra("selected_unhealthy_items", selectedUnealthyItems)
            }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(porsiList: List<String>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        porsiAdapter = PorsiAdapter(porsiList) { selected ->
            selectedPorsi = selected
        }
        recyclerView.adapter = porsiAdapter
    }
}
