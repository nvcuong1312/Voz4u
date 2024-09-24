package com.nvcutu.voz.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import com.nvcutu.voz.R
import com.nvcutu.voz.models.BottomBarPageItem
import com.nvcutu.voz.models.ThreadListItemModel
import java.util.Objects

class BottomBarPageItemAdapter(private val mList: List<BottomBarPageItem>) :
    RecyclerView.Adapter<BottomBarPageItemAdapter.ViewHolder>() {
    var onItemClick: ((BottomBarPageItem) -> Unit)? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottom_bar_page_item, parent, false)

        context = parent.context

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.tvBottomBarPageItem.text = itemsViewModel.page.toString()
        if (itemsViewModel.isSelected) {
            holder.vBottomBarPageItem.setBackgroundColor(context.getColor(R.color.page_un_active))
        } else {
            holder.vBottomBarPageItem.setBackgroundColor(context.getColor(R.color.page_active))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvBottomBarPageItem: TextView = itemView.findViewById(R.id.tvBottomBarPageItem)
        var vBottomBarPageItem: View = itemView.findViewById(R.id.vBottomBarPageItem)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }
}