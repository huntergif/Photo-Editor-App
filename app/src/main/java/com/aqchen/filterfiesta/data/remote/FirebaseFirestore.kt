package com.aqchen.filterfiesta.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseFirestore {
    val conn: FirebaseFirestore = Firebase.firestore
}
