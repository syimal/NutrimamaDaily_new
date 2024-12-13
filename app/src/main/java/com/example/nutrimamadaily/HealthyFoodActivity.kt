package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class HealthyFoodActivity : AppCompatActivity() {

    private var initialPoints = 0 // Untuk menyimpan poin awal (dari UtamaActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthy_food)

        // Mendapatkan nilai poin yang dikirim dari UtamaActivity
        initialPoints = intent.getIntExtra("points", 0)

        // Mengumpulkan semua CheckBox dengan ID dinamis
        val checkBoxes = mutableListOf<CheckBox>()
        for (i in 1..36) {
            val checkBoxId = resources.getIdentifier("cbHealthy$i", "id", packageName)
            val checkBox = findViewById<CheckBox?>(checkBoxId)
            if (checkBox != null) {
                checkBoxes.add(checkBox)
            }
        }

        val btnOke1 = findViewById<Button>(R.id.BtnOke1)

        // Ketika tombol "Next" ditekan
        btnOke1.setOnClickListener {
            val selectedItems = StringBuilder("Makanan Sehat yang Anda Pilih:\n")
            var selectedHealthyCount = 0

            // Loop untuk mengecek makanan sehat yang dipilih
            for ((index, checkBox) in checkBoxes.withIndex()) {
                if (checkBox.isChecked) {
                    selectedItems.append("${getFoodName(index + 1)}\n")
                    selectedHealthyCount++
                }
            }

            // Tambahkan 50 poin untuk tombol ini
            val progress = initialPoints + 50 // Poin ditambahkan berdasarkan tombol

            // Pindah ke JumlahMakananActivity dengan data yang dikirim
            val intent = Intent(this, JumlahMakananActivity::class.java).apply {
                putExtra("progress_healthy", progress) // Poin yang sudah dihitung
                putExtra("selected_healthy_items", selectedItems.toString()) // Mengirim daftar makanan sehat yang dipilih
            }
            startActivity(intent)
        }
    }

    private fun getFoodName(index: Int): String {
        return when (index) {
            1 -> "Nasi"
            2 -> "Salad Sayur"
            3 -> "Salad Buah"
            4 -> "Sayur Sop"
            5 -> "Soto Ayam"
            6 -> "Bubur"
            7 -> "Rawon"
            8 -> "Ikan Bakar"
            9 -> "Ikan Goreng"
            10 -> "Nasi Goreng"
            11 -> "Pecel"
            12 -> "Gado-Gado"
            13 -> "Pepes Ikan"
            14 -> "Sayur Bening Bayam"
            15 -> "Tempe Goreng"
            16 -> "Tahu Goreng"
            17 -> "Bubur Kacang Ijo"
            18 -> "Tumis Bayam"
            19 -> "Rujak Buah"
            20 -> "Bubur Sum-Sum"
            21 -> "Bubur Ketan Hitam"
            22 -> "Sop Daging"
            23 -> "Udang Saus Padang"
            24 -> "Kepiting Saus Padang"
            25 -> "Cumi Asam Manis"
            26 -> "Cumi Saus Tiram"
            27 -> "Cumi Saus Padang"
            28 -> "Ayam Bakar"
            29 -> "Tumis Kangkung"
            30 -> "Ayam Goreng"
            31 -> "Rendang"
            32 -> "Opor Ayam"
            33 -> "Dendeng Sapi"
            34 -> "Ayam Rica-Rica"
            35 -> "Tumis Tahu Tempe"
            36 -> "Pudding Buah"
            else -> "Makanan Tidak Diketahui"
        }
    }
}
