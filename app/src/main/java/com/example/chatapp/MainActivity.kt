package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        submit.setOnClickListener {
            val name = nameField.text.toString()
            if (name.isEmpty()) {
                nameField.error = "Please enter your name"
            }
            else{
                nameField.error = null
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("NAME", name)
                Log.d("TAG", "onStart: Name: $name")
                startActivity(intent)
            }
        }
    }
}