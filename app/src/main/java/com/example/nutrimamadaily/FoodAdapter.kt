package com.example.nutrimamadaily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val foodList: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<FoodItem>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.checkBox.text = "${foodItem.nama} (Zat Besi: ${foodItem.zatBesi})"
        holder.checkBox.isChecked = selectedItems.contains(foodItem)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(foodItem)
            } else {
                selectedItems.remove(foodItem)
            }
        }
    }



    override fun getItemCount(): Int = foodList.size

    fun getSelectedItems(): List<FoodItem> = selectedItems.toList()
}