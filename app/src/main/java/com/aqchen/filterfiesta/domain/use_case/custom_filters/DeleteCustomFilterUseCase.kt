package com.aqchen.filterfiesta.domain.use_case.custom_filters

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.CustomFilterRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.util.Resource
import javax.inject.Inject

class DeleteCustomFilterUseCase @Inject constructor(
    private val customFilterRepository: CustomFilterRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(customFilter: CustomFilter): Resource<Unit> {
        val user = GetCurrentUserUseCase(authRepository).invoke()
            ?: return Resource.Error("user cannot be null")
        if (customFilter.id == null) {
            return Resource.Error("custom filter id cannot be null")
        }
        return customFilterRepository.deleteUserCustomFilter(user.uid, customFilter.id)
    }
}
