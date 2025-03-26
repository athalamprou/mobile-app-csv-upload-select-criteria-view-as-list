package com.example.projectpart1attempt1

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
        setContentView(R.layout.activity_main)

        //DHLWSH KOYMPIWN
        val BtnDisk = findViewById<Button>(R.id.ButtonDisk)
        val BtnURL = findViewById<Button>(R.id.ButtonURL)
        val buttonURL = findViewById<Button>(R.id.ButtonURL)

        //INTENTS KAI LISTENERS GIA TA KOYMPIA (ME TA ANTISTOIXA TOYS ACTIVITIES)
        BtnDisk.setOnClickListener{
            val intent1 = Intent(this, ReadfromDisk::class.java)
            startActivity(intent1)
        }

        BtnURL.setOnClickListener{
            val intent2 = Intent(this, ReadfromURL::class.java)
            startActivity(intent2)
        }

        buttonURL.setOnClickListener {

            val intent = Intent(this, ReadfromURL::class.java)
            startActivity(intent)
        }


    } //EDW KLEINEI onCreate
} //EDW KLEINEI class MainActivity