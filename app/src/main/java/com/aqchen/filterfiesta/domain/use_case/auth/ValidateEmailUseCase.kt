package com.aqchen.filterfiesta.domain.use_case.auth

import android.text.TextUtils
import android.util.Patterns
import com.aqchen.filterfiesta.domain.models.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
      operator fun invoke(email: String): ValidationResult {
          return if (TextUtils.isEmpty(email)) {
              ValidationResult(
                  successful = false,
                  errorMessage = "The email can't be blank",
              )
          } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
              ValidationResult(
                  successful = false,
                  errorMessage = "Invalid email format"
              )
          } else {
              ValidationResult(
                  successful = true,
              )
          }
    }
}