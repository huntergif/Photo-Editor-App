package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqchen.filterfiesta.R

class CustomFiltersDetailsList : Fragment() {

    companion object {
        fun newInstance() = CustomFiltersDetailsList()
    }

    private lateinit var viewModel: CustomFiltersDetailsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_filters_details_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
