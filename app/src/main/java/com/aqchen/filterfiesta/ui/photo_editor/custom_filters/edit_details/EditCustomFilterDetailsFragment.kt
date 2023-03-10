package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.edit_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqchen.filterfiesta.R

class EditCustomFilterDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = EditCustomFilterDetailsFragment()
    }

    private lateinit var viewModel: EditCustomFilterDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_custom_filter_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditCustomFilterDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}