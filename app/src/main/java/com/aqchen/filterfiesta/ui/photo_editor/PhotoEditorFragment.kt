package com.aqchen.filterfiesta.ui.photo_editor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqchen.filterfiesta.R

class PhotoEditorFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoEditorFragment()
    }

    private lateinit var viewModel: PhotoEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_editor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoEditorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}