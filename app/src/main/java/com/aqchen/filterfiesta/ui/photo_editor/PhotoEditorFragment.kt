package com.aqchen.filterfiesta.ui.photo_editor

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
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import com.google.android.material.transition.MaterialSharedAxis

class PhotoEditorFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoEditorFragment()
    }

    private lateinit var viewModel: PhotoEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val toolPagerFragment = view.findViewById<FragmentContainerView>(R.id.tool_pager_fragment_container)
        childFragmentManager.beginTransaction().replace(R.id.tool_pager_fragment_container, ToolPagerFragment()).commit()

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
                        findNavController().navigate(R.id.action_photoEditorFragment_to_customFiltersDetailsListFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}
