package com.upiyptk.makansehat

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity: AppCompatActivity() {
    private lateinit var ref: DatabaseReference
    private lateinit var rv: RecyclerView
    private lateinit var switch: SwitchCompat
    private lateinit var progressInner: ProgressBar
    private lateinit var progressOuter: ProgressBar
    private lateinit var mode: TextView
    private lateinit var carbohidat: TextView
    private var array: ArrayList<FoodData> = arrayListOf()
    private var total: Double = 0.0
    private var percent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rv_foodRecent)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        mode = findViewById(R.id.tv_foodMode)
        mode.text = "Normal"

        progressInner = findViewById(R.id.pb_foodRecent_inner)
        progressOuter = findViewById(R.id.pb_foodRecent_outer)
        carbohidat = findViewById(R.id.tv_foodRecent_carbohidrat)
        progressInner.progress = 0
        progressOuter.progress = 0
        carbohidat.text = "0.0 kkal"

        ref = FirebaseDatabase.getInstance().getReference("makanan")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                total = 0.0
                array.clear()

                if(snapshot.exists()) {
                    for(food in snapshot.children) {
                        val foodValue = food.getValue(FoodData::class.java)
                        total += foodValue?.karbohidrat!!

                        array.add(foodValue)
                    }

                    percent = total.toInt()
                    setPercent()

                    val adapter = FoodAdapter(array)
                    rv.adapter = adapter
                    adapter.setOnItemClickCallback(object: FoodAdapter.OnItemClickCallback {
                        override fun onItemClicked(list: FoodData) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_TITLE, list.jenis)
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })

        switch = findViewById(R.id.sc_foodMode)
        switch.setOnCheckedChangeListener { _, _ ->
            when(switch.isChecked) {
                true -> {
                    mode.text = "Diet"
                    percent = (total/2).toInt()
                    setPercent()
                }
                false -> {
                    mode.text = "Normal"
                    percent = total.toInt()
                    setPercent()
                }
            }
        }
    }

    private fun setPercent() {
        val bd = BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN)

        progressInner.progress = percent
        progressOuter.progress = percent
        carbohidat.text = "$bd kkal"

        val animateInner = ObjectAnimator.ofInt(progressInner, "progress", 0, percent)
        animateInner.duration = 800
        animateInner.start()
        val animateOuter = ObjectAnimator.ofInt(progressOuter, "progress", 0, percent)
        animateOuter.duration = 800
        animateOuter.start()
    }
}