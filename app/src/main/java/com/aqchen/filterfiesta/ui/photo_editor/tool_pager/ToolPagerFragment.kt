package com.aqchen.filterfiesta.ui.photo_editor.tool_pager

import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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
import com.aqchen.filterfiesta.domain.models.ToolPage
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.AdjustmentsFragment
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.CustomFiltersFragment
import com.aqchen.filterfiesta.ui.photo_editor.preset_filters.PresetFiltersFragment
import com.aqchen.filterfiesta.ui.util.CenterLinearLayoutManager
import kotlinx.coroutines.launch


class ToolPagerFragment : Fragment() {

    companion object {
        fun newInstance() = ToolPagerFragment()
    }

    private lateinit var viewModel: ToolPagerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToolPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tool_pager_recycler_view)
        val layoutManager = CenterLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.clipToPadding = false // needed for CenterLinearLayoutManager to work

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

            adapter = ToolPagerAdapter(
                viewModel.toolPages,
                viewModel.selectedPositionFlow,
            ) { itemPos ->
                recyclerView.smoothScrollToPosition(itemPos)
            }
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            recyclerView.adapter = adapter

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.currentPositionFlow.collect { currentPos ->
                        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            @Suppress("WrongConstant")
                            val vibratorManager =
                                context?.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager?
                            vibratorManager?.defaultVibrator
                        } else {
                            @Suppress("DEPRECATION")
                            context?.getSystemService(VIBRATOR_SERVICE) as Vibrator?
                        })?.vibrate(VibrationEffect.createOneShot(8, 50))
                    }
                }

                launch {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.selectedPositionFlow.collect { selectedPos ->
                            adapter.notifyItemChanged(selectedPos)
                            val prevPos = viewModel.previousSelectedPosition
                            if (prevPos != null) {
                                adapter.notifyItemChanged(prevPos)
                            }
                            val toolPage = viewModel.toolPages[selectedPos]
                            val ft = parentFragmentManager.beginTransaction()
                            val newFragment = when (toolPage) {
                                is ToolPage.CustomFilters -> {
                                    CustomFiltersFragment.newInstance()
                                }

                                ToolPage.Adjustments -> {
                                    AdjustmentsFragment.newInstance()
                                }

                                ToolPage.OtherTools -> {
                                    AdjustmentsFragment.newInstance()
                                }

                                ToolPage.PresetFilters -> {
                                    PresetFiltersFragment.newInstance()
                                }
                            }
                            ft.replace(R.id.tool_page_fragment_container, newFragment)
                            ft.commit()
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
