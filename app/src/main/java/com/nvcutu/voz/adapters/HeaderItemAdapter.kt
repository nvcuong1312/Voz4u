package com.nvcutu.voz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nvcutu.voz.R
import com.nvcutu.voz.models.HeaderHomeItemModel

class HeaderItemAdapter(private val mList: List<HeaderHomeItemModel>) :
    RecyclerView.Adapter<HeaderItemAdapter.ViewHolder>() {
         var onItemClick: ((HeaderHomeItemModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_header_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.textView.text = itemsViewModel.name
        holder.threadView.text = "Threads: " + itemsViewModel.totalThread
        holder.messageView.text = "Messages: " + itemsViewModel.totalMessage
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvItemName)
        val threadView: TextView = itemView.findViewById(R.id.tvItemThread)
        val messageView: TextView = itemView.findViewById(R.id.tvItemMessage)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }
}