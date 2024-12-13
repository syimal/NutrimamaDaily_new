package com.example.nutrimamadaily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass2(private val dataList: ArrayList<DataClass2>) : RecyclerView.Adapter<AdapterClass2.ViewHolderClass>() {

    // Fungsi untuk membuat view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout2, parent, false)
        return ViewHolderClass(itemView)
    }

    // Fungsi untuk mendapatkan jumlah item
    override fun getItemCount(): Int {
        return dataList.size
    }

    // Fungsi untuk mengikat data ke view holder
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle
    }

    // ViewHolder untuk menghubungkan item dengan layout
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.image)
        val rvTitle: TextView = itemView.findViewById(R.id.title)
    }
}
