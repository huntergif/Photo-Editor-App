package com.aqchen.filterfiesta.ui.photo_editor.tool_pager

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.aqchen.filterfiesta.domain.models.ToolPage
import com.aqchen.filterfiesta.domain.use_case.tool_pages.GetToolPagesUseCase
import com.aqchen.filterfiesta.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ToolPagerViewModel @Inject constructor(
    private val getToolPagesUseCase: GetToolPagesUseCase
) : ViewModel() {
    private val _toolPagesFlow = MutableStateFlow<Resource<List<ToolPage>>>(Resource.Loading)
    val toolPagesFlow: StateFlow<Resource<List<ToolPage>>> = _toolPagesFlow

    private val _currentPositionFlow = MutableStateFlow(0)
    val currentPositionFlow: StateFlow<Int> = _currentPositionFlow

    private val _selectedPositionFlow = MutableStateFlow(0)
    val selectedPositionFlow: StateFlow<Int> = _selectedPositionFlow

    val toolPages = getToolPagesUseCase()

    var previousSelectedPosition: Int? = null

    fun onEvent(event: ToolPagerEvent) {
        when (event) {
            is ToolPagerEvent.LoadList -> {
                _toolPagesFlow.value = Resource.Success(getToolPagesUseCase())
            }
            is ToolPagerEvent.SelectFocusedToolPage -> {
                previousSelectedPosition = _selectedPositionFlow.value
                _selectedPositionFlow.value = _currentPositionFlow.value
            }
            is ToolPagerEvent.FocusToolPageViewHolder -> {
                _currentPositionFlow.value = event.position
            }
        }
    }

    // https://stackoverflow.com/a/68938346
    // For restoring recycler view scroll state
    // tried the other answers - this was the only one that worked
    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

}
