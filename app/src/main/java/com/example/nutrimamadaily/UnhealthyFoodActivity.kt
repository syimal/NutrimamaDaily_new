// UnhealthyFoodActivity
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

class UnhealthyFoodActivity : AppCompatActivity() {

    private var initialPoints = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UnhealthyFoodAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unhealthy_food)

        // Ambil nilai poin dari intent sebelumnya
        initialPoints = intent.getIntExtra("points", 0)

        // Inisialisasi RecyclerView dan Firestore
        recyclerView = findViewById(R.id.recyclerViewUnhealthy)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        // List makanan tidak sehat
        val unhealthyFoodList = mutableListOf<String>()

        // QUERY FIRESTORE: Ambil hanya makanan tidak sehat
        db.collection("makanan")
            .whereEqualTo("sehat", false)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val namaMakanan = document.getString("Nama") ?: ""
                    unhealthyFoodList.add(namaMakanan)
                }

                // Inisialisasi adapter dan hubungkan dengan RecyclerView
                adapter = UnhealthyFoodAdapter(unhealthyFoodList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
                Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
            }

        // Tombol Submit untuk berpindah ke JumlahMakananActivity
        val btnSubmit = findViewById<Button>(R.id.btnOke2)
        btnSubmit.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih setidaknya satu makanan tidak sehat!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tambahkan 25 poin untuk makanan tidak sehat
            val progress = initialPoints + 25
            // Logging untuk memeriksa data yang akan dikirim
            Log.d("UnhealthyFoodActivity", "Selected Unhealthy Items: $selectedItems")
            Log.d("UnhealthyFoodActivity", "Progress: $progress")


            // Pindah ke JumlahMakananActivity
            val intent = Intent(this, JumlahMakananActivity::class.java).apply {
                putExtra("progress_unhealthy", progress)
                putExtra("selected_unhealthy_items", selectedItems.joinToString("\n"))
            }
            startActivity(intent)
            finish()
        }
    }
}
