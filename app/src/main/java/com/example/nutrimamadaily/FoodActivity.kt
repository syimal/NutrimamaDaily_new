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

class FoodActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        recyclerView = findViewById(R.id.recyclerViewFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        fetchFoodData()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih setidaknya satu makanan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedHealthy = selectedItems.filter { it.sehat }.joinToString("\n") { it.nama }
            val selectedUnhealthy = selectedItems.filter { !it.sehat }.joinToString("\n") { it.nama }

            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("selected_healthy_items", selectedHealthy)
                putExtra("selected_unhealthy_items", selectedUnhealthy)
            }
            startActivity(intent)
        }
    }

    private fun fetchFoodData() {
        db.collection("makanan")
            .get()
            .addOnSuccessListener { result ->
                val foodList = mutableListOf<FoodItem>()
                for (document in result) {
                    val namaMakanan = document.getString("Nama") ?: ""
                    val sehat = document.getBoolean("sehat") ?: false
                    val zatBesi = (document.get("zatBesi") as? Number)?.toDouble() ?: 0.0
                    foodList.add(FoodItem(namaMakanan, sehat, zatBesi))
                }

                if (foodList.isNotEmpty()) {
                    adapter = FoodAdapter(foodList)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "Tidak ada makanan untuk ditampilkan.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
                Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
            }
    }
}
