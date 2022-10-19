package com.upiyptk.makansehat

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailActivity: AppCompatActivity() {
    private lateinit var ref: DatabaseReference
    private lateinit var foodImage: ImageView
    private lateinit var foodTitle: TextView
    private lateinit var foodDescription: TextView
    private lateinit var foodWeight: TextView
    private lateinit var foodCarbohidrat: TextView
    private var weight: Double = 0.0
    private var carbohidrat: Double = 0.0
    private var position: Int = 0
    private var image: Int = 0
    private var m: String = "Error"

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val description = arrayOf(
            "Beras, terigu, roti manis",
            "Tepung tapioka, singkong, kentang",
            "Tahu, kacang tanah, tempe",
            "Daun singkong, tomat merah, nangka muda",
            "Pisang kepok, jeruk manis, pisang ambon",
            "Bakso sapi, daging ayam, daging sapi",
            "Tongkol, bandeng, lele",
            "Telur ayam, telur bebek, telur puyuh",
            "Susu kental manis, UHT coklat cair, UHT vanila cair"
        )

        val title = intent.getStringExtra("extra_title")
        when(title) {
            "Serealia" -> {
                position = 0
                m = "m1"
                image = R.drawable.image_serealia
            }
            "Umbi" -> {
                position = 1
                m = "m2"
                image = R.drawable.image_umbi
            }
            "Kacang" -> {
                position = 2
                m = "m3"
                image = R.drawable.image_serealia
            }
            "Sayur" -> {
                position = 3
                m = "m4"
                image = R.drawable.image_serealia
            }
            "Buah" -> {
                position = 4
                m = "m5"
                image = R.drawable.image_buah
            }
            "Daging" -> {
                position = 5
                m = "m6"
                image = R.drawable.image_serealia
            }
            "Ikan" -> {
                position = 6
                m = "m7"
                image = R.drawable.image_serealia
            }
            "Telur" -> {
                position = 7
                m = "m8"
                image = R.drawable.image_serealia
            }
            "Susu" -> {
                position = 8
                m = "m9"
                image = R.drawable.image_serealia
            }
            else -> {
                position = 9
                m = "Error"
                image = R.drawable.food_error
            }
        }

        foodTitle = findViewById(R.id.tv_foodTitle)
        foodTitle.text = title
        foodImage = findViewById(R.id.iv_foodImage)
        foodImage.setImageResource(image)
        foodDescription = findViewById(R.id.tv_foodDescription)
        foodDescription.text = description[position]

        ref = FirebaseDatabase.getInstance().reference
        ref.child("makanan").child(m).child("berat")
            .get().addOnSuccessListener {
                val weightString = it.value.toString()
                weight = weightString.toDouble()
                foodWeight = findViewById(R.id.tv_foodWeight)
                foodWeight.text = weight.toString() + " gram"
            } .addOnFailureListener {
                Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_LONG).show()
            }

        ref.child("makanan").child(m).child("karbohidrat")
            .get().addOnSuccessListener {
                val carbohidratString = it.value.toString()
                carbohidrat = carbohidratString.toDouble()
                foodCarbohidrat = findViewById(R.id.tv_foodCarbohidrat)
                foodCarbohidrat.text = carbohidrat.toString() + " kkal"
            } .addOnFailureListener {
                Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_LONG).show()
            }
    }
}