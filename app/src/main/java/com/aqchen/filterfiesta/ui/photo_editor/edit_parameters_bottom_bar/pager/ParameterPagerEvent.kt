package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar.pager

sealed class ParameterPagerEvent {
    data class FocusParameterPageViewHolder(val position: Int): ParameterPagerEvent()
    object SelectFocusedParameterPage: ParameterPagerEvent()
}
