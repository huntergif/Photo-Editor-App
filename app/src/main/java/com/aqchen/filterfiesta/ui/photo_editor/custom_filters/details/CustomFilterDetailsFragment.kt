package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.shared_view_models.view_custom_filter.ViewCustomFilterViewModel
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.motion.MotionUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class CustomFilterDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFilterDetailsFragment()
    }

    private lateinit var viewModel: CustomFilterDetailsViewModel
    private lateinit var viewCustomFilterViewModel: ViewCustomFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // The drawing view is the id of the view above which the container transform
            // will play in z-space.
            drawingViewId = R.id.nav_host_fragment_content_main
            duration = MotionUtils.resolveThemeDuration(
                requireContext(),
                com.google.android.material.R.attr.motionDurationMedium3,
                350
            ).toLong()
            // Set the color of the scrim to transparent as we also want to animate the
            // list fragment out of view
            scrimColor = Color.TRANSPARENT
            val containerColor = TypedValue().let {
                requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, it, true)
                requireContext().getColor(it.resourceId)
            }
            setAllContainerColors(containerColor)
        }

        return inflater.inflate(R.layout.fragment_custom_filter_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView: TextView = view.findViewById(R.id.custom_filter_details_name_label)
        val recyclerview: RecyclerView = view.findViewById(R.id.custom_filter_details_filters_recycler_view)
        val adapter = CustomFilterDetailsAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[CustomFilterDetailsViewModel::class.java]
            viewCustomFilterViewModel = ViewModelProvider(requireActivity())[ViewCustomFilterViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // collect updates to the current selected custom filter
                    viewCustomFilterViewModel.viewCustomFilterStateFlow.collect {
                        if (it != null) {
                            nameTextView.text = it.name
                            viewModel.onEvent(CustomFilterDetailsEvent.CustomFilterChanged(it))
                        }
                    }
                }
                launch {
                    // collect update to the custom filter's filters (the filter list that make up the custom filter)
                    viewModel.customFilterStateFlow.collect {
                        if (it.customFilter != null) {
                            adapter.submitList(it.customFilter.filters)
                        }
                    }
                }
                launch {
                    // collect updates to delete request state
                    viewModel.deleteCustomFilterStateStateFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                Snackbar.make(view, "Failed to delete custom filter", Snackbar.LENGTH_LONG).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                viewModel.onEvent(CustomFilterDetailsEvent.ResetState)
                                // navigate back to the details list fragment
                                findNavController().popBackStack()
                                Snackbar.make(view, "Successfully deleted custom filter", Snackbar.LENGTH_LONG).show()
                            }
                            null -> {}
                        }
                    }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_custom_filter_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_custom_filter_details_edit -> {
                        // pairs with transitions in edit details fragment
                        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
                            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
                        }
                        findNavController().navigate(R.id.action_customFiltersDetailsFragment_to_editCustomFilterDetailsFragment)
                        true
                    }
                    R.id.action_custom_filter_details_delete -> {
                        viewModel.onEvent(CustomFilterDetailsEvent.DeleteCustomFilter)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}