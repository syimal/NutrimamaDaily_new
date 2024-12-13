package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UtamaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)

        // Inisialisasi Button
        val btnHealthy = findViewById<Button>(R.id.btnHealthy)
        val btnUnHealthy = findViewById<Button>(R.id.btnUnHealthy)

        // Listener untuk tombol "Makanan Sehat"
        btnHealthy.setOnClickListener {
            startFoodActivity(HealthyFoodActivity::class.java, 25)  // Kirim 25 poin untuk sehat
        }

        // Listener untuk tombol "Makanan Tidak Sehat"
        btnUnHealthy.setOnClickListener {
            startFoodActivity(UnhealthyFoodActivity::class.java, 25)  // Kirim 25 poin untuk tidak sehat
        }
    }

    // Fungsi untuk mengirim Intent dengan nilai poin
    private fun startFoodActivity(activity: Class<*>, points: Int) {
        val intent = Intent(this@UtamaActivity, activity)
        intent.putExtra("points", points)  // Mengirimkan nilai poin ke activity tujuan
        startActivity(intent)
    }

}
