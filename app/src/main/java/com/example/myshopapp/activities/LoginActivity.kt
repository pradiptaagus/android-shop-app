package com.example.myshopapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.myshopapp.R
import com.example.myshopapp.firebase.Auth
import com.example.myshopapp.firebase.FireStore
import com.example.myshopapp.models.User
import com.example.myshopapp.utils.SharedPreferenceManager
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: Auth
    private lateinit var fireStore: FireStore
    private lateinit var tvRegister: TextView
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize firebase
        auth = Auth()
        fireStore = FireStore()

        tvRegister = findViewById<TextView>(R.id.tv_register)
        btnLogin = findViewById<Button>(R.id.btn_login)
        tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)
        etEmail = findViewById<TextInputEditText>(R.id.et_email)
        etPassword = findViewById<TextInputEditText>(R.id.et_password)

        tvForgotPassword.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        tvRegister.setOnClickListener(this)
    }

    private fun authenticateUser() {
        val isValid = validate()

        if (isValid) {
            showProgressDialog()

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            auth.login(email, password)
                .addOnCompleteListener { task ->
                    completeAuthCallback(task)
                }
        }
    }

    private fun completeAuthCallback(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            fireStore.getUserDetail()
                .addOnSuccessListener(fun(task) {
                    hideProgressDialog()

                    val user = task.toObject(User::class.java)
                    if (user != null) {
                        Log.i("First Name", user.firstName)
                        Log.i("Last Name", user.lastName)
                        Log.i("Email", user.email)

                        SharedPreferenceManager().setUserName(this@LoginActivity, "${user.firstName} ${user.lastName}")
                    }

                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra("user_detail", user)
                    startActivity(intent)
                    finish()
                })
        } else {
            hideProgressDialog()
            showSnackBar("Failed to log in! Error: ${task.exception!!.message.toString()}", true)
        }
    }

    private fun validate(): Boolean {
        return when {
            findViewById<TextInputEditText>(R.id.et_email).text.toString().trim(' ') == "" -> {
                showSnackBar("Email is required.", true)
                false
            }
            findViewById<TextInputEditText>(R.id.et_password).text.toString().trim(' ') == "" -> {
                showSnackBar("Password is required.", true)
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    authenticateUser()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}