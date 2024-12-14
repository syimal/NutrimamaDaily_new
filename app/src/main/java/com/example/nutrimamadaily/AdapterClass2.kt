package com.example.nutrimamadaily

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass2 (private  val dataList: ArrayList<DataClass2>): RecyclerView.Adapter<AdapterClass2.ViewHolderClass2>(){

    var onItemClick: ((DataClass2) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass2 {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout2, parent, false)
        return  ViewHolderClass2(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass2, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentItem)
        }
    }

    class ViewHolderClass2(itemView: View):RecyclerView.ViewHolder(itemView){
        val rvImage:ImageView = itemView.findViewById(R.id.image)
        val rvTitle:TextView= itemView.findViewById(R.id.title)
    }
}