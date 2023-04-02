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

            val selectedFilterState = photoEditorImagesViewModel.selectedFilterStateFlow.value
            val parameterSettings = editParametersViewModel.selectedParameterSettingStateFlow.value

            if (selectedFilterState == null) {
                Snackbar.make(view, "No adjustment selected", Snackbar.LENGTH_LONG).show()
            } else if (parameterSettings == null) {
                Snackbar.make(view, "No parameter selected", Snackbar.LENGTH_LONG).show()
            } else {
                slider.valueFrom = parameterSettings.min.toFloat()
                slider.valueTo = parameterSettings.max.toFloat()
                slider.value =
                    (selectedFilterState.filters[selectedFilterState.selectPosition].parameters[parameterSettings.type]
                        ?.toFloat())
                        ?: parameterSettings.default.toFloat()
            }

            slider.addOnChangeListener { _, value, _ ->
                if (selectedFilterState != null && parameterSettings != null) {
                    // since everything is immutable in the selected filter state, we need to copy the nested items we change
                    // get a mutable copy of the parameters map for the selected filter
                    val mutableParams = selectedFilterState.filters[selectedFilterState.selectPosition].parameters.toMutableMap()
                    // change the filter's parameter value to the new slider value
                    mutableParams[parameterSettings.type] = value.toDouble()
                    // create a copy of the filter at the selected position, with the parameter map
                    val newFilter = selectedFilterState.filters[selectedFilterState.selectPosition].copy(parameters = mutableParams.toMap())
                    // get a mutable copy of the filters list
                    val newList = selectedFilterState.filters.toMutableList()
                    // set the new filter in the filters list
                    newList[selectedFilterState.selectPosition] = newFilter
                    // notify the view model that the filter list has changed
                    photoEditorImagesViewModel.onEvent((PhotoEditorImagesEvent.SelectFilter(newList.toList(), selectedFilterState.selectPosition)))
                }
            }
        }
    }
}
