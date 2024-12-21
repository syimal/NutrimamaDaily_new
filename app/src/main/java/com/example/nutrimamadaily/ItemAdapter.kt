package com.example.nutrimamadaily

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val items: List<Item>,
    private val onDeleteClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvMakan: android.widget.TextView = view.findViewById(R.id.tvMakan)
        val tvDate: android.widget.TextView = view.findViewById(R.id.tvDate)
        val btnDelete: android.widget.ImageButton = view.findViewById(R.id.btnDelete)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set data ke views
        holder.tvMakan.text = item.makan
        holder.tvDate.text = item.date

        // Set warna background card berdasarkan posisi
        holder.cardView.setCardBackgroundColor(
            if (position % 2 == 0)
                Color.WHITE
            else
                Color.parseColor("#F5F5F5")
        )

        // Set click listener untuk tombol delete
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount() = items.size
}