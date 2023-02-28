package com.aqchen.filterfiesta.ui.photo_editor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.ui.photo_editor.tool_pager.ToolPagerFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolPagerFragment = view.findViewById<FragmentContainerView>(R.id.tool_pager_fragment_container)
        childFragmentManager.beginTransaction().replace(R.id.tool_pager_fragment_container, ToolPagerFragment()).commit()

    }

}