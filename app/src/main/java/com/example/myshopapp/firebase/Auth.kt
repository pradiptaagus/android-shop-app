package com.example.myshopapp.firebase

import android.content.ContentValues
import android.util.Log
import com.example.myshopapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class Auth {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(
        email: String,
        password: String
    ): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun getCurrentUserId(): String {
        val currentUser = auth.currentUser
        var currentUserId = ""

        if (currentUser != null) {
            currentUserId = currentUser.uid
        }

        return currentUserId
    }
}