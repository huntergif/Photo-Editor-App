package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import androidx.lifecycle.ViewModelProvider
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.view_custom_filter.ViewCustomFilterViewModel
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CustomFiltersDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFiltersDetailsFragment()
    }

    private lateinit var viewModel: CustomFiltersDetailsViewModel
    private lateinit var viewCustomFilterViewModel: ViewCustomFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_filters_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview: RecyclerView = view.findViewById(R.id.custom_filters_details_filters_recycler_view)
        val adapter = CustomFiltersDetailsAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[CustomFiltersDetailsViewModel::class.java]
            viewCustomFilterViewModel = ViewModelProvider(requireActivity())[ViewCustomFilterViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewCustomFilterViewModel.viewCustomFilterStateFlow.collect {
                        if (it != null) {
                            viewModel.onEvent(CustomFiltersDetailsEvent.CustomFilterChanged(it))
                        }
                    }
                }
                launch {
                    viewModel.customFilterStateFlow.collect {
                        if (it.customFilter != null) {
                            adapter.submitList(it.customFilter.filters)
                        }
                    }
                }
                launch {
                    viewModel.deleteCustomFilterStateStateFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                Snackbar.make(view, "Failed to delete custom filter", Snackbar.LENGTH_LONG).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                viewModel.onEvent(CustomFiltersDetailsEvent.ResetState)
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
                        findNavController().navigate(R.id.action_customFiltersDetailsFragment_to_editCustomFilterDetailsFragment)
                        true
                    }
                    R.id.action_custom_filter_details_delete -> {
                        viewModel.onEvent(CustomFiltersDetailsEvent.DeleteCustomFilter)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}