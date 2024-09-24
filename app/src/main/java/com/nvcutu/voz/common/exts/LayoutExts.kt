package com.nvcutu.voz.common.exts

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import java.util.Objects


class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
    }
}

class MyLayout(context: Context) : LinearLayoutManager(context) {
    lateinit var layoutCompletedAction: (RecyclerView.State?, Objects) -> Unit
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)

    }
}