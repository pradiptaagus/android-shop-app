package com.example.myshopapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myshopapp.activities.BaseActivity
import com.example.myshopapp.activities.MainActivity

class SharedPreferenceManager {
    private lateinit var sharedPref: SharedPreferences

    private val MY_SHOP_APP: String = "MY_SHOP_APP"

    fun setUserName(activity: BaseActivity, username: String) {
        this.sharedPref = activity.getSharedPreferences(MY_SHOP_APP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = this.sharedPref.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun getUsername(activity: BaseActivity): String {
        this.sharedPref = activity.getSharedPreferences(MY_SHOP_APP, Context.MODE_PRIVATE)
        return sharedPref.getString("username", "")!!
    }
}