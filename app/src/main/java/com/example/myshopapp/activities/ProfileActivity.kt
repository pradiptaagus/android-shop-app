package com.example.myshopapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshopapp.R
import com.example.myshopapp.firebase.FireStore
import com.example.myshopapp.models.User
import com.example.myshopapp.utils.Constant.FEMALE
import com.example.myshopapp.utils.Constant.GENDER
import com.example.myshopapp.utils.Constant.MALE
import com.example.myshopapp.utils.Constant.MOBILE
import com.example.myshopapp.utils.Constant.PICK_IMAGE_REQUEST_CODE
import com.example.myshopapp.utils.Constant.READ_EXTERNAL_STORAGE_PERMISSION_CODE
import com.example.myshopapp.utils.GlideLoader
import java.io.IOException
import java.lang.Exception

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var ivUserPhoto: ImageView
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var btnSubmit: Button

    private lateinit var mUserDetail: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupActionBar()

        ivUserPhoto = findViewById<ImageView>(R.id.iv_user_photo)
        etFirstName = findViewById<EditText>(R.id.et_first_name)
        etLastName = findViewById<EditText>(R.id.et_last_name)
        etEmail = findViewById<EditText>(R.id.et_email)
        etMobileNumber = findViewById<EditText>(R.id.et_mobile_number)
        rbMale = findViewById<RadioButton>(R.id.rb_male)
        rbFemale = findViewById<RadioButton>(R.id.rb_female)
        btnSubmit = findViewById<Button>(R.id.btn_submit)

        // Get intent extra from login activity
        if (intent.hasExtra("user_detail")) {
            mUserDetail = intent.getParcelableExtra<User>("user_detail")!!
        }

        // Set edit text value from intent extra
        etFirstName.setText(mUserDetail.firstName)
        etFirstName.isEnabled = false

        etLastName.setText(mUserDetail.lastName)
        etLastName.isEnabled = false

        etEmail.setText(mUserDetail.email)
        etEmail.isEnabled = false

        // Bind onClickListener
        ivUserPhoto.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo ->
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_CODE)
                    } else {
                        showImageChooser(this)
                    }
                R.id.btn_submit ->
                    submit()
            }
        }
    }

    private fun submit() {
        val isValid = validate()

        if (isValid) {
            val userHashMap = HashMap<String, Any>()

            val mobileNumber = etMobileNumber.text.toString().trim(' ')
            val gender = if (rbMale.isChecked) {
                MALE
            } else {
                FEMALE
            }

            if (mobileNumber.isNotEmpty()) {
                userHashMap[MOBILE] = mobileNumber
            }

            userHashMap[GENDER] = gender

            showProgressDialog()

            FireStore().updateUser(userHashMap).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    hideProgressDialog()
                    showSnackBar("Your profile was updated successfully")

                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                    finish()
                } else {
                    failedRegistration(task.exception!!)
                }
            }.addOnFailureListener {e ->
                failedRegistration(e)
            }
        }
    }

    private fun failedRegistration(e: Exception) {
        hideProgressDialog()
        showSnackBar("Error: ${e.message.toString()}", true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this)
            } else {
                Toast.makeText(this, "Oops, you just denied the permission for storage. You can also allow it from setting.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(selectedImageFileUri, ivUserPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to pick image!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        return when {
            etMobileNumber.text.toString().trim(' ') == "" -> {
                showSnackBar("Mobile number is required!", true)
                false
            }
            else -> {
                true
            }
        }
    }
}