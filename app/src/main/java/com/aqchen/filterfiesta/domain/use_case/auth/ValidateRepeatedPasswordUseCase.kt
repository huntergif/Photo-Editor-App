package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.models.ValidationResult
import javax.inject.Inject

class ValidateRepeatedPasswordUseCase @Inject constructor() {
      operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
         return if (password != repeatedPassword) {
            ValidationResult(
                successful = false,
                errorMessage = "Passwords don't match"
            )
         } else {
             ValidationResult(
                 successful = true
             )
         }
    }
}