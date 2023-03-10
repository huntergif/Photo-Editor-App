package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.edit_details

import com.aqchen.filterfiesta.domain.models.CustomFilter

data class EditCustomFilterDetailsFormState(
    val customFilter: CustomFilter? = null,
    val customFilterError: String? = null,
    val name: String = "",
    val nameError: String? = null,
)
