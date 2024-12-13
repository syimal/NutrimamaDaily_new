package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WonPage : AppCompatActivity() {

    private lateinit var circularProgressBar: CircularProgressBar
    private var progress = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isNavigating = false // Untuk mencegah navigasi ganda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.won_page) // Pastikan layout sesuai

        // Ambil data progress dari Intent
        val totalProgress = intent.getIntExtra("progress", 0).coerceAtMost(100) // Batas maksimum 100

        // Referensi elemen UI
        circularProgressBar = findViewById(R.id.circularProgressBar)
        val tvFinalProgress = findViewById<TextView>(R.id.tvFinalProgress)
        val btnStartPlan = findViewById<Button>(R.id.btnStartPlan)

        // Set teks untuk progres yang diterima
        tvFinalProgress.text = "Progres Anda: $totalProgress%"

        // Tombol navigasi
        btnStartPlan.setOnClickListener {
            if (!isNavigating) {
                isNavigating = true
                Toast.makeText(this, "Navigasi ke halaman berikutnya...", Toast.LENGTH_SHORT).show()

                // Navigasi ke SummaryActivity atau halaman lain
                val intent = Intent(this, SummaryActivity::class.java) // Ubah sesuai activity tujuan
                startActivity(intent)
            }
        }

        // Simulasikan progress bar
        simulateProgress(totalProgress)
    }

    private fun simulateProgress(targetProgress: Int) {
        handler.post(object : Runnable {
            override fun run() {
                if (progress < targetProgress) {
                    progress++
                    circularProgressBar.setProgress(progress)
                    handler.postDelayed(this, 50) // Update setiap 50ms
                }
            }
        })
    }
}
