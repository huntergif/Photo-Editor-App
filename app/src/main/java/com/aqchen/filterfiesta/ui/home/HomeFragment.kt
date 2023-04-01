package com.aqchen.filterfiesta.ui.home

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
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.auth.login.LoginFormEvent
import com.aqchen.filterfiesta.ui.auth.login.LoginViewModel
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.util.Resource
import com.aqchen.filterfiesta.util.setTextViewWithClickableSpan
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: PhotoEditorImagesViewModel
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Registers a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("HomeFragment", "Selected URI: $uri")
                viewModel.onEvent(PhotoEditorImagesEvent.SetBaseImage(uri))
                // navigate to photo editor fragment
                findNavController().navigate(R.id.action_homeFragment_to_photoEditorFragment)
            } else {
                Log.d("HomeFragment", "No media selected")
            }
        }

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
        val selectPhotoButton = view.findViewById<MaterialButton>(R.id.select_roll_button)
        val takePhotoButton = view.findViewById<MaterialButton>(R.id.select_camera_button)

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
    }
}