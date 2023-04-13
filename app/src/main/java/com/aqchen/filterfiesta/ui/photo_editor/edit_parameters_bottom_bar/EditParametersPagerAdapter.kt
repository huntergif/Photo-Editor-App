package com.aqchen.filterfiesta.ui.photo_editor.edit_parameters_bottom_bar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.domain.models.image.ParameterSetting

class EditParametersPagerAdapter(
    private val adjustmentParameterSettings: List<ParameterSetting>
) : RecyclerView.Adapter<EditParametersPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.view_holder_edit_adjustment_parameter_page_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_adjustment_parameter_page, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = adjustmentParameterSettings[position].name
    }

    override fun getItemCount() = adjustmentParameterSettings.size
}
