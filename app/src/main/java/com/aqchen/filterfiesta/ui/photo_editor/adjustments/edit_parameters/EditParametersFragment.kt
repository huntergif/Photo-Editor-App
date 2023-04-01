package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager.ParameterPagerFragment
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditParametersFragment : Fragment() {

    companion object {
        fun newInstance() = EditParametersFragment()
    }

    private lateinit var viewModel: EditParametersViewModel
    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_bar_edit_parameters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adjustmentNameTextView: TextView = view.findViewById(R.id.edit_adjustment_name_text_view)
        val cancelButton: Button = view.findViewById(R.id.edit_parameters_cancel_button)

        childFragmentManager.beginTransaction().replace(R.id.edit_parameters_pager_container, ParameterPagerFragment()).commit()

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
            adjustmentNameTextView.text = photoEditorImagesViewModel.selectedFilterStateFlow.value?.filter?.name ?: "No adjustment selected"



            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    photoEditorImagesViewModel.filterPreviewFiltersStateFlow.collect {
                        // TODO: SHOULD SOMEHOW GENERATE NEW IMAGE
                        Snackbar.make(view, it.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
