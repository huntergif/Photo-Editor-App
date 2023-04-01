package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager.ParameterPagerFragment

class EditParametersFragment : Fragment() {

    companion object {
        fun newInstance() = EditParametersFragment()
    }

    private lateinit var viewModel: EditParametersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_bar_edit_parameters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.edit_parameters_pager_container, ParameterPagerFragment()).commit()
    }
}
