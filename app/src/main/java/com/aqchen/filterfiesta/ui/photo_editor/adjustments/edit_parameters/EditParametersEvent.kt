package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters

import com.aqchen.filterfiesta.domain.models.image.ParameterSetting

sealed class EditParametersEvent {
    data class SelectParameter(val parameterSetting: ParameterSetting): EditParametersEvent()
}
