package com.aqchen.filterfiesta.domain.use_case.custom_filters

import android.text.TextUtils
import android.util.Patterns
import com.aqchen.filterfiesta.domain.models.ValidationResult
import javax.inject.Inject

class ValidateCustomFilterNameUseCase @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        return if (TextUtils.isEmpty(email)) {
            ValidationResult(
                successful = false,
                errorMessage = "The name can't be blank",
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
