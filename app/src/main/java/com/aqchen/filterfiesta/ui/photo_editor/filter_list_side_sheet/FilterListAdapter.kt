package com.aqchen.filterfiesta.ui.photo_editor.filter_list_side_sheet

import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.Filter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter

class FilterListAdapter(val filters: List<Filter>) : DragDropSwipeAdapter<Filter, FilterListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : DragDropSwipeAdapter.ViewHolder(view) {
        val filterNameTextView: TextView
        val filterDescriptionTextView: TextView
        val dragIcon: ImageView

        init {
            filterNameTextView = view.findViewById(R.id.view_holder_filter_list_filter_name)
            filterDescriptionTextView = view.findViewById(R.id.view_holder_filter_list_filter_description)
            dragIcon = view.findViewById(R.id.view_holder_filter_list_drag_image)
        }
    }

    override fun getViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(item: Filter, viewHolder: ViewHolder, position: Int) {
        viewHolder.filterNameTextView.text = item.type
        viewHolder.filterDescriptionTextView.text = item.parameters.toString()
    }

    override fun getViewToTouchToStartDraggingItem(
        item: Filter,
        viewHolder: ViewHolder,
        position: Int
    ): View? {
        return viewHolder.dragIcon
    }
}
