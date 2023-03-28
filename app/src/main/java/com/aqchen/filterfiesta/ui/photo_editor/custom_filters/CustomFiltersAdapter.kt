package com.aqchen.filterfiesta.ui.photo_editor.custom_filters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.CustomFilter

// Note we extend androidx.recyclerview.widget.ListAdapter since the list may be dynamic from Firestore
class CustomFiltersAdapter : ListAdapter<CustomFilter, CustomFiltersAdapter.ViewHolder>(FilterGroupDiff) {

    object FilterGroupDiff: DiffUtil.ItemCallback<CustomFilter>() {
        override fun areItemsTheSame(oldItem: CustomFilter, newItem: CustomFilter): Boolean {
            Log.d("CustomFiltersAdapter", "areItemsTheSame")
            if (oldItem.id == null && newItem.id == null) return false
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CustomFilter, newItem: CustomFilter): Boolean {
            Log.d("CustomFiltersAdapter", "areContentsTheSame")
            return oldItem == newItem
        }

    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val textView: TextView

        init {
            imageView = view.findViewById(R.id.view_holder_filter_group_image)
            textView = view.findViewById(R.id.view_holder_filter_group_name)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_custom_filter, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("CustomFiltersAdapter", "ON BIND VIEW HOLDER")
        viewHolder.textView.text = getItem(position).name
    }
}
