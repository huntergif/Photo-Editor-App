package com.aqchen.filterfiesta.ui.photo_editor.tool_pager

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.ToolPage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class ToolPagerAdapter(
    private val toolPages: List<ToolPage>,
    private val selectedPositionFlow: StateFlow<Int>,
    private val onClickListener: (position: Int) -> Unit,
) : RecyclerView.Adapter<ToolPagerAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.view_holder_tool_page_name)
        }

        var isSelectedPosition: Boolean = false
            set(value) {
                field = value
                updateTextViewColor()
            }

        private fun updateTextViewColor() {
            if (isSelectedPosition) {
                val color = TypedValue().let {
                    textView.context.theme.resolveAttribute(
                        com.google.android.material.R.attr.colorPrimary,
                        it,
                        true
                    )
                    textView.context.getColor(it.resourceId)
                }
                textView.setTextColor(color)
            } else {
                val color = TypedValue().let {
                    textView.context.theme.resolveAttribute(
                        com.google.android.material.R.attr.colorOnBackground,
                        it,
                        true
                    )
                    textView.context.getColor(it.resourceId)
                }
                textView.setTextColor(color)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_tool_page, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = toolPages[position].pageName

        // https://stackoverflow.com/questions/43673445/why-setonclicklistener-not-working-in-adapter-with-recyclerview
        viewHolder.itemView.setOnClickListener {
            onClickListener(position)
        }

        viewHolder.isSelectedPosition = position == selectedPositionFlow.value
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = toolPages.size
}
