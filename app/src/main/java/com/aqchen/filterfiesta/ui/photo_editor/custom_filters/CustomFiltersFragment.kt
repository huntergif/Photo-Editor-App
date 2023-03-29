package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerAdapter
import com.aqchen.filterfiesta.ui.util.CenterLinearLayoutManager
import com.aqchen.filterfiesta.ui.util.MarginItemDecoration
import com.aqchen.filterfiesta.ui.util.MarginItemDecorationDirection
import com.google.android.material.motion.MotionUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch

class CustomFiltersFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFiltersFragment()
    }

    private lateinit var viewModel: CustomFiltersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomFiltersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool_page_custom_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitTransition = MaterialFadeThrough().apply {
            duration = MotionUtils.resolveThemeDuration(
                requireContext(),
                com.google.android.material.R.attr.motionDurationMedium2,
                300
            ).toLong()
        }
        enterTransition = MaterialFadeThrough().apply {
            duration = MotionUtils.resolveThemeDuration(
                requireContext(),
                com.google.android.material.R.attr.motionDurationMedium2,
                300
            ).toLong()
        }

        recyclerView = view.findViewById(R.id.custom_filters_recycler_view)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = CustomFiltersAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(MarginItemDecorationDirection.Horizontal))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[CustomFiltersViewModel::class.java]
            viewModel.onEvent(CustomFiltersEvent.LoadCustomFilters)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.customFiltersStateFlow.collect {
                        when (it.getCustomFiltersStatus) {
                            is com.aqchen.filterfiesta.util.Resource.Error -> {
                                Snackbar.make(view, "Failed to get custom filters", Snackbar.LENGTH_LONG).show()
                            }
                            com.aqchen.filterfiesta.util.Resource.Loading -> {
                                // Do nothing for now
                            }
                            is com.aqchen.filterfiesta.util.Resource.Success -> {
                                launch {
                                    it.customFilters?.collect { customFilters ->
                                        adapter.submitList(customFilters)
                                    }
                                }
                            }
                            null -> {
                                // Do nothing
                            }
                        }
                    }
                }
            }
        }
    }

}