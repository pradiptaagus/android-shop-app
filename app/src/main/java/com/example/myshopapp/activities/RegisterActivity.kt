package com.example.myshopapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.example.myshopapp.R
import com.example.myshopapp.firebase.FireStore
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.example.myshopapp.firebase.Auth
import com.example.myshopapp.models.User
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class RegisterActivity : BaseActivity() {

    private lateinit var auth: Auth
    private lateinit var fireStore: FireStore
    private lateinit var etFirstName: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etPasswordConfirmation: TextInputEditText
    private lateinit var cbAccTermAgreement: MaterialCheckBox
    private lateinit var tvLogin: TextView
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupActionBar()

        // Initialize firebase
        auth = Auth()
        fireStore = FireStore()

        // Get view references
        etFirstName = findViewById<TextInputEditText>(R.id.et_first_name)
        etLastName = findViewById<TextInputEditText>(R.id.et_last_name)
        etEmail = findViewById<TextInputEditText>(R.id.et_email)
        etPassword = findViewById<TextInputEditText>(R.id.et_password)
        etPasswordConfirmation = findViewById<TextInputEditText>(R.id.et_confirm_password)
        cbAccTermAgreement = findViewById<MaterialCheckBox>(R.id.cb_term_agreement)
        tvLogin = findViewById<TextView>(R.id.tv_login)
        btnRegister = findViewById<Button>(R.id.btn_register)

        tvLogin.setOnClickListener {
            onBackPressed()
        }

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        val isValid = validate()

        if (isValid) {

            showProgressDialog()

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()


            auth.registerUser(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                           firebaseUser.uid,
                            etFirstName.text.toString().trim(' '),
                            etLastName.text.toString().trim(' '),
                            etEmail.text.toString().trim(' ')
                        )

                        fireStore.registerUser(user)
                            .addOnSuccessListener {
                                completeRegistration()
                            }
                            .addOnFailureListener { e ->
                                failedRegistration(e)
                            }
                    } else {
                        failedRegistration(task.exception!!)
                    }
                }
                .addOnFailureListener { e ->
                    failedRegistration(e)
                }
        }
    }

    private fun completeRegistration() {
        hideProgressDialog()
        showSnackBar("You are register successfully.")
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 2000)
    }

    private fun failedRegistration(e: Exception) {
        hideProgressDialog()
        showSnackBar("Error: ${e.message.toString()}", true)
    }

    private fun validate(): Boolean {
        if (etFirstName.text.toString().trim(' ') == "") {
            showSnackBar("First name is required.", true)
            return false
        } else if (etLastName.text.toString().trim(' ') == "") {
            showSnackBar("Last name is required.", true)
            return false
        } else if (etEmail.text.toString().trim(' ') == "") {
            showSnackBar("Email is required.", true)
            return false
        } else if (etPassword.text.toString().trim(' ') == "") {
            showSnackBar("Password is required.", true)
            return false
        } else if (etPasswordConfirmation.text.toString().trim(' ') == "") {
            showSnackBar("Password confirmation is required.", true)
            return false
        } else if (etPassword.text.toString() != etPasswordConfirmation.text.toString()) {
            showSnackBar("Password confirmation not same with password.", true)
            return false
        } else if (!cbAccTermAgreement.isChecked) {
            showSnackBar("Please accept term & agreement.", true)
            return false
        } else {
            return true
        }
    }
}