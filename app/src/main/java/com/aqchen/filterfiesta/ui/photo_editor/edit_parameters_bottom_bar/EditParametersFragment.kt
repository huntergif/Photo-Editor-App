package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar.pager.ParameterPagerFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.BitmapType
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditParametersFragment : Fragment() {

    companion object {
        fun newInstance() = EditParametersFragment()
    }

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
        val saveButton: Button = view.findViewById(R.id.edit_parameters_save_button)

        childFragmentManager.beginTransaction().replace(R.id.edit_parameters_pager_container, ParameterPagerFragment()).commit()

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
            adjustmentNameTextView.text = photoEditorImagesViewModel.selectedFilterStateFlow.value?.filter?.name ?: "No adjustment selected"

            saveButton.setOnClickListener {
                val bitmapResource = photoEditorImagesViewModel.filterPreviewBitmapStateFlow.value
                // val selectedState = photoEditorImagesViewModel.selectedFilterStateFlow.value // selected state contains to "new" filter list
                val newFilters = photoEditorImagesViewModel.filterPreviewFiltersStateFlow.value

                if (bitmapResource is Resource.Success) {
                    // set the image filters to the selected state (temp) filters
                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetImageFilters(newFilters))
                    // set the preview image as the bitmap from the generated for the filter preview
                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetInternalBitmap(Resource.Success(bitmapResource.data), BitmapType.PREVIEW_IMAGE))
                    parentFragmentManager.popBackStack()
                } else {
                    Snackbar.make(view, "Can't save new filter", Snackbar.LENGTH_LONG).show()
                }
            }

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    // when the selected filter state changes (namely the filter list updates from parameters changing), we want to generate a new bitmap using the updated filter list
                    photoEditorImagesViewModel.filterPreviewFiltersStateFlow.collectLatest {
//                        if (it?.filters != null) {
//                            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.GenerateBitmapUsingFilters(it.filters, BitmapType.FILTER_PREVIEW))
//                        }
                        photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.GenerateBitmapUsingFilters(it, BitmapType.FILTER_PREVIEW))
                    }
                }

                launch {
                    // collect results of the filter preview bitmap generation
                    photoEditorImagesViewModel.filterPreviewBitmapStateFlow.collect {
                        Log.d("DisplayBitmap", "COLLECTED PREVIEW BITMAP ${it.toString()}")
                        when (it) {
                            is Resource.Error -> {
                                Snackbar.make(view, "Failed to generate filter preview image", Snackbar.LENGTH_LONG).show()
                                saveButton.isEnabled = false
                            }
                            Resource.Loading -> {
                                saveButton.isEnabled = false
                                // notify the image preview fragment that the bitmap to be displayed is currently loading
                                photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(Resource.Loading))
                            }
                            is Resource.Success -> {
                                saveButton.isEnabled = true
                                // notify the image preview fragment that the new bitmap should be displayed
                                photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(Resource.Success(it.data)))
                            }
                            null -> {}
                        }
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
//            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetFilterPreviewFilters(
//                emptyList()))
            when (val previewImageBitmap = photoEditorImagesViewModel.previewImageBitmapStateFlow.value) {
                is Resource.Success -> {
                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetInternalBitmap(Resource.Success(previewImageBitmap.data), BitmapType.FILTER_PREVIEW))
                }
                else -> {
                    Snackbar.make(view, "Can't cancel, preview image not loaded", Snackbar.LENGTH_SHORT).show()
                }
            }
            parentFragmentManager.popBackStack()
        }
    }
}
