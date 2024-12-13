package com.example.nutrimamadaily

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class FyeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    lateinit var descList: Array<String>
    lateinit var detailImage: Array<Int>
    private lateinit var myAdapter: AdapterClass
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<DataClass>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fye)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Data untuk RecyclerView
        imageList = arrayOf(
            R.drawable.ic_ayam,
            R.drawable.ic_bayam,
            R.drawable.ic_brokoli,
            R.drawable.ic_daging,
            R.drawable.ic_delima,
            R.drawable.ic_hatisapi,
            R.drawable.ic_ikan,
            R.drawable.ic_kacangedamame,
            R.drawable.ic_kacangpanjang,
            R.drawable.ic_telur
        )

        titleList = arrayOf(
            "Ayam", "Bayam", "Brokoli", "Daging", "Delima", "Hati Sapi", "Ikan", "Kacang Edamame", "Kacang Panjang", "Telur"
        )

        descList = arrayOf(
            getString(R.string.Ayam),
            getString(R.string.Bayam),
            getString(R.string.Brokoli),
            getString(R.string.Daging),
            getString(R.string.Delima),
            getString(R.string.HatiSapi),
            getString(R.string.Ikan),
            getString(R.string.KacangEdamame),
            getString(R.string.KacangPanjang),
            getString(R.string.Telur)
        )

        detailImage = arrayOf(
            R.drawable.ic_ayam,
            R.drawable.ic_bayam,
            R.drawable.ic_brokoli,
            R.drawable.ic_daging,
            R.drawable.ic_delima,
            R.drawable.ic_hatisapi,
            R.drawable.ic_ikan,
            R.drawable.ic_kacangedamame,
            R.drawable.ic_kacangpanjang,
            R.drawable.ic_telur
        )

        // Inisialisasi RecyclerView dan SearchView
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf()
        searchList = arrayListOf()

        // Mengirim data ke Firebase Realtime Database
        sendDataToDatabase()

        // Mengambil data dari Firebase
        fetchDataFromDatabase()

        // Setup fungsionalitas pencarian
        setupSearchView()
    }

    // Mengirim data ke Firebase Realtime Database
    private fun sendDataToDatabase() {
        val database = FirebaseDatabase.getInstance().reference
        val nutritionRef = database.child("nutrition")

        for (i in titleList.indices) {
            val nutritionData = mapOf(
                "title" to titleList[i],
                "description" to descList[i],
                "imageResource" to imageList[i],
                "detailImageResource" to detailImage[i]
            )

            nutritionRef.child(titleList[i]).setValue(nutritionData)
        }
    }

    // Mengambil data dari Firebase Realtime Database
    private fun fetchDataFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference
        val nutritionRef = database.child("nutrition")

        nutritionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                searchList.clear()

                for (childSnapshot in snapshot.children) {
                    val title = childSnapshot.child("title").getValue(String::class.java) ?: ""
                    val description = childSnapshot.child("description").getValue(String::class.java) ?: ""
                    val imageResource = childSnapshot.child("imageResource").getValue(Int::class.java) ?: 0
                    val detailImageResource = childSnapshot.child("detailImageResource").getValue(Int::class.java) ?: 0

                    val dataClass = DataClass(imageResource, title, description, detailImageResource)
                    dataList.add(dataClass)
                }

                searchList.addAll(dataList)
                myAdapter = AdapterClass(searchList)
                recyclerView.adapter = myAdapter

                // Tambahkan click listener di sini
                myAdapter.onItemClick = { item ->
                    val intent = Intent(this@FyeActivity, DetailActivity::class.java)
                    intent.putExtra("android", item)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                // Optionally, you can use the local data as a fallback
            }
        })
    }

    // Setup pencarian
    private fun setupSearchView() {
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataList.forEach {
                        if (it.dataTitle.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(dataList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
    }
}