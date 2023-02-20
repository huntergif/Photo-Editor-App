package com.aqchen.filterfiesta.domain.use_case.auth

import android.text.TextUtils
import android.util.Patterns
import com.aqchen.filterfiesta.domain.models.ValidationResult
import javax.inject.Inject

class ValidateTermsUseCase @Inject constructor() {
      operator fun invoke(accepted: Boolean): ValidationResult {
          return if (!accepted) {
              ValidationResult(
                  successful = false,
                  errorMessage = "Please accept the terms"
              )
          } else {
              ValidationResult(
                  successful = true,
              )
          }
    }
}
