package com.aqchen.filterfiesta.domain.use_case.filter_groups

data class FilterGroupsUseCases (
    val createFilterGroupUseCase: CreateFilterGroupUseCase,
    val deleteFilterGroupUseCase: DeleteFilterGroupUseCase,
    val getFilterGroupsUseCase: GetFilterGroupsUseCase,
    val updateFilterGroupUseCase: UpdateFilterGroupUseCase,
)
