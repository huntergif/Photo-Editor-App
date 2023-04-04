package com.aqchen.filterfiesta.domain.models

sealed class ToolPage(val pageName: String) {
    object CustomFilters: ToolPage("Custom Filters")
    object Adjustments: ToolPage("Adjustments")
    object PresetFilters: ToolPage("Preset Filters")
}
