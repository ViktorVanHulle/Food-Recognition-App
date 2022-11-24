package com.example.photorecognitionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photorecognitionapp.firestore.MealItem

class DashboardAdapter(var list: ArrayList<MealItem>) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.setText(list.get(position).name)
        var text_cal = list.get(position).calories?.times(list.get(position).intakeQuan!!)?.toInt()
        holder.cal.setText(text_cal.toString() + " kcal")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.food_name)
        var cal = itemView.findViewById<TextView>(R.id.food_cal)
    }

}
