package com.aqchen.filterfiesta.domain.use_case.custom_filters

import com.aqchen.filterfiesta.domain.models.CustomFilter
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.CustomFilterRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.domain.util.OrderType
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCustomFilterUseCase @Inject constructor(
    private val customFilterRepository: CustomFilterRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        customFilterId: String?
    ): Resource<Flow<CustomFilter?>> {
        // get current user
        val user = GetCurrentUserUseCase(authRepository).invoke()
            ?: return Resource.Error("user cannot be null")
        if (customFilterId == null) {
            return Resource.Error("custom filter id can't be null")
        }
        // get filter groups for user
        return customFilterRepository.getUserCustomFilter(user.uid, customFilterId)
    }
}
