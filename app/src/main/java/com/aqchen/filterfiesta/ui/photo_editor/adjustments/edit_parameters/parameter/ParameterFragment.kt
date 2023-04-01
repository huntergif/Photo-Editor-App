package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.parameter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.EditParametersViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.SelectFilterState
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ParameterFragment: Fragment() {

    companion object {
        fun newInstance() = ParameterFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel
    private lateinit var editParametersViewModel: EditParametersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_parameter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slider: Slider = view.findViewById(R.id.edit_parameter_slider)

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
            editParametersViewModel = ViewModelProvider(requireActivity())[EditParametersViewModel::class.java]

            val selectedAdjustmentState = photoEditorImagesViewModel.selectedFilterStateFlow.value
            val parameterSettings = editParametersViewModel.selectedParameterSettingStateFlow.value

            if (selectedAdjustmentState == null) {
                Snackbar.make(view, "No adjustment selected", Snackbar.LENGTH_LONG).show()
            } else if (parameterSettings == null) {
                Snackbar.make(view, "No parameter selected", Snackbar.LENGTH_LONG).show()
            } else {
                slider.valueFrom = parameterSettings.min.toFloat()
                slider.valueTo = parameterSettings.max.toFloat()
//                slider.value =
//                    (photoEditorImagesViewModel.selectedFilterStateFlow.value?.filterValue?.parameters?.get(parameterSettings.type)?.toFloat())
//                        ?: parameterSettings.default.toFloat()
                slider.value =
                    (photoEditorImagesViewModel.previewFilterStateFlow.value?.parameters?.get(parameterSettings.type)?.toFloat())
                        ?: parameterSettings.default.toFloat()
            }

            slider.addOnChangeListener { _, value, _ ->
                var filterList = photoEditorImagesViewModel.imageFiltersStateFlow.value
                val previewFilter = photoEditorImagesViewModel.previewFilterStateFlow.value
                if (selectedAdjustmentState != null && parameterSettings != null && previewFilter != null) {
                    filterList = filterList + selectedAdjustmentState.filterValue
                    val mutableParams = previewFilter.parameters.toMutableMap()
                    mutableParams[parameterSettings.type] = value.toDouble()
                    Log.d("ParameterFragment", mutableParams[parameterSettings.type].toString())
                    //photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SelectFilter(filterValue = selectedAdjustmentState.filterValue.copy(parameters = mutableParams.toMap()), filterListPosition = selectedAdjustmentState.filterListPosition))
                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetPreviewFilter(previewFilter.copy(parameters = mutableParams.toMap())))
                }
            }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoEditorImagesViewModel.previewFilterStateFlow.collect {
                    if (it != null) {
                        photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.ShowPreview)
//                        slider.value =
//                            (photoEditorImagesViewModel.previewFilterStateFlow.value?.parameters?.get(parameterSettings.type)?.toFloat())
//                                ?: parameterSettings.default.toFloat()
                    }
                }
            }
        }
    }
}
