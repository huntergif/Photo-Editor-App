package com.aqchen.filterfiesta.domain.use_case.tool_pages

import com.aqchen.filterfiesta.domain.models.ToolPage
import javax.inject.Inject

class GetToolPagesUseCase @Inject constructor() {

    operator fun invoke(): List<ToolPage> {
        return listOf(
            ToolPage.CustomFilters,
            ToolPage.Adjustments,
            ToolPage.PresetFilters,
        )
    }
}
