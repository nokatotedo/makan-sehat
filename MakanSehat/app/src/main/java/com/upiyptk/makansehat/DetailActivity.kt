package com.upiyptk.makansehat

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.math.BigDecimal
import java.math.RoundingMode

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
                image = R.drawable.image_kacang
            }
            "Sayur" -> {
                position = 3
                m = "m4"
                image = R.drawable.image_sayur
            }
            "Buah" -> {
                position = 4
                m = "m5"
                image = R.drawable.image_buah
            }
            "Daging" -> {
                position = 5
                m = "m6"
                image = R.drawable.image_daging
            }
            "Ikan" -> {
                position = 6
                m = "m7"
                image = R.drawable.image_ikan
            }
            "Telur" -> {
                position = 7
                m = "m8"
                image = R.drawable.image_telur
            }
            "Susu" -> {
                position = 8
                m = "m9"
                image = R.drawable.image_susu
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
                .setValue(weight).addOnSuccessListener {
                    Toast.makeText(this@DetailActivity, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                } .addOnFailureListener {
                    Toast.makeText(this@DetailActivity, "Gagal disimpan", Toast.LENGTH_SHORT).show()
                }

            ref.child("makanan").child(m).child("karbohidrat")
                .setValue(carbohidrat).addOnSuccessListener {
                    Toast.makeText(this@DetailActivity, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                } .addOnFailureListener {
                    Toast.makeText(this@DetailActivity, "Gagal disimpan", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun total() {
        val recipe: Double = when(title) {
            "Serealia" -> weight*0.70
            "Umbi" -> weight*0.26
            "Kacang" -> weight*0.09
            "Sayur" -> weight*0.06
            "Buah" -> weight*0.21
            "Daging" -> weight*0.02
            "Ikan" -> 0.00
            "Telur" -> 0.00
            "Susu" -> weight*0.13
            else -> 0.0
        }

        val totalWeight = BigDecimal(weight).setScale(2, RoundingMode.HALF_EVEN)
        val totalCarbohidrat = BigDecimal(recipe).setScale(2, RoundingMode.HALF_EVEN)
        weight = totalWeight.toDouble()
        carbohidrat = totalCarbohidrat.toDouble()

        foodWeight.text = "$totalWeight gram"
        foodCarbohidrat.text = "$totalCarbohidrat kkal"
    }
}