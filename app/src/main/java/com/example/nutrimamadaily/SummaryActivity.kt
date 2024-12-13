package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // Referensi tombol "Oke"
        val buttonOke = findViewById<Button>(R.id.buttonoke)

        buttonOke.setOnClickListener {
            // Membuka UtamaActivity secara eksplisit
            val intent = Intent(this@SummaryActivity, HomepageActivity::class.java)
            startActivity(intent) // Memulai UtamaActivity
            finish() // Menutup SummaryActivity
        }

    }
}
