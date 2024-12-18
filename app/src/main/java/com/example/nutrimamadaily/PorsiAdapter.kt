package com.example.nutrimamadaily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView

class PorsiAdapter(
    private val porsiList: List<String>,
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<PorsiAdapter.PorsiViewHolder>() {

    private var selectedPosition = -1

    inner class PorsiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButtonPorsi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PorsiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_porsi, parent, false)
        return PorsiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PorsiViewHolder, position: Int) {
        val porsi = porsiList[position]
        holder.radioButton.text = porsi
        holder.radioButton.isChecked = position == selectedPosition

        holder.radioButton.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemSelected(porsi)
        }
    }

    override fun getItemCount(): Int = porsiList.size
}
