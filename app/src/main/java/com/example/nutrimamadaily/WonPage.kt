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
    private var isNavigating = false
    private var isProgressRunning = false // Flag untuk mencegah pemanggilan ulang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.won_page)

        val totalProgress = intent.getIntExtra("progress", 0).coerceAtMost(100)
        val selectedItems = intent.getStringExtra("selected_healthy_items")
        val selectedPorsi = intent.getStringExtra("selected_porsi")
        val unhealthyProgress = intent.getIntExtra("progress_unhealthy", 0)
        val selectedUnealthyItems = intent.getStringExtra("selected_unhealthy_items")

        circularProgressBar = findViewById(R.id.circularProgressBar)
        val tvFinalProgress = findViewById<TextView>(R.id.tvFinalProgress)
        val btnStartPlan = findViewById<Button>(R.id.btnStartPlan)

        tvFinalProgress.text = "Progres Anda: $totalProgress%"

        // Menambah 1% progres di Homepage hanya ketika progress sudah 100%
        btnStartPlan.setOnClickListener {
            if (!isNavigating) {
                isNavigating = true
                Toast.makeText(this, "Navigasi ke halaman ringkasan...", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, SummaryActivity::class.java).apply {
                    putExtra("progress", totalProgress)
                    putExtra("selected_healthy_items", selectedItems)
                    putExtra("selected_porsi", selectedPorsi)
                    putExtra("progress_unhealthy", unhealthyProgress)
                    putExtra("selected_unhealthy_items", selectedUnealthyItems)
                }

                // Kirim intent untuk memperbarui progres di Homepage
                if (totalProgress == 100) {
                    intent.putExtra("update_progress", true)  // Tanda bahwa progres sudah 100% untuk diupdate
                } else {
                    intent.putExtra("update_progress", false)  // Tidak mengupdate progres jika tidak 100%
                }

                startActivity(intent)
                finish() // Mengakhiri aktivitas ini setelah berpindah
            }
        }

        // Jalankan progres bar hanya jika belum berjalan
        if (!isProgressRunning) {
            isProgressRunning = true
            simulateProgress(totalProgress)
        }
    }

    private fun simulateProgress(targetProgress: Int) {
        handler.post(object : Runnable {
            override fun run() {
                if (progress < targetProgress) {
                    progress++
                    circularProgressBar.setProgress(progress)
                    handler.postDelayed(this, 50)
                } else {
                    // Jika sudah selesai, hentikan progress
                    isProgressRunning = false
                }
            }
        })
    }
}
