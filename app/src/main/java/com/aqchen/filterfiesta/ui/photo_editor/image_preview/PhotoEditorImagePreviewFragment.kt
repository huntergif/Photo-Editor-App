package com.aqchen.filterfiesta.ui.photo_editor.image_preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.CustomFiltersViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class PhotoEditorImagePreviewFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoEditorImagePreviewFragment()
    }

    private lateinit var viewModel: PhotoEditorImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView: ImageView = view.findViewById(R.id.image_preview_image_view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.baseImageStateFlow.collect {
                    Glide.with(requireActivity())
                        .load(it?.imageUri)
                        .placeholder(R.drawable.baseline_add_photo_alternate_70)
                        .into(imageView)
                }
            }
        }
    }
}
