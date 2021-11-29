package com.example.myshopapp.activities

import android.os.Bundle
import android.widget.EditText
import com.example.myshopapp.R
import com.example.myshopapp.models.User

class ProfileActivity : BaseActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobileNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupActionBar()

        etFirstName = findViewById<EditText>(R.id.et_first_name)
        etLastName = findViewById<EditText>(R.id.et_last_name)
        etEmail = findViewById<EditText>(R.id.et_email)
        etMobileNumber = findViewById<EditText>(R.id.et_mobile_number)

        // Get intent extra from login activity
        var user = User()
        if (intent.hasExtra("user_detail")) {
            user = intent.getParcelableExtra<User>("user_detail")!!
        }

        // Set edit text value from intent extra
        etFirstName.setText(user.firstName)
        etFirstName.isEnabled = false

        etLastName.setText(user.lastName)
        etLastName.isEnabled = false

        etEmail.setText(user.email)
        etEmail.isEnabled = false
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_profile)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}