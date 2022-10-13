package com.upiyptk.makansehat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity: AppCompatActivity() {
    private lateinit var ref: DatabaseReference
    private lateinit var rv: RecyclerView
    private var array: ArrayList<FoodData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rv_foodRecent)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        ref = FirebaseDatabase.getInstance().getReference("makanan")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                if(snapshot.exists()) {
                    for(food in snapshot.children) {
                        val foodValue = food.getValue(FoodData::class.java)
                        array.add(foodValue!!)
                    }
                    rv.adapter = FoodAdapter(array)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"Error", Toast.LENGTH_LONG).show()
            }
        })
    }
}