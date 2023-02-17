package com.aqchen.filterfiesta.domain.util

import android.text.TextUtils

class ValidationUtil {
    fun validateEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
