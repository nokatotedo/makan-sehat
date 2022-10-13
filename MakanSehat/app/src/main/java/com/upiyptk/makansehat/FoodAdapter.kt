package com.upiyptk.makansehat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoodAdapter(private val list: ArrayList<FoodData>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    inner class FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodTitle: TextView = itemView.findViewById(R.id.tv_foodTitle)
        val foodWeight: TextView = itemView.findViewById(R.id.tv_foodWeight)
        val foodCarbohidrat: TextView = itemView.findViewById(R.id.tv_foodCarbohidrat)
        val foodImage: ImageView = itemView.findViewById(R.id.iv_foodImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_recent, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val positionList = list[position]

        val foodTitle = positionList.jenis
        val foodWeight = positionList.berat
        val foodCarbohidrat = positionList.karbohidrat
        val foodImage = when(foodTitle) {
            "Buah" -> R.drawable.food_buah
            "Daging" -> R.drawable.food_daging
            "Ikan" -> R.drawable.food_ikan
            "Kacang" -> R.drawable.food_kacang
            "Minuman Olahan" -> R.drawable.food_minuman
            "Sayur" -> R.drawable.food_sayur
            "Serealia" -> R.drawable.food_serealia
            "Susu" -> R.drawable.food_susu
            "Telur" -> R.drawable.food_telur
            "Umbi" -> R.drawable.food_umbi
            else -> R.drawable.food_error
        }

        holder.foodTitle.text = foodTitle
        holder.foodWeight.text = foodWeight.toString()
        holder.foodCarbohidrat.text = foodCarbohidrat.toString()
        Glide.with(holder.itemView.context)
            .load(foodImage)
            .into(holder.foodImage)
    }

    override fun getItemCount(): Int = list.size
}