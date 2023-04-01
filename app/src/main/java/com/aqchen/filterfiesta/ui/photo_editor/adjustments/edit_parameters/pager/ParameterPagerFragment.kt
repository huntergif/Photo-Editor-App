package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager

import android.content.Context
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
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.EditParametersEvent
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.EditParametersViewModel
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.parameter.ParameterFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.ui.util.CenterLinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ParameterPagerFragment : Fragment() {

    companion object {
        fun newInstance() = ParameterPagerFragment()
    }

    private lateinit var viewModel: ParameterPagerViewModel
    private lateinit var photoEditorViewModel: PhotoEditorImagesViewModel
    private lateinit var editParametersViewModel: EditParametersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ParameterPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_adjustment_parameter_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.adjustment_parameter_pager_recycler_view)
        val layoutManager = CenterLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.clipToPadding = false

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                viewModel.onEvent(ParameterPagerEvent.FocusParameterPageViewHolder(snapHelper.getSnapPosition(recyclerView)))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.onEvent(ParameterPagerEvent.SelectFocusedParameterPage)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[ParameterPagerViewModel::class.java]
            photoEditorViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]
            editParametersViewModel = ViewModelProvider(requireActivity())[EditParametersViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.currentPositionFlow.collect { currentPos ->
                        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            @Suppress("WrongConstant")
                            val vibratorManager =
                                context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
                            vibratorManager?.defaultVibrator
                        } else {
                            @Suppress("DEPRECATION")
                            context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                        })?.vibrate(VibrationEffect.createOneShot(8, 50))
                    }
                }

                launch {
                    photoEditorViewModel.selectedAdjustmentStateFlow.collect {
                        if (it != null) {
                            adapter = ParameterPagerAdapter(
                                it.adjustment.parameterSettings,
                                viewModel.selectedPositionFlow
                            ) { itemPos ->
                                recyclerView.smoothScrollToPosition(itemPos)
                            }
                            recyclerView.adapter = adapter
                        }
                    }
                }

                launch {
                    viewModel.selectedPositionFlow.collect { selectedPos ->
                        adapter.notifyItemChanged(selectedPos)
                        val prevPos = viewModel.previousSelectedPosition
                        if (prevPos != null) {
                            adapter.notifyItemChanged(prevPos)
                        }
                        editParametersViewModel.onEvent(EditParametersEvent.SelectParameter(adapter.parameterPages[selectedPos]))
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.edit_parameter_fragment_container, ParameterFragment.newInstance())
                            .commit()
                    }
                }
            }
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