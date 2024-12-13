package com.example.nutrimamadaily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class TipsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass2>
    private lateinit var searchList: ArrayList<DataClass2>
    private lateinit var adapter: AdapterClass2
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        // Adjust padding for system bars (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView and SearchView
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialize data lists
        dataList = ArrayList()
        searchList = ArrayList()

        // List data for images and titles
        val imageList = arrayListOf(
            R.drawable.go, R.drawable.happy, R.drawable.tea, R.drawable.fetus,
            R.drawable.heartburn, R.drawable.tamarind, R.drawable.nutrition, R.drawable.anemia,
            R.drawable.bicycle, R.drawable.sashimi, R.drawable.pointer, R.drawable.cough, R.drawable.fruit
        )
        val titleList = arrayListOf(
            "Berpergian selama Kehamilan", "Terapkan ini Agar Bahagia Jalani Kehamilan",
            "Tips Aman Minum Teh untuk Bumil", "Trik Menambah Cairan Ketuban Secara Alami",
            "Trik untuk Atasi Heartburn Secara Alami", "Manfaat Mengonsumsi Asam Jawa saat Hamil",
            "Wajib Penuhi Nutrisi ini di Trimester 1", "Jangan Makan Sembarangan, Waspada Anemia",
            "Bolehkah Bersepeda saat Hamil?", "Salmon Mentai, Amankah Dikonsumsi Bumil?",
            "Tips Aman Gunakan Toilet Jongkok saat Hamil Tua", "Cara Tepat Atasi Batuk saat Hamil",
            "Tips Sehat Makan Buah selama Hamil"
        )

        // Mengisi data untuk DataClass2
        for (i in imageList.indices) {
            dataList.add(DataClass2(imageList[i], titleList[i]))
        }
        searchList.addAll(dataList)

        // Inisialisasi AdapterClass2 dengan searchList
        adapter = AdapterClass2(searchList)
        recyclerView.adapter = adapter

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.lowercase(Locale.getDefault()).orEmpty()
                searchList.clear()

                if (searchText.isNotEmpty()) {
                    dataList.forEach {
                        if (it.dataTitle.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                } else {
                    searchList.addAll(dataList)
                }

                // Update adapter
                adapter.notifyDataSetChanged()
                return false
            }
        })
    }
}
