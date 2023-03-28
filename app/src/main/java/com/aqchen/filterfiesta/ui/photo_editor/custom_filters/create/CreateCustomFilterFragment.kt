package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.create

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.custom_filters.CustomFiltersFragment
import com.aqchen.filterfiesta.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class CreateCustomFilterFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFiltersFragment()
    }

    private lateinit var viewModel: CreateCustomFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // pairs with transitions in photo editor fragment
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_create_custom_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextInputLayout: TextInputLayout = view.findViewById(R.id.create_custom_filter_name_input_layout)
        val nameTextInput: EditText = view.findViewById(R.id.create_custom_filter_name_input)
        val submitButton: Button = view.findViewById(R.id.create_custom_filter_submit_button)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel = ViewModelProvider(requireActivity())[CreateCustomFilterViewModel::class.java]

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.createCustomFilterFlow.collect {
                        when (it) {
                            is Resource.Error -> {
                                submitButton.isEnabled = true
                                Snackbar.make(view, R.string.create_custom_filter_error, Snackbar.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                submitButton.isEnabled = false
                            }
                            is Resource.Success -> {
                                Snackbar.make(view, R.string.create_custom_filter_success, Snackbar.LENGTH_LONG).show()
                                findNavController().popBackStack()
                            }
                            null -> {}
                        }
                    }
                }
                launch {
                    viewModel.createCustomFilterFormStateFlow.collect {
                        nameTextInputLayout.error = it.nameError
                    }
                }
            }
        }

        nameTextInput.doAfterTextChanged {
                text -> viewModel.onEvent(CreateCustomFilterFormEvent.NameChanged(text.toString()))
        }

        submitButton.setOnClickListener {
            viewModel.onEvent(CreateCustomFilterFormEvent.Submit)
        }
    }
}
