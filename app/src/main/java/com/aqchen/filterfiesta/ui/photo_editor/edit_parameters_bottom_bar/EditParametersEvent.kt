package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar

import com.aqchen.filterfiesta.domain.models.image.ParameterSetting

sealed class EditParametersEvent {
    data class SelectParameter(val parameterSetting: ParameterSetting) : EditParametersEvent()
}
