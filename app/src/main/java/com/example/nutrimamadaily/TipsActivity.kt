package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class TipsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdapterClass2
    private lateinit var databaseReference: DatabaseReference
    private var dataList = ArrayList<DataClass2>()
    private var searchList = ArrayList<DataClass2>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        recyclerView = findViewById(R.id.recyclerView)
        val searchView: SearchView = findViewById(R.id.search)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        databaseReference = FirebaseDatabase.getInstance().getReference("pregnancy_tips")
        myAdapter = AdapterClass2(searchList)
        recyclerView.adapter = myAdapter

        fetchDataFromFirebase()

        myAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, DetailActivity2::class.java)
            intent.putExtra("android", selectedItem)
            startActivity(intent)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }
        })
    }

    private fun fetchDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                searchList.clear()

                for (dataSnapshot in snapshot.children) {
                    val tip = dataSnapshot.getValue(DataClass2::class.java)
                    tip?.let { dataList.add(it) }
                }

                searchList.addAll(dataList)
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }

    private fun filterData(query: String?) {
        val searchText = query?.lowercase() ?: ""
        searchList.clear()

        if (searchText.isNotEmpty()) {
            dataList.forEach {
                if (it.dataTitle.lowercase().contains(searchText)) {
                    searchList.add(it)
                }
            }
        } else {
            searchList.addAll(dataList)
        }

        myAdapter.notifyDataSetChanged()
    }
}
