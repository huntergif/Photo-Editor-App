package com.aqchen.filterfiesta.domain.use_case.filter_groups

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.util.Resource
import javax.inject.Inject

class UpdateFilterGroupUseCase @Inject constructor(
    private val filterGroupRepository: FilterGroupRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(filterGroup: FilterGroup): Resource<Unit> {
        // get current user
        val user = GetCurrentUserUseCase(authRepository).invoke()
            ?: return Resource.Error("user cannot be null")
        if (filterGroup.id == null) {
            return Resource.Error("filter group id cannot be null")
        }
        return filterGroupRepository.updateUserFilterGroup(user.uid, filterGroup)
    }
}
