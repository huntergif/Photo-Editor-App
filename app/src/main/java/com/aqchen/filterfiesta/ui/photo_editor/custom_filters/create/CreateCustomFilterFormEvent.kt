package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.create

sealed class CreateCustomFilterFormEvent {
    data class NameChanged(val name: String): CreateCustomFilterFormEvent()
    object Submit: CreateCustomFilterFormEvent()
}