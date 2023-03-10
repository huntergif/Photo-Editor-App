package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.aqchen.filterfiesta.R
import com.google.android.material.motion.MotionUtils
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch

class FilterGroupsFragment : Fragment() {

    companion object {
        fun newInstance() = FilterGroupsFragment()
    }

    private lateinit var viewModel: FilterGroupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[FilterGroupsViewModel::class.java]
        }

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
    }

}