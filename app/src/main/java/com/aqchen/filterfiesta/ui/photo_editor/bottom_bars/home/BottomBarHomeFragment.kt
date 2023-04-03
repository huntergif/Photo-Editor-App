package com.aqchen.filterfiesta.ui.photo_editor.bottom_bars.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class BottomBarHomeFragment : Fragment() {

    companion object {
        fun newInstance() = BottomBarHomeFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_bar_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolPagerFragment: FragmentContainerView = view.findViewById(R.id.tool_pager_fragment_container)
        childFragmentManager.beginTransaction().replace(R.id.tool_pager_fragment_container, ToolPagerFragment()).commit()

        val cancelButton: Button = view.findViewById(R.id.photo_editor_home_cancel_button)
        val saveButton: Button = view.findViewById(R.id.photo_editor_home_save_button)

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    photoEditorImagesViewModel.previewImageBitmapStateFlow.collect {
                        Log.d("BottomBarHomeFragment", "Collected preview image bitmap")
                        photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(it))
//                        when (it) {
//                            is Resource.Success -> {
//                                photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(it))
//                            }
//                            else -> {}
//                        }
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.Cancel)
        }

        saveButton.setOnClickListener {
            photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.Save)
        }
    }
}