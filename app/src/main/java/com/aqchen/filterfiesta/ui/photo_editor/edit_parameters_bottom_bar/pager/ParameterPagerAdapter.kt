package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar.pager

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting
import kotlinx.coroutines.flow.StateFlow

class ParameterPagerAdapter(
    val parameterPages: List<ParameterSetting>,
    private val selectedPositionFlow: StateFlow<Int>,
    private val onClickListener: (position: Int) -> Unit,
) : RecyclerView.Adapter<ParameterPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.view_holder_edit_adjustment_parameter_page_name)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_adjustment_parameter_page, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = parameterPages[position].name

        viewHolder.itemView.setOnClickListener {
            onClickListener(position)
        }

        viewHolder.isSelectedPosition = position == selectedPositionFlow.value
    }

    override fun getItemCount() = parameterPages.size
}
