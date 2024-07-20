package com.maseria.diary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnContact = findViewById<Button>(R.id.btnContact)
        btnContact.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        val btnCourses = findViewById<Button>(R.id.btnCourses)
        btnCourses.setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }

        val btnEvents = findViewById<Button>(R.id.btnEvents)
        btnEvents.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }

        val btnNews = findViewById<Button>(R.id.btnNews)
        btnNews.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }
    }
}