package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.edit_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.view_custom_filter.ViewCustomFilterEvent
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.shared_view_models.view_custom_filter.ViewCustomFilterViewModel
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class EditCustomFilterDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = EditCustomFilterDetailsFragment()
    }

    private lateinit var viewModel: EditCustomFilterDetailsViewModel
    private lateinit var viewCustomFilterViewModel: ViewCustomFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // pairs with transitions in custom filters details fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        return inflater.inflate(R.layout.fragment_edit_custom_filter_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextInputLayout: TextInputLayout = view.findViewById(R.id.edit_custom_filter_details_name_input_layout)
        val nameTextInput: EditText = view.findViewById(R.id.edit_custom_filter_details_name_input)
        val submitButton: Button = view.findViewById(R.id.edit_custom_filter_details_submit_button)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[EditCustomFilterDetailsViewModel::class.java]
            viewCustomFilterViewModel = ViewModelProvider(requireActivity())[ViewCustomFilterViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewCustomFilterViewModel.viewCustomFilterStateFlow.collect {
                        if (it != null) {
                            submitButton.isEnabled = true
                            viewModel.onEvent(EditCustomFilterDetailsFormEvent.CustomFilterChanged(it))
                        } else {
                            submitButton.isEnabled = false
                            Snackbar.make(view, "No custom filter selected", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                launch {
                    viewModel.editCustomFilterFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                submitButton.isEnabled = true
                                Snackbar.make(view, R.string.edit_custom_filter_error, Snackbar.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                submitButton.isEnabled = false
                            }
                            is Resource.Success -> {
                                Snackbar.make(view, R.string.edit_custom_filter_success, Snackbar.LENGTH_LONG).show()
                                findNavController().popBackStack()
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    viewModel.editCustomFilterFormStateFlow.collect {
                        nameTextInputLayout.error = it.nameError
                    }
                }
            }
        }

        nameTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(EditCustomFilterDetailsFormEvent.NameChanged(text.toString()))
        }

        submitButton.setOnClickListener {
            viewModel.onEvent(EditCustomFilterDetailsFormEvent.Submit)
        }
    }

}
