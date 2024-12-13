package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class UnhealthyFoodActivity : AppCompatActivity() {

    private var initialPoints = 0 // Untuk menyimpan poin awal (dari UtamaActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unhealthy_food)

        // Mendapatkan nilai poin yang dikirim dari UtamaActivity
        initialPoints = intent.getIntExtra("points", 0)

        // Mengumpulkan semua CheckBox dengan ID dinamis
        val checkBoxes = mutableListOf<CheckBox>()
        for (i in 1..20) {
            val checkBoxId = resources.getIdentifier("cbUnHealthy$i", "id", packageName)
            val checkBox = findViewById<CheckBox?>(checkBoxId)
            checkBox?.let { checkBoxes.add(it) }
        }

        val btnOke2 = findViewById<Button>(R.id.BtnOke2)

        // Ketika tombol "Next" ditekan
        btnOke2.setOnClickListener {
            val selectedItems = StringBuilder("Makanan Tidak Sehat yang Anda Pilih:\n")
            var selectedUnhealthyCount = 0

            // Loop untuk mengecek makanan tidak sehat yang dipilih
            for ((index, checkBox) in checkBoxes.withIndex()) {
                if (checkBox.isChecked) {
                    selectedItems.append("${getFoodName(index + 1)}\n")
                    selectedUnhealthyCount++
                }
            }

            // Tambahkan 25 poin untuk tombol ini
            val progress = initialPoints + 25 // Poin ditambahkan berdasarkan tombol

            // Pindah ke JumlahMakananActivity dengan data yang dikirim
            val intent = Intent(this, JumlahMakananActivity::class.java).apply {
                putExtra("progress_unhealthy", progress) // Poin yang sudah dihitung
                putExtra("selected_unhealthy_items", selectedItems.toString()) // Mengirim daftar makanan tidak sehat yang dipilih
            }
            startActivity(intent)
        }
    }

    private fun getFoodName(index: Int): String {
        return when (index) {
            1 -> "Seblak"
            2 -> "Mie Instan"
            3 -> "Kentang Goreng"
            4 -> "Burger"
            5 -> "Pizza"
            6 -> "Hotdog"
            7 -> "Nugget Ayam"
            8 -> "Cake"
            9 -> "Donat"
            10 -> "Pop Corn"
            11 -> "Ikan Sarden"
            12 -> "Es Krim"
            13 -> "Cireng"
            14 -> "Gorengan"
            15 -> "Jeroan"
            16 -> "Cokelat"
            17 -> "Bakso Bakar"
            18 -> "Mie Ayam"
            19 -> "Bakso"
            20 -> "Basreng"
            else -> "Makanan Tidak Diketahui"
        }
    }
}
