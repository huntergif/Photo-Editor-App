package com.aqchen.filterfiesta.ui.photo_editor.adjustments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.image.BaseImageFilter

class AdjustmentsAdapter : ListAdapter<BaseImageFilter, AdjustmentsAdapter.ViewHolder>(
    FilterDiff
) {
    object FilterDiff: DiffUtil.ItemCallback<BaseImageFilter>() {
        override fun areItemsTheSame(oldItem: BaseImageFilter, newItem: BaseImageFilter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BaseImageFilter, newItem: BaseImageFilter): Boolean {
            return oldItem.type == newItem.type
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val textView: TextView

        init {
            imageView = view.findViewById(R.id.view_holder_adjustment_image)
            textView = view.findViewById(R.id.view_holder_adjustment_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_adjustment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = getItem(position).name
    }
}
