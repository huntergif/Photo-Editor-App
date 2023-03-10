package com.aqchen.filterfiesta.ui.photo_editor.adjustments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aqchen.filterfiesta.R
import kotlinx.coroutines.launch

class AdjustmentsFragment: Fragment() {
    companion object {
        fun newInstance() = AdjustmentsFragment()
    }

    private lateinit var viewModel: AdjustmentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool_page_adjustments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[AdjustmentsViewModel::class.java]
        }
    }
}