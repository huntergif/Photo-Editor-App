package com.aqchen.filterfiesta.ui.photo_editor.filter_groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.aqchen.filterfiesta.R
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
    }

}