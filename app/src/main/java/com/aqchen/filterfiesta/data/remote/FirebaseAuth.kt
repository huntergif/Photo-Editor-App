package com.aqchen.filterfiesta.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthentication {
    val conn: FirebaseAuth = Firebase.auth
}
