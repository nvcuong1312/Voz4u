package com.nvcutu.voz.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.text.Html.ImageGetter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.nvcutu.voz.R
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.models.ThreadItemModel


class ThreadItemAdapter(
    private val mList: List<ThreadItemModel>,
    private val lifecycle: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<ThreadItemAdapter.ViewHolder>() {
    var onItemClick: ((ThreadItemModel) -> Unit)? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.thread_item, parent, false)

        context = parent.context
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        holder.tvPostAuthor.text = itemsViewModel.author
        holder.tvPostNumber.text = itemsViewModel.postNumber
        holder.tvPostTime.text = itemsViewModel.postTime
        holder.tvAuthorTitle.text = itemsViewModel.authorTitle

        val callBack = object : Drawable.Callback {
            override fun invalidateDrawable(who: Drawable) {
                holder.tvContent.invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                holder.tvContent.postDelayed(what, `when`)
            }

            override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                holder.tvContent.removeCallbacks(what)
            }
        }

        val imageGetter = ImageGetter { source ->
            val finalSource =
                if (!URLUtil.isValidUrl(source)) Resource.URL_BASE + source else source

            val lvlDrw = LevelListDrawable()

            val onLoaded: (Drawable) -> Unit = {
                val scale = getScale(holder.itemView, it.toBitmap().width)

                lvlDrw.addLevel(2, 2, it)
                lvlDrw.level = 2
                lvlDrw.setBounds(
                    0,
                    0,
                    (it.toBitmap().width * scale).toInt(),
                    (it.toBitmap().height * scale).toInt()
                )

                if (it is GifDrawable) {
                    it.callback = callBack
                    it.setLoopCount(GifDrawable.LOOP_FOREVER)
                    it.start()
                }

                holder.tvContent.text = holder.tvContent.text
            }

            if (finalSource.contains(".gif")) {
                Glide.with(context)
                    .asGif()
                    .load(finalSource)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(object : SimpleTarget<GifDrawable>() {
                        override fun onResourceReady(
                            resource: GifDrawable,
                            transition: Transition<in GifDrawable>?
                        ) {
                            onLoaded.invoke(resource)
                        }
                    })
            } else {
                Glide.with(context)
                    .load(finalSource)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            onLoaded.invoke(resource)
                        }
                    })
            }

            lvlDrw
        }

        holder.tvContent.text =
            Html.fromHtml(itemsViewModel.content, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPostAuthor: TextView = itemView.findViewById(R.id.tvPostAuthor)
        val tvPostNumber: TextView = itemView.findViewById(R.id.tvPostNumber)
        val tvPostTime: TextView = itemView.findViewById(R.id.tvPostTime)
        val tvAuthorTitle: TextView = itemView.findViewById(R.id.tvAuthorTitle)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    private fun getScale(view: View, width: Int): Float {
        if (view.width - 100 < width) {
            return (view.width - 100f) / width
        }

        if (width < 23) {
            return 35f / width
        } else if (width <= 24) {
            return 50f / width
        } else if (width <= 50) {
            return 70f / width
        }

        return 1f
    }
}