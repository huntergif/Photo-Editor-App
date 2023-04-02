package com.aqchen.filterfiesta.ui.photo_editor.bottom_bars.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
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

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    val previewImageBitmap = photoEditorImagesViewModel.previewImageBitmapStateFlow.value
                    photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetDisplayedPhotoEditorBitmap(previewImageBitmap))
                }
            }
        }
    }
}