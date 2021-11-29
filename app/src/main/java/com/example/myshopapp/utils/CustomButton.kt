package com.example.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class CustomButton(context: Context, attributes: AttributeSet): androidx.appcompat.widget.AppCompatButton(context, attributes) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}