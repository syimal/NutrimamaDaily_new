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

class HealthyFoodActivity : AppCompatActivity() {

    private var initialPoints = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HealthyFoodAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthy_food)

        // Ambil nilai poin dari intent sebelumnya
        initialPoints = intent.getIntExtra("points", 0)

        // Inisialisasi RecyclerView dan Firestore
        recyclerView = findViewById(R.id.recyclerViewHealthyFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        // List untuk menyimpan data nama makanan sehat
        val healthyFoodList = mutableListOf<String>()

        // QUERY FIRESTORE: Ambil hanya makanan sehat
        db.collection("makanan")
            .whereEqualTo("sehat", true) // Filter: sehat == true
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val namaMakanan = document.getString("Nama") ?: "" // Ambil nama
                    healthyFoodList.add(namaMakanan) // Tambahkan ke list
                }
                // Set adapter setelah data berhasil diambil
                adapter = HealthyFoodAdapter(healthyFoodList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
                Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
            }

        // Tombol Submit untuk berpindah ke JumlahMakananActivity
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            val progress = initialPoints + 50

            // Pindah ke JumlahMakananActivity
            val intent = Intent(this, JumlahMakananActivity::class.java).apply {
                putExtra("progress_healthy", progress)
                putExtra("selected_healthy_items", selectedItems.joinToString("\n"))
            }
            startActivity(intent)
        }
    }
}
