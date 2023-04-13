package com.aqchen.filterfiesta.ui.photo_editor.adjustments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.image.newDefaultFilter
import com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar.EditParametersFragment
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesEvent
import com.aqchen.filterfiesta.ui.shared_view_models.photo_editor_images.PhotoEditorImagesViewModel
import com.aqchen.filterfiesta.ui.util.MarginItemDecoration
import com.aqchen.filterfiesta.ui.util.MarginItemDecorationDirection
import com.google.android.material.motion.MotionUtils
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class AdjustmentsFragment: Fragment() {
    companion object {
        fun newInstance() = AdjustmentsFragment()
    }

    private lateinit var viewModel: AdjustmentsViewModel
    private lateinit var photoEditorViewModel: PhotoEditorImagesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdjustmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // pairs with transitions in photo editor fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = MotionUtils.resolveThemeDuration(
                requireContext(),
                com.google.android.material.R.attr.motionDurationMedium3,
                350
            ).toLong()
        }
        exitTransition = MaterialFadeThrough().apply {
            duration = MotionUtils.resolveThemeDuration(
                requireContext(),
                com.google.android.material.R.attr.motionDurationMedium3,
                350
            ).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        return inflater.inflate(R.layout.fragment_tool_page_adjustments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.fragment_tool_page_adjustments_recycler_view)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(MarginItemDecoration(MarginItemDecorationDirection.Horizontal))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[AdjustmentsViewModel::class.java]
            photoEditorViewModel = ViewModelProvider(requireActivity())[PhotoEditorImagesViewModel::class.java]

            adapter = AdjustmentsAdapter {
                // when the user selects an adjustment, we need to add a new "default" filter to the end of the current filter list
                val newFilterList = photoEditorViewModel.imageFiltersStateFlow.value + newDefaultFilter(it)
                // select the new filter by passing in the "new" filter list and selecting the last index
                photoEditorViewModel.onEvent(PhotoEditorImagesEvent.SelectFilter(newFilterList, newFilterList.size - 1))
                // change the bottom bar to tho the edit parameters fragment
                requireParentFragment().parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.photo_editor_bottom_bar, EditParametersFragment.newInstance())
                    addToBackStack("photo_editor_bottom_bar")
                }
            }

            recyclerView.adapter = adapter

            adapter.submitList(viewModel.getAdjustmentsList())
        }

    }
}
