package com.example.nutrimamadaily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class UnhealthyFoodAdapter(private val foodList: List<String>) : RecyclerView.Adapter<UnhealthyFoodAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_unhealthy_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodName = foodList[position]
        holder.checkBox.text = foodName
        holder.checkBox.isChecked = selectedItems.contains(foodName)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(foodName)
            } else {
                selectedItems.remove(foodName)
            }
        }
    }

    override fun getItemCount(): Int = foodList.size

    fun getSelectedItems(): List<String> = selectedItems.toList()
}