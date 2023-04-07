package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters.pager

sealed class ParameterPagerEvent {
    data class FocusParameterPageViewHolder(val position: Int): ParameterPagerEvent()
    object SelectFocusedParameterPage: ParameterPagerEvent()
}
