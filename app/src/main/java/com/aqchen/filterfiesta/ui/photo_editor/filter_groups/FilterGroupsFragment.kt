package com.aqchen.filterfiesta.ui.photo_editor.filter_groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqchen.filterfiesta.R

class FilterGroupsFragment : Fragment() {

    companion object {
        fun newInstance() = FilterGroupsFragment()
    }

    private lateinit var viewModel: FilterGroupsModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_groups, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilterGroupsModel::class.java)
        // TODO: Use the ViewModel
    }

}