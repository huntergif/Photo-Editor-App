package com.aqchen.filterfiesta.domain.use_case.filter_groups

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.util.Resource
import javax.inject.Inject

class CreateFilterGroupUseCase @Inject constructor(
    private val filterGroupRepository: FilterGroupRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(filterGroup: FilterGroup): Resource<Unit> {
        val user = GetCurrentUserUseCase(authRepository).invoke()
            ?: return Resource.Error("user cannot be null")
        // filterGroup id *should* be null (for Firestore to auto-gen id), but could be not null in certain situations
        return filterGroupRepository.createUserFilterGroup(user.uid, filterGroup)
    }
}
