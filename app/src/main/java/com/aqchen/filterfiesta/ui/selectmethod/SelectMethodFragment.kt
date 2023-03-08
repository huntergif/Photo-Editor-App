package com.aqchen.filterfiesta.ui.selectmethod

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqchen.filterfiesta.R

class SelectMethodFragment : Fragment() {

    companion object {
        fun newInstance() = SelectMethodFragment()
    }

    private lateinit var viewModel: SelectMethodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_method, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectMethodViewModel::class.java)
        // TODO: Use the ViewModel
    }

}