package com.example.mobileappfinalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the plus button by its ID
        val addEventButton = findViewById<ImageButton>(R.id.addEventButton)

        // Set a click listener to open the CreateEventActivity
        addEventButton.setOnClickListener {
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }
    }
}
