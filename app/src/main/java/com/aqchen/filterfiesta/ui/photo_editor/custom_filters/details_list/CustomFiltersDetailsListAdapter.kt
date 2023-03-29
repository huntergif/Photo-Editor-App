package com.aqchen.filterfiesta.ui.photo_editor.custom_filters.details_list

import android.content.Context
import android.content.res.Resources
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

// Need to pass context to get string resource
class CustomFiltersDetailsListAdapter(
    private val context: Context,
    private val onClickListener: (view: View, customFilter: CustomFilter) -> Unit,
) : ListAdapter<CustomFilter, CustomFiltersDetailsListAdapter.ViewHolder>(FilterGroupDiff) {

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
        val nameTextView: TextView
        val descriptionTextView: TextView

        init {
            imageView = view.findViewById(R.id.view_holder_details_list_custom_filter_image)
            nameTextView = view.findViewById(R.id.view_holder_details_list_custom_filter_name)
            descriptionTextView = view.findViewById(R.id.view_holder_details_list_custom_filter_description)
        }

        var isDeleteMode: Boolean = false
            set(value) {
                field = value
                updateDeleteButton()
            }

        private fun updateDeleteButton() {
            if (isDeleteMode) {
                imageView.setImageResource(R.drawable.baseline_remove_circle_outline_70)
            } else {
                imageView.setImageResource(R.drawable.baseline_chevron_right_70)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_custom_filter_detail_list, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTextView.text = getItem(position).name
        viewHolder.descriptionTextView.text = context.getString(R.string.custom_filters_details_list_description, getItem(position).filters.size)
        viewHolder.itemView.transitionName = "custom_filters_details_list_container_transition_" + getItem(position).id
        viewHolder.itemView.setOnClickListener {
            Log.i("ToolPagerFragment", "ONCLICK")

            onClickListener(viewHolder.itemView, getItem(position))
        }
    }
}
