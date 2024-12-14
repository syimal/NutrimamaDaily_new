package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class TipsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass2>
    private lateinit var searchList: ArrayList<DataClass2>
    lateinit var descList: Array<String>
    private lateinit var myAdapter: AdapterClass2
    private lateinit var adapter: AdapterClass2
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        descList = arrayOf(
            getString(R.string.go),
            getString(R.string.happy),
            getString(R.string.tea),
            getString(R.string.fetus),
            getString(R.string.heartburn),
            getString(R.string.tamarind),
            getString(R.string.nutrition),
            getString(R.string.anemia),
            getString(R.string.bicycle),
            getString(R.string.sashimi),
            getString(R.string.pointer),
            getString(R.string.cough),
            getString(R.string.fruit)
        )

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = ArrayList()
        searchList = ArrayList()

        for (i in imageList.indices) {
            dataList.add(DataClass2(imageList[i], titleList[i], descList[i]))
        }
        searchList.addAll(dataList)

        adapter = AdapterClass2(searchList)
        recyclerView.adapter = adapter

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

                adapter.notifyDataSetChanged()
                return false
            }
        })

        myAdapter=AdapterClass2(searchList)
        recyclerView.adapter = myAdapter

        myAdapter.onItemClick ={
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }
    }
}