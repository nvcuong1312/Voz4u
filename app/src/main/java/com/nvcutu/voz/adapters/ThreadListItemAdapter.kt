package com.nvcutu.voz.adapters

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nvcutu.voz.R
import com.nvcutu.voz.models.ThreadListItemModel

class ThreadListItemAdapter(private val mList: List<ThreadListItemModel>) :
    RecyclerView.Adapter<ThreadListItemAdapter.ViewHolder>() {
    var onItemClick: ((ThreadListItemModel) -> Unit)? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.thread_list_item, parent, false)

        context = parent.context
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        if (itemsViewModel.avtText.isNotEmpty()) {
            holder.tvThreadListItemAvt.text = itemsViewModel.avtText
            holder.tvThreadListItemAvt.setTextColor(Color.parseColor(itemsViewModel.avtTextColor))
            holder.vThreadListItemAvtBg.setBackgroundColor(Color.parseColor(itemsViewModel.avtBgColor))
        } else {
            Glide.with(context).load(itemsViewModel.avtUrl).into(holder.imvThreadListItemIcon)
        }

        holder.tvThreadListItemName.text = itemsViewModel.title
        holder.tvThreadListItemAuthor.text = itemsViewModel.author
        holder.tvThreadListItemRepliesAndTime.text =
            "Replies: ${itemsViewModel.replies} | Views: ${itemsViewModel.views} | ${itemsViewModel.lastTime}"

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvThreadListItemAvt: TextView = itemView.findViewById(R.id.tvThreadListItemAvt)
        val vThreadListItemAvtBg: View = itemView.findViewById(R.id.vThreadListItemAvtBg)
        val imvThreadListItemIcon: ImageView = itemView.findViewById(R.id.imvThreadListItemIcon)
        val tvThreadListItemName: TextView = itemView.findViewById(R.id.tvThreadListItemName)
        val tvThreadListItemAuthor: TextView = itemView.findViewById(R.id.tvThreadListItemAuthor)
        val tvThreadListItemRepliesAndTime: TextView =
            itemView.findViewById(R.id.tvThreadListItemRepliesAndTime)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(mList[adapterPosition])
            }
        }
    }
}