package com.aqchen.filterfiesta.domain.use_case.custom_filters

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.models.ValidationResult

class ValidateCustomFilterUpdatableUseCase {
    operator fun invoke(customFilter: CustomFilter?): ValidationResult {
        if (customFilter == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Custom filter can't be null"
            )
        } else {
            if (customFilter.id == null) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Custom filter id can't be null"
                )
            }
        }
        return ValidationResult(successful = true)
    }
}
