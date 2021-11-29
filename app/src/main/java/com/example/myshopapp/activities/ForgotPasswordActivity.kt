package com.example.myshopapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.example.myshopapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()

        // Initialize firebase Auth
        auth = FirebaseAuth.getInstance()

        etEmail = findViewById<TextInputEditText>(R.id.et_email)
        btnResetPassword = findViewById<Button>(R.id.btn_reset_password)

        btnResetPassword.setOnClickListener {
            submit()
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_forgot_password)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun submit() {
        val isValid = validate()

        if (isValid) {
            showProgressDialog()

            val email = etEmail.text.toString().trim(' ')

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {task ->
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        showSnackBar("The email was sent. Please check your inbox.")
                        Handler(Looper.getMainLooper()).postDelayed({finish()}, 1500)
                    } else {
                        showSnackBar("Failed to send email. Error: ${task.exception?.message.toString()}", true)
                    }
                }
        }
    }

    private fun validate(): Boolean {
        return when {
            TextUtils.isEmpty(etEmail.text.toString().trim(' ')) -> {
                showSnackBar("Email is required.", true)
                false
            }
            else -> {
                return true
            }
        }
    }
}