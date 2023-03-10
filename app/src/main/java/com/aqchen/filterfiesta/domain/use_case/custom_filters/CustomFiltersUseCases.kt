package com.aqchen.filterfiesta.domain.use_case.custom_filters

data class CustomFiltersUseCases constructor(
    val createFilterGroupUseCase: CreateCustomFilterUseCase,
    val deleteCustomFilterUseCase: DeleteCustomFilterUseCase,
    val getCustomFiltersUseCase: GetCustomFiltersUseCase,
    val updateFilterGroupUseCase: UpdateCustomFilterUseCase,
)
