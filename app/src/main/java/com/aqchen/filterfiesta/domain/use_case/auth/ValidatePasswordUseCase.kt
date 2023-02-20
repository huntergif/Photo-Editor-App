package com.aqchen.filterfiesta.domain.use_case.auth

import com.aqchen.filterfiesta.domain.models.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
      operator fun invoke(password: String, loginValidation: Boolean = false): ValidationResult {
          fun String.isLongEnough() = length >= 8
          fun String.hasDigit() = count(Char::isDigit) > 0
          fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)

          if (loginValidation) {
              if (password.isBlank()) {
                  return ValidationResult(
                      successful = false,
                      errorMessage = "The password can't be empty"
                  )
              }
          } else if (!password.isLongEnough()) {
              return ValidationResult(
                  successful = false,
                  errorMessage = "The password must be at least 8 characters long"
              )
          } else if (!password.hasDigit()) {
               return ValidationResult(
                  successful = false,
                  errorMessage = "The password must contain a digit"
              )
          } else if (!password.isMixedCase()) {
               return ValidationResult(
                  successful = false,
                  errorMessage = "The password must contain a lower and uppercase character"
              )
          }
          return ValidationResult(
              successful = true,
          )
    }
}
