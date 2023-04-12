package com.aqchen.filterfiesta.ui.photo_editor.image_preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.CustomFiltersViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

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
        val loadingIndicator: ProgressBar = view.findViewById(R.id.image_preview_loading_indicator)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.displayPhotoEditorBitmapStateFlow.collectLatest {
                        Log.d("DisplayBitmap", "Current filter preview bitmap: ${viewModel.filterPreviewBitmapStateFlow.value}")
                        when (it) {
                            is Resource.Error -> {
                                // glide should fail to load and display the placeholder
                                Glide.with(requireActivity())
                                    .load(it.data)
                                    .placeholder(R.drawable.baseline_add_photo_alternate_70)
                                    .into(imageView)
                                loadingIndicator.visibility = INVISIBLE
                            }
                            Resource.Loading -> {
                                loadingIndicator.visibility = VISIBLE
                            }
                            is Resource.Success -> {
                                Log.d("DisplayBitmap", "RECEIVED DISPLAY BITMAP SUCCESS")
                                loadingIndicator.visibility = INVISIBLE

                                // placeholder removes flickering while loading into image view
                                // https://stackoverflow.com/questions/45142274/glide-showing-imageview-background-in-between-loading-images
                                Glide.with(requireActivity())
                                    .load(it.data)
                                    .placeholder(imageView.drawable)
                                    .into(imageView)
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    viewModel.filterPreviewBitmapStateFlow.collect {
                        Log.d("DisplayBitmap", "Collected filter preview bitmap resource: $it")
                    }
                }
            }
        }
    }
}
