package com.example.nutrimamadaily

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {

    private lateinit var detailImage: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        detailDesc = findViewById(R.id.detailDesc)

        // Ambil data dari intent
        val dataClass = intent.getParcelableExtra<DataClass>("android")

        // Jika data tidak null, gunakan titel untuk mengambil data dari Firebase
        dataClass?.let { item ->
            fetchDetailFromFirebase(item.dataTitle)
        } ?: run {
            // Handle scenario where no data was passed
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchDetailFromFirebase(title: String) {
        val database = FirebaseDatabase.getInstance().reference
        val nutritionRef = database.child("nutrition").child(title)

        nutritionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fetchedTitle = snapshot.child("title").getValue(String::class.java) ?: title
                    val description = snapshot.child("description").getValue(String::class.java) ?: "Deskripsi tidak tersedia"
                    val detailImageResource = snapshot.child("detailImageResource").getValue(Int::class.java) ?: 0

                    // Set data ke view
                    detailTitle.text = fetchedTitle
                    detailDesc.text = description

                    // Tambahkan pengecekan untuk gambar
                    if (detailImageResource != 0) {
                        detailImage.setImageResource(detailImageResource)
                    } else {
                        //
                    }
                } else {
                    // Data tidak ditemukan
                    Log.e("DetailActivity", "No data found for title: $title")
                    Toast.makeText(this@DetailActivity, "Informasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error dengan lebih detail
                Log.e("DetailActivity", "Database error: ${error.message}")
                Toast.makeText(this@DetailActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}