package com.nvcutu.voz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nvcutu.voz.R
import com.nvcutu.voz.models.HeaderHomeItemModel
import com.nvcutu.voz.models.HeaderHomeModel

class HeaderAdapter(private val mList: List<HeaderHomeModel>) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
    var onClick: ((HeaderHomeModel, Int) -> Unit)? = null
    var onItemClick: ((HeaderHomeItemModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_header, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        // Text
        holder.textView.text = itemsViewModel.name

        // List
        val arrayList = arrayListOf<HeaderHomeItemModel>()
        val headerAdapter = HeaderItemAdapter(arrayList)
        holder.childView.adapter = headerAdapter.apply {
            onItemClick = {
                this@HeaderAdapter.onItemClick?.invoke(it)
            }
        }

        arrayList.addAll(itemsViewModel.items)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = this.itemView.findViewById(R.id.tvHeaderName)
        val childView: RecyclerView = this.itemView.findViewById(R.id.rcyItemBox)

        init {
            itemView.setOnClickListener {
                onClick?.invoke(mList[adapterPosition], adapterPosition)
            }
        }
    }
}