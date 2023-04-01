package com.aqchen.filterfiesta.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel
    private var photoUri: Uri? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { tookPhoto: Boolean ->
        if (tookPhoto && photoUri != null) {
            photoUri?.let{
                photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetBaseImage(it))
                findNavController().navigate(R.id.action_homeFragment_to_photoEditorFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Registers a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("HomeFragment", "Selected URI: $uri")
                photoEditorImagesViewModel.onEvent(PhotoEditorImagesEvent.SetBaseImage(uri))
                // navigate to photo editor fragment
                findNavController().navigate(R.id.action_homeFragment_to_photoEditorFragment)
            } else {
                Log.d("HomeFragment", "No media selected")
            }
        }

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val selectPhotoButton = view.findViewById<MaterialButton>(R.id.select_roll_button)
        val selectCameraButton: Button = view.findViewById(R.id.select_camera_button)

        selectPhotoButton.setOnClickListener {
            //viewModel.onEvent(LoginFormEvent.Submit)
            // Set exit and renter transitions to the register fragment
            // note that we also need to set transitions in the register fragment onCreateView
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }

            // launch the photo picker and allow the user to select images only
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        selectCameraButton.setOnClickListener {
            val photoName = "IMG_${Date()}.JPG"
            val photoFile = File(requireContext().applicationContext.filesDir, photoName)
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.aqchen.filterfiesta.fileprovider",
                photoFile
            )
            takePhoto.launch(photoUri)
        }

        lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
        }
    }
}
