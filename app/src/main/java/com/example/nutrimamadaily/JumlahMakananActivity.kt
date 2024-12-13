package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class JumlahMakananActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jumlah_makanan)

        // Referensi elemen UI
        val rgJumlahPorsi = findViewById<RadioGroup>(R.id.rgJumlahPorsi)
        val btnSubmit = findViewById<Button>(R.id.BtnSubmit)

        // Dapatkan progres sebelumnya dari Intent (baik unhealthy dan healthy)
        val healthyProgress = intent.getIntExtra("progress_healthy", 0)
        val unhealthyProgress = intent.getIntExtra("progress_unhealthy", 0)
        Log.d("JumlahMakananActivity", "Healthy Progress: $healthyProgress") // Debugging
        Log.d("JumlahMakananActivity", "Unhealthy Progress: $unhealthyProgress") // Debugging

        // Gabungkan poin healthy dan unhealthy
        val totalProgress = healthyProgress + unhealthyProgress
        Log.d("JumlahMakananActivity", "Total Progress: $totalProgress") // Debugging

        // Set listener untuk tombol submit
        btnSubmit.setOnClickListener {
            // Cek apakah pengguna memilih salah satu porsi
            val selectedId = rgJumlahPorsi.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih jumlah porsi terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tampilkan Toast berdasarkan pilihan porsi
            val selectedPorsi = when (selectedId) {
                R.id.rbSatu -> "Anda memilih Satu Porsi"
                R.id.rbDua -> "Anda memilih Setengah Porsi"
                R.id.rbTiga -> "Anda memilih Seperempat Porsi"
                else -> ""
            }
            Toast.makeText(this, selectedPorsi, Toast.LENGTH_SHORT).show()

            // Tambahkan 25 poin ke total progres
            val updatedProgress = totalProgress + 25  // Menambahkan 25 poin

            // Kirim hasil ke halaman berikutnya (WonPage)
            val intent = Intent(this, WonPage::class.java)
            intent.putExtra("progress", updatedProgress)
            startActivity(intent)
        }
    }
}
