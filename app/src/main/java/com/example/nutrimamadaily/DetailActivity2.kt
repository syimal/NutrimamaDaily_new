package com.example.nutrimamadaily

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailImage: ImageView = findViewById(R.id.detailImage)
        val detailTitle: TextView = findViewById(R.id.detailTitle)
        val detailDesc: TextView = findViewById(R.id.detailDesc)

        val data = intent.getParcelableExtra<DataClass2>("android")

        if (data != null) {
            detailImage.setImageResource(data.dataImage)
            detailTitle.text = data.dataTitle
            detailDesc.text = data.dataDesc
        } else {
            Toast.makeText(this, "Tidak dapat memuat detail", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
