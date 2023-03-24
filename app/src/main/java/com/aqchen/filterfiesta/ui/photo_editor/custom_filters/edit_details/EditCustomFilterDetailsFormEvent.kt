package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.edit_details

import com.aqchen.filterfiesta.domain.models.CustomFilter

sealed class EditCustomFilterDetailsFormEvent {
    data class CustomFilterChanged(val customFilter: CustomFilter): EditCustomFilterDetailsFormEvent()
    data class NameChanged(val name: String) : EditCustomFilterDetailsFormEvent()
    object Submit : EditCustomFilterDetailsFormEvent()
}
