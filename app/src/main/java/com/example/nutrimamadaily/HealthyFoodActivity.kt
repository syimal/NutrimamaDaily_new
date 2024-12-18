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

        initialPoints = intent.getIntExtra("points", 0)

        recyclerView = findViewById(R.id.recyclerViewHealthyFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        val healthyFoodList = mutableListOf<String>()

        db.collection("makanan")
            .whereEqualTo("sehat", true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val namaMakanan = document.getString("Nama") ?: ""
                    healthyFoodList.add(namaMakanan)
                }
                adapter = HealthyFoodAdapter(healthyFoodList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
                Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
            }

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih setidaknya satu makanan sehat!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progress = initialPoints + 50
            val intent = Intent(this, JumlahMakananActivity::class.java).apply {
                putExtra("progress_healthy", progress)
                putExtra("selected_healthy_items", selectedItems.joinToString("\n"))
            }
            startActivity(intent)
        }
    }
}
