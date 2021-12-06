package com.example.myshopapp.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.provider.MediaStore
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshopapp.R
import com.example.myshopapp.utils.Constant.PICK_IMAGE_REQUEST_CODE
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog

    fun showSnackBar(message: String, isError: Boolean = false) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (isError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.error)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.success)
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String = resources.getString(R.string.please_wait)) {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        val tvProgressBarText = progressDialog.findViewById<TextView>(R.id.tv_progress_bar_text)
        tvProgressBarText.text = text
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}