package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.shared_view_models.view_custom_filter.ViewCustomFilterEvent
import com.aqchen.filterfiesta.ui.shared_view_models.view_custom_filter.ViewCustomFilterViewModel
import com.google.android.material.motion.MotionUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class CustomFiltersDetailsListFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFiltersDetailsListFragment()
    }

    private lateinit var viewModel: CustomFiltersDetailsListViewModel
    private lateinit var viewCustomFilterViewModel: ViewCustomFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // pairs with transitions in photo editor fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        return inflater.inflate(R.layout.fragment_custom_filters_details_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        val recyclerView: RecyclerView = view.findViewById(R.id.custom_filters_details_list_recycler_view)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = CustomFiltersDetailsListAdapter(requireContext()) { view, it ->
            if (it.id != null) {
                viewCustomFilterViewModel.onEvent(ViewCustomFilterEvent.SelectCustomFilter(it))

                // indirectly pairs with transitions in details fragment
                // technically only the details fragment needs the container transform transition, but
                // it looks better when the outgoing fragment also has a transition
                exitTransition = MaterialElevationScale(false).apply {
                    duration = MotionUtils.resolveThemeDuration(
                        requireContext(),
                        com.google.android.material.R.attr.motionDurationMedium3,
                        350
                    ).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = MotionUtils.resolveThemeDuration(
                        requireContext(),
                        com.google.android.material.R.attr.motionDurationMedium3,
                        350
                    ).toLong()
                }
                // we must give the destination fragment transition name in extras so that the
                // container transform transition will work
                val customFilterDetailsTransitionName = getString(
                    R.string.custom_filter_details_container_transition_name
                )
                val extras = FragmentNavigatorExtras(
                    view to customFilterDetailsTransitionName
                )
                findNavController().navigate(R.id.action_customFiltersDetailsListFragment_to_customFiltersDetailsFragment, null, null, extras)
            } else {
                Snackbar.make(view, "No custom filter to view details of", Snackbar.LENGTH_LONG).show()
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[CustomFiltersDetailsListViewModel::class.java]
            viewCustomFilterViewModel = ViewModelProvider(requireActivity())[ViewCustomFilterViewModel::class.java]

            viewModel.onEvent(CustomFiltersDetailsListEvent.LoadCustomFilters)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // collect custom filter list updates
                    viewModel.customFiltersStateFlow.collect {
                        when (it.getCustomFiltersStatus) {
                            is com.aqchen.filterfiesta.util.Resource.Error -> {
                                startPostponedEnterTransition()
                            }
                            com.aqchen.filterfiesta.util.Resource.Loading -> {}
                            is com.aqchen.filterfiesta.util.Resource.Success -> {
                                launch {
                                    it.customFilters?.collect { customFilters ->
                                        adapter.submitList(customFilters)
                                        startPostponedEnterTransition()
                                    }
                                }
                            }
                            null -> {}
                        }
                    }
                }
            }
        }
    }
}
