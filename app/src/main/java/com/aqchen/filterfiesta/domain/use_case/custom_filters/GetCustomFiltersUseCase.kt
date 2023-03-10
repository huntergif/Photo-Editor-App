package com.aqchen.filterfiesta.domain.use_case.filter_groups

import com.aqchen.filterfiesta.domain.models.FilterGroup
import com.aqchen.filterfiesta.domain.repository.AuthRepository
import com.aqchen.filterfiesta.domain.repository.FilterGroupRepository
import com.aqchen.filterfiesta.domain.use_case.auth.GetCurrentUserUseCase
import com.aqchen.filterfiesta.domain.util.FilterGroupsOrder
import com.aqchen.filterfiesta.domain.util.OrderType
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilterGroupsUseCase @Inject constructor(
    private val filterGroupRepository: FilterGroupRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        filterGroupsOrder: FilterGroupsOrder = FilterGroupsOrder.DateCreated(OrderType.Descending)
    ): Resource<Flow<List<FilterGroup>>> {
        // get current user
        val user = GetCurrentUserUseCase(authRepository).invoke()
            ?: return Resource.Error("user cannot be null")
        // get filter groups for user
        return when(val filterGroupsRes = filterGroupRepository.getUserFilterGroups(user.uid)) {
            // Handle success
            is Resource.Success -> {
                // Sort get result by given order
                filterGroupsRes.data.map { filterGroups ->
                    when (filterGroupsOrder.orderType) {
                        OrderType.Ascending -> {
                            when (filterGroupsOrder) {
                                is FilterGroupsOrder.DateCreated -> filterGroups.sortBy { it.dateCreated }
                                is FilterGroupsOrder.Name -> filterGroups.sortBy { it.name.lowercase() }
                            }
                        }
                        OrderType.Descending -> {
                            when (filterGroupsOrder) {
                                is FilterGroupsOrder.DateCreated -> filterGroups.sortByDescending { it.dateCreated }
                                is FilterGroupsOrder.Name -> filterGroups.sortByDescending { it.name.lowercase() }
                            }
                        }
                    }
                }
                return filterGroupsRes
            }
            // Return Resource.Error from repository
            else -> filterGroupsRes
        }
    }
}
