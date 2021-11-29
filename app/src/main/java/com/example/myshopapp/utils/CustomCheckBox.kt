package com.example.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.checkbox.MaterialCheckBox

class CustomCheckBox(context: Context, attributeSet: AttributeSet): MaterialCheckBox(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
        textSize = 16.0F
    }
}