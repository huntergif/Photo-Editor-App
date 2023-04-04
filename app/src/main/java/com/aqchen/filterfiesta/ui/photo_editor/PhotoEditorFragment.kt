package com.aqchen.filterfiesta.ui.photo_editor

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.bottom_bars.home.BottomBarHomeFragment
import com.aqchen.filterfiesta.ui.photo_editor.filter_list_side_sheet.FilterListAdapter
import com.aqchen.filterfiesta.ui.photo_editor.save_modal_bottom_sheet.SaveModalBottomSheetFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.BitmapType
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PhotoEditorFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoEditorFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    showConfirmExitDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set enter and return transitions - pairs with transitions in home fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        return inflater.inflate(R.layout.fragment_photo_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.photo_editor_bottom_bar, BottomBarHomeFragment()).commit()
        childFragmentManager.restoreBackStack("photo_editor_bottom_bar")

        val saveModalBottomSheet = SaveModalBottomSheetFragment()
        val filterListSideSheet = SideSheetDialog(requireContext())
        val filterListSideSheetAdapter = FilterListAdapter(emptyList())

        filterListSideSheet.setContentView(R.layout.side_sheet_photo_editor)

        val onItemSwipeListener = object : OnItemSwipeListener<String> {
            override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: String): Boolean {
                // Handle action of item swiped
                // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
                // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
                return false
            }
        }

        val onItemDragListener = object : OnItemDragListener<String> {
            override fun onItemDragged(previousPosition: Int, newPosition: Int, item: String) {
                // Handle action of item being dragged from one position to another
            }

            override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: String) {
                // Handle action of item dropped
            }
        }

        lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            // Need to use lifecycle of owner or else we'll observe the same flows multiple times when fragment is recreated
            // https://stackoverflow.com/questions/67422182/flow-oneach-collect-gets-called-multiple-times-when-back-from-fragment
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                // collect updates to the base image uri
                launch {
                    photoEditorImagesViewModel.baseImageStateFlow.collectLatest {
                        if (it != null && it != photoEditorImagesViewModel.lastProcessedImage) {
                            // reset image filters
                            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetImageFilters(
                                emptyList()))
                            // notify that the base image bitmap is loading
                            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(
                                Resource.Loading
                            ))

                            val target = object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    Log.d("PhotoEditorFragment", "ON RESOURCE READY ${resource.isRecycled}")
                                    photoEditorImagesViewModel.lastProcessedImage = it
                                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetInternalBitmap(resource, BitmapType.PREVIEW_IMAGE))
//                                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(
//                                        Resource.Success(resource)
//                                    ))
                                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetBaseImageBitmap(resource))
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    Log.d("PhotoEditorFragment", "ONLOADCLEARED")
                                    // do nothing (need to have implementation for abstract function)
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    super.onLoadFailed(errorDrawable)

                                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(
                                        Resource.Error("Failed to load base image")
                                    ))
                                }

                            }
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(it.imageUri)
                                .into(target)
                        }
                    }
                }
                // collect updates to the "save" event
                launch {
                    photoEditorImagesViewModel.saveEventStateFlow.collectLatest {
                        saveModalBottomSheet.show(parentFragmentManager, SaveModalBottomSheetFragment.TAG)
                    }
                }
                // collect updates to the "cancel" even
                launch {
                    photoEditorImagesViewModel.cancelEventStateFlow.collectLatest {
                        showConfirmExitDialog()
                    }
                }
                // collect updates to image filters to update side sheet
                launch {
                    photoEditorImagesViewModel.imageFiltersStateFlow.collectLatest {
                        filterListSideSheetAdapter.dataSet = it
                    }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_photo_editor, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_photo_editor_settings -> {
                        // todo menu1
                        true
                    }
                    R.id.action_photo_editor_new_custom_filter -> {
                        // pairs with transitions in create fragment
                        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        childFragmentManager.saveBackStack("photo_editor_bottom_bar")
                        findNavController().navigate(R.id.action_photoEditorFragment_to_createCustomFilterFragment)
                        true
                    }
                    R.id.action_photo_editor_manage_custom_filters -> {
                        // pairs with transitions in details list fragment
                        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        childFragmentManager.saveBackStack("photo_editor_bottom_bar")
                        findNavController().navigate(R.id.action_photoEditorFragment_to_customFiltersDetailsListFragment)
                        true
                    }
                    R.id.action_photo_editor_manage_filters -> {
                        filterListSideSheet.show()
                        val recyclerView = filterListSideSheet.findViewById<DragDropSwipeRecyclerView>(R.id.side_sheet_photo_editor_filter_list_recycler_view)
                        if (recyclerView == null) {
                            Snackbar.make(view, "Failed to show filter list", Snackbar.LENGTH_SHORT).show()
                            return true
                        }
                        Snackbar.make(view, filterListSideSheetAdapter.dataSet.toString(), Snackbar.LENGTH_SHORT).show()
                        recyclerView.adapter = filterListSideSheetAdapter
                        recyclerView.orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
                        recyclerView.swipeListener = onItemSwipeListener
                        recyclerView.dragListener = onItemDragListener
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showConfirmExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_confirm_exit_title))
            .setMessage(resources.getString(R.string.dialog_confirm_exit_description))
            .setNegativeButton(resources.getString(R.string.dialog_confirm_exit_negative_label)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.dialog_confirm_exit_positive_label)) { dialog, which ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_global_homeFragment)
            }
            .show()
    }
}
