package com.example.nutrimamadaily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class UnhealthyFoodAdapter(
    private val foodList: List<FoodItem>,
    private val onItemChecked: (FoodItem, Boolean) -> Unit
) : RecyclerView.Adapter<UnhealthyFoodAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<FoodItem>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.checkBox.text = foodItem.nama
        holder.checkBox.isChecked = selectedItems.contains(foodItem)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(foodItem)
            } else {
                selectedItems.remove(foodItem)
            }
            onItemChecked(foodItem, isChecked)
        }
    }

    override fun getItemCount() = foodList.size
}
