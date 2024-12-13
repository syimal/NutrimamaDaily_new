package com.example.nutrimamadaily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RiwayatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)  // Pastikan ini merujuk ke layout yang sesuai

        val rvHistory: RecyclerView = findViewById(R.id.rvHistory)

        // Contoh Data Makan, sesuaikan dengan data yang sebenarnya
        val makan = mutableListOf(
            Item("Sate", "24 November 2023 10:24:06"),
            Item("Nasi Ayam", "24 November 2023 10:24:06"),
            Item("Mie Goreng", "25 November 2023 11:45:08")
        )

        // Menyiapkan RecyclerView dengan LinearLayoutManager
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = ItemAdapter(makan) { item ->
            // Aksi hapus item ketika tombol hapus diklik
            makan.remove(item)
            rvHistory.adapter?.notifyDataSetChanged()
        }
    }
}
