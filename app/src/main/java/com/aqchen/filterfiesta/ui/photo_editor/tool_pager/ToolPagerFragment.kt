package com.aqchen.filterfiesta.ui.photo_editor.tool_pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.util.CenterLinearLayoutManager
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ToolPagerFragment : Fragment() {

    companion object {
        fun newInstance() = ToolPagerFragment()
    }

    private lateinit var viewModel: ToolPagerViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.tool_pager_recycler_view)
        val layoutManager = CenterLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.clipToPadding = false // needed for CenterLinearLayoutManager to work
        val adapter = ToolPagerAdapter(emptyList())
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = adapter

        // Add snap functionality
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // get the currently selected tool
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                viewModel.onEvent(ToolPagerEvent.FocusToolPageViewHolder(snapHelper.getSnapPosition(recyclerView)))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.onEvent(ToolPagerEvent.SelectFocusedToolPage)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[ToolPagerViewModel::class.java]
            viewModel.onEvent(ToolPagerEvent.LoadList)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.toolPagesFlow.collect {
                        when (it) {
                            is Resource.Success -> {
                                recyclerView.adapter = ToolPagerAdapter(it.data)
                                adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            }
                            else -> {
                                Snackbar.make(view, R.string.tool_pages_get_failure, Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                launch {
                    viewModel.currentPositionFlow.collect {
                        Log.d("ToolPagerFragment", "current pos: $it")
                    }
                }

                launch {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.selectedPositionFlow.collect {
                            Log.d("ToolPagerFragment", "selected pos: $it")
                        }
                    }
                }
            }
        }
    }

    // https://stackoverflow.com/a/68938346
    // For restoring recycler view scroll state
    override fun onPause() {
        super.onPause()
        recyclerView.layoutManager?.onSaveInstanceState()?.let { viewModel.saveRecyclerViewState(it) }
    }

    // https://stackoverflow.com/a/68938346
    // For restoring recycler view scroll state
    override fun onResume() {
        super.onResume()
        if (viewModel.stateInitialized()) {
            recyclerView.layoutManager?.onRestoreInstanceState(
                viewModel.restoreRecyclerViewState()
            )
        }
    }

    // https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424
    // Gets index position of current snap target
    private fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}
