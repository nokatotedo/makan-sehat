package com.upiyptk.makansehat

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.math.RoundingMode
import java.text.DecimalFormat

class DetailActivity: AppCompatActivity() {
    private lateinit var ref: DatabaseReference
    private lateinit var buttonMinus: ImageView
    private lateinit var buttonPlus: ImageView
    private lateinit var foodImage: ImageView
    private lateinit var foodTitle: TextView
    private lateinit var foodDescription: TextView
    private lateinit var foodWeight: TextView
    private lateinit var foodCarbohidrat: TextView
    private lateinit var buttonSave: Button
    private var weight: Double = 0.0
    private var carbohidrat: Double = 0.0
    private var position: Int = 0
    private var image: Int = 0
    private var m: String = "Error"
    private var title: String = "Error"

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

        title = intent.getStringExtra("extra_title")!!
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
                foodWeight.text = "$weight gram"
            } .addOnFailureListener {
                Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_LONG).show()
            }

        ref.child("makanan").child(m).child("karbohidrat")
            .get().addOnSuccessListener {
                val carbohidratString = it.value.toString()
                carbohidrat = carbohidratString.toDouble()
                foodCarbohidrat = findViewById(R.id.tv_foodCarbohidrat)
                foodCarbohidrat.text = "$carbohidrat kkal"
            } .addOnFailureListener {
                Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_LONG).show()
            }

        buttonPlus = findViewById(R.id.biv_plus)
        buttonPlus.setOnClickListener {
            weight += 0.01
            total()
        }

        buttonMinus = findViewById(R.id.biv_minus)
        buttonMinus.setOnClickListener {
            weight -= 0.01
            total()
        }

        buttonSave = findViewById(R.id.bv_save)
        buttonSave.setOnClickListener {
            ref.child("makanan").child(m).child("berat")
                .setValue(weight)
            ref.child("makanan").child(m).child("karbohidrat")
                .setValue(carbohidrat)
        }
    }

    private fun total() {
        val recipe: Double = when(title) {
            "Serealia" -> weight*(20.2/30.0)
            "Umbi" -> weight*(11.7/19.5)
            "Kacang" -> weight*(2.0/27.5)
            "Sayur" -> weight*(0.6/12.0)
            "Buah" -> weight*(12.7/66.0)
            "Daging" -> 0.0
            "Ikan" -> weight*(0.9/67.5)
            "Telur" -> weight*(0.1/15.0)
            "Susu" -> weight*(5.5/15.0)
            else -> 0.0
        }
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        val totalWeight = df.format(weight)
        val totalCarbohidrat = df.format(recipe)
        weight = totalWeight.toDouble()
        carbohidrat = totalCarbohidrat.toDouble()

        foodWeight.text = "$totalWeight gram"
        foodCarbohidrat.text = "$totalCarbohidrat kkal"
    }
}