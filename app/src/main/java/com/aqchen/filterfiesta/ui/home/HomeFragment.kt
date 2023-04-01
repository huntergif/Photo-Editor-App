package com.aqchen.filterfiesta.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel
    private var photoUri: Uri? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val selectCameraButton: Button = view.findViewById(R.id.select_camera_button)

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
