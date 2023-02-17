package com.aqchen.filterfiesta.ui.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginFlow.collect {
                    when (it) {
                        is Resource.Success -> TODO()
                        is Resource.Error -> TODO()
                        Resource.Loading -> TODO()
                        null -> TODO()
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        val submitBtn = view.findViewById<Button>(R.id.loginSubmit)

        submitBtn.setOnClickListener { viewModel.login("a@a.com", "1234")  }
    }

}