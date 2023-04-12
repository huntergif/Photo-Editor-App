package com.aqchen.filterfiesta.ui.photo_editor.save_modal_bottom_sheet

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list.CustomFiltersDetailsListEvent
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list.CustomFiltersDetailsListViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.BitmapType
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.ui.util.saveImageToCache
import com.aqchen.filterfiesta.ui.util.saveImageToStream
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SaveModalBottomSheetFragment: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "save_modal_bottom_sheet"
    }

    private lateinit var viewModel: SaveModalBottomSheetViewModel
    private lateinit var customFiltersDetailsListViewModel: CustomFiltersDetailsListViewModel
    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_modal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveButton: Button = view.findViewById(R.id.bottom_sheet_save_button)
        val shareButton: Button = view.findViewById(R.id.bottom_sheet_share_button)
        val loadingIndicator: ProgressBar = view.findViewById(R.id.bottom_sheet_save_loading_indicator)
        val customFiltersRecyclerView: RecyclerView = view.findViewById(R.id.bottom_sheet_save_custom_filters_recycler_view)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = SaveAsCustomFilterAdapter(requireContext()) { _, it ->
            if (it.id != null && viewModel.saveCustomFilterStateFlow.value !is Resource.Loading) {
                Snackbar.make(view, "Save to ${it.name}", Snackbar.LENGTH_LONG).show()
                viewModel.onEvent(SaveModalBottomSheetEvent.SaveAsCustomFilter(it, photoEditorImagesViewModel.imageFiltersStateFlow.value))
            } else {
                Snackbar.make(view, "Invalid custom filter to save to", Snackbar.LENGTH_LONG).show()
            }
        }
        customFiltersRecyclerView.layoutManager = layoutManager
        customFiltersRecyclerView.adapter = adapter
        customFiltersRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        val modalBottomSheetBehavior = dialog.behavior

        lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[SaveModalBottomSheetViewModel::class.java]
            customFiltersDetailsListViewModel = ViewModelProvider(requireActivity())[CustomFiltersDetailsListViewModel::class.java]
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            customFiltersDetailsListViewModel.onEvent(CustomFiltersDetailsListEvent.LoadCustomFilters)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // collect custom filter list updates
                    customFiltersDetailsListViewModel.customFiltersStateFlow.collect {
                        when (it.getCustomFiltersStatus) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                launch {
                                    it.customFilters?.collect { customFilters ->
                                        adapter.submitList(customFilters)
                                    }
                                }
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    viewModel.exportStateFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                loadingIndicator.visibility = INVISIBLE
                                saveButton.isEnabled = true
                                shareButton.isEnabled = true
                                Snackbar.make(view, it.errorMessage, Snackbar.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                loadingIndicator.visibility = VISIBLE
                                saveButton.isEnabled = false
                                shareButton.isEnabled = false
                            }
                            is Resource.Success -> {
                                loadingIndicator.visibility = INVISIBLE
                                saveButton.isEnabled = true
                                shareButton.isEnabled = true
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    photoEditorImagesViewModel.imageExportBitmapStateFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                loadingIndicator.visibility = INVISIBLE
                                modalBottomSheetBehavior.isHideable = true
                                saveButton.isEnabled = true
                                shareButton.isEnabled = true
                                Snackbar.make(view, it.errorMessage, Snackbar.LENGTH_LONG).show()
                            }
                            is Resource.Loading -> {
                                loadingIndicator.visibility = VISIBLE
                                modalBottomSheetBehavior.isHideable = false
                                saveButton.isEnabled = false
                                shareButton.isEnabled = false
                            }
                            is Resource.Success -> {
                                launch {
                                    viewModel.onEvent(
                                        SaveModalBottomSheetEvent.SetExportState(
                                            Resource.Loading
                                        )
                                    )
                                    when (viewModel.exportTypeStateFlow.value) {
                                        ExportType.SAVE -> {
                                            launch(Dispatchers.IO) {
                                                saveImage(it.data, requireContext(), "FilterFiesta")
                                            }
                                            Snackbar.make(
                                                view,
                                                "Saved image on device",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }

                                        ExportType.SHARE -> {
                                            launch(Dispatchers.IO) {
                                                shareImage(it.data)
                                            }
                                        }

                                        null -> {}
                                    }
                                    viewModel.onEvent(SaveModalBottomSheetEvent.SetExportType(null))
                                    viewModel.onEvent(
                                        SaveModalBottomSheetEvent.SetExportState(
                                            Resource.Success(Unit)
                                        )
                                    )
                                    loadingIndicator.visibility = INVISIBLE
                                    modalBottomSheetBehavior.isHideable = true
                                    saveButton.isEnabled = true
                                    shareButton.isEnabled = true
                                }
                            }
                            null -> {}
                        }
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            modalBottomSheetBehavior.isHideable = false
            loadingIndicator.visibility = VISIBLE
            viewModel.onEvent(SaveModalBottomSheetEvent.SetExportType(ExportType.SAVE))
            photoEditorImagesViewModel.onEvent(
                PhotoEditorImagesEvent.GenerateBitmapUsingFilters(
                    photoEditorImagesViewModel.imageFiltersStateFlow.value,
                    BitmapType.IMAGE_EXPORT_ORIGINAL_RES,
                )
            )
        }

        shareButton.setOnClickListener {
            //viewModel.onEvent(SaveModalBottomSheetEvent.SetSaveState(Resource.Loading))
            modalBottomSheetBehavior.isHideable = false
            loadingIndicator.visibility = VISIBLE
            viewModel.onEvent(SaveModalBottomSheetEvent.SetExportType(ExportType.SHARE))
            photoEditorImagesViewModel.onEvent(
                PhotoEditorImagesEvent.GenerateBitmapUsingFilters(
                    photoEditorImagesViewModel.imageFiltersStateFlow.value,
                    BitmapType.IMAGE_EXPORT_ORIGINAL_RES,
                )
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val saveModalBottomSheetBehavior = dialog.behavior
        saveModalBottomSheetBehavior.peekHeight = requireParentFragment().requireView().findViewById<FragmentContainerView>(R.id.photo_editor_bottom_bar).height
        return dialog
    }

    // https://stackoverflow.com/questions/36624756/how-to-save-bitmap-to-android-gallery
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    // https://stackoverflow.com/questions/9049143/android-share-intent-for-a-bitmap-is-it-possible-not-to-save-it-prior-sharing
    private fun shareImage(bitmap: Bitmap) {
        val contentUri = saveImageToCache(bitmap, "image.png", requireContext())
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        // temporary permission for receiving app to read this file
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(
            contentUri,
            requireContext().contentResolver.getType(contentUri)
        )
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(shareIntent, "Choose an app"))
    }
}
