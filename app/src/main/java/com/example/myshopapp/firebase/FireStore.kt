package com.example.myshopapp.firebase

import com.example.myshopapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore(private val USER: String = "users") {

    private val auth: Auth = Auth()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerUser(
        user: User
    ): Task<Void> {
        return fireStore.collection(USER).document(user.id)
            .set(user, SetOptions.merge())
    }

    fun getUserDetail(): Task<DocumentSnapshot> {
        return fireStore.collection(USER).document(auth.getCurrentUserId()).get()
    }

    fun updateUser(data: HashMap<String, Any>): Task<Void> {
        return fireStore.collection(USER).document(auth.getCurrentUserId()).update(data)
    }
}