package com.aqchen.filterfiesta.ui.photo_editor

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.bottom_bars.home.BottomBarHomeFragment
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Future

class PhotoEditorFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoEditorFragment()
    }

    private lateinit var photoEditorImagesViewModel: PhotoEditorImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomBarFragment: FragmentContainerView = view.findViewById(R.id.photo_editor_bottom_bar)
        childFragmentManager.beginTransaction().replace(R.id.photo_editor_bottom_bar, BottomBarHomeFragment()).commit()
        childFragmentManager.restoreBackStack("photo_editor_bottom_bar")

        lifecycleScope.launch {
            photoEditorImagesViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            photoEditorImagesViewModel.previewImageFile = File(requireContext().filesDir, "preview_image")
            photoEditorImagesViewModel.filterPreviewImageFile = File(requireContext().filesDir, "filter_preview_image")
            val uri = Uri.fromFile(photoEditorImagesViewModel.previewImageFile)

            val target = object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    photoEditorImagesViewModel.previewImageBitmap = resource
//                    photoEditorImagesViewModel.filterPreviewBitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            }

            Glide.with(requireContext())
                .asBitmap()
                .load(uri)
                .into(target)
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}
