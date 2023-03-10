package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.Filter

class CustomFiltersDetailsAdapter : ListAdapter<Filter, CustomFiltersDetailsAdapter.ViewHolder>(FilterDiff) {

    object FilterDiff: DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            Log.d("CustomFiltersAdapter", "areItemsTheSame")
            return oldItem.type == newItem.type && newItem.parameters == oldItem.parameters
        }

        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            Log.d("CustomFiltersAdapter", "areContentsTheSame")
            return oldItem == newItem
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView
        val parametersTextView: TextView

        init {
            nameTextView = view.findViewById(R.id.view_holder_custom_filter_detail_filter_name)
            parametersTextView = view.findViewById(R.id.view_holder_custom_filter_detail_filter_parameters)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_custom_filter_detail, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = getItem(position).type
        holder.parametersTextView.text = getItem(position).parameters.toString()
    }
}
