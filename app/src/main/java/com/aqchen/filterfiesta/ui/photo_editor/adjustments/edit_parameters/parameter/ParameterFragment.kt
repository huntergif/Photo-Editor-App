package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.parameter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.EditParametersViewModel
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager.ParameterPagerFragment
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager.ParameterPagerViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
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

        val nameTextView: TextView = view.findViewById(R.id.edit_parameter_name_text_view)
        val slider: Slider = view.findViewById(R.id.edit_parameter_slider)

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
            editParametersViewModel = ViewModelProvider(requireActivity())[EditParametersViewModel::class.java]

            val selectedAdjustmentState = photoEditorImagesViewModel.selectedAdjustmentStateFlow.value
            val parameterSettings = editParametersViewModel.selectedParameterSettingStateFlow.value

            if (selectedAdjustmentState == null) {
                Snackbar.make(view, "No adjustment selected", Snackbar.LENGTH_LONG).show()
            } else if (parameterSettings == null) {
                Snackbar.make(view, "No parameter selected", Snackbar.LENGTH_LONG).show()
            } else {
                nameTextView.text = parameterSettings.name
                slider.valueFrom = parameterSettings.min.toFloat()
                slider.valueTo = parameterSettings.max.toFloat()
                slider.value =
                    (photoEditorImagesViewModel.selectedAdjustmentStateFlow.value?.currentParams?.get(parameterSettings.type)?.toFloat())
                        ?: parameterSettings.default.toFloat()
            }
        }
    }
}
