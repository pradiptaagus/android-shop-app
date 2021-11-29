package com.example.myshopapp.activities

import android.os.Bundle
import android.widget.TextView
import com.example.myshopapp.R
import com.example.myshopapp.utils.SharedPreferenceManager

class MainActivity : BaseActivity() {

    private lateinit var tvHello: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello = findViewById<TextView>(R.id.tv_hello)

        val username: String = SharedPreferenceManager().getUsername(this@MainActivity)
        tvHello.text = username
    }
}