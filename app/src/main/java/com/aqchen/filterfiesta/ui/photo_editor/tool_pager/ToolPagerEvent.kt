package com.aqchen.filterfiesta.ui.photo_editor.tool_pager

sealed class ToolPagerEvent {
    object LoadList: ToolPagerEvent()
    data class FocusToolPageViewHolder(val position: Int): ToolPagerEvent()
    object SelectFocusedToolPage: ToolPagerEvent()
}
