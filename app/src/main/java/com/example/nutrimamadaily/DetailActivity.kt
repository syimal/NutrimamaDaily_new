package com.example.nutrimamadaily

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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
        val dataClass = intent.getSerializableExtra("android") as? DataClass

        // Jika data tidak null, gunakan titel untuk mengambil data dari Firebase
        dataClass?.let { item ->
            fetchDetailFromFirebase(item.dataTitle)
        }
    }

    private fun fetchDetailFromFirebase(title: String) {
        val database = FirebaseDatabase.getInstance().reference
        val nutritionRef = database.child("nutrition").child(title)

        nutritionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val title = snapshot.child("title").getValue(String::class.java) ?: ""
                    val description = snapshot.child("description").getValue(String::class.java) ?: ""
                    val detailImageResource = snapshot.child("detailImageResource").getValue(Int::class.java) ?: 0

                    // Set data ke view
                    detailTitle.text = title
                    detailDesc.text = description
                    detailImage.setImageResource(detailImageResource)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                // Misalnya, tampilkan pesan error atau gunakan data default
            }
        })
    }
}