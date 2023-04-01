package com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.adjustments.edit_parameters.pager.ParameterPagerFragment
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment
import kotlinx.coroutines.launch

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
