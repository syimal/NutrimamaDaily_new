package com.example.nutrimamadaily

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RiwayatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        loadHistoryFromFirestore()
    }

    private fun loadHistoryFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val makan = mutableListOf<Item>()

        db.collection("riwayat")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val summary = document.getString("summary") ?: ""
                    val date = document.getString("date") ?: ""
                    makan.add(Item(summary, date))
                }

                val rvHistory: RecyclerView = findViewById(R.id.rvHistory)
                rvHistory.layoutManager = LinearLayoutManager(this)
                rvHistory.adapter = ItemAdapter(makan) { item ->
                    deleteHistoryFromFirestore(item, makan)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("RiwayatActivity", "Error loading history: ${exception.localizedMessage}")
            }
    }

    private fun deleteHistoryFromFirestore(item: Item, makan: MutableList<Item>) {
        val db = FirebaseFirestore.getInstance()

        db.collection("riwayat")
            .whereEqualTo("summary", item.makan)
            .whereEqualTo("date", item.date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("riwayat").document(document.id).delete()
                        .addOnSuccessListener {
                            makan.remove(item)
                            findViewById<RecyclerView>(R.id.rvHistory).adapter?.notifyDataSetChanged()
                            Log.d("RiwayatActivity", "History deleted successfully!")
                        }
                        .addOnFailureListener { e ->
                            Log.e("RiwayatActivity", "Error deleting history: ", e)
                        }
                }
            }
    }
}

