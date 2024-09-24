package com.nvcutu.voz.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvcutu.voz.MainActivity
import com.nvcutu.voz.adapters.BottomBarPageItemAdapter
import com.nvcutu.voz.common.exts.CenterSmoothScroller
import com.nvcutu.voz.common.exts.MyLayout
import com.nvcutu.voz.databinding.BottomBarMainBinding
import com.nvcutu.voz.models.BottomBarPageItem
import com.nvcutu.voz.viewmodel.BaseVm

class BottomBar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var binding: BottomBarMainBinding
    private var arrayList = arrayListOf<BottomBarPageItem>()
    private var pageAdapter = BottomBarPageItemAdapter((arrayList))

    var onPageItemClick: ((Int) -> Unit)? = null
    var onFirstPageClick: (() -> Unit)? = null
    var onLastPageClick: (() -> Unit)? = null

    init { // inflate binding and add as view
        binding = BottomBarMainBinding.inflate(LayoutInflater.from(context), this, false)

        binding.rcyBottomBarPage.adapter = pageAdapter.apply {
            onItemClick = {
                onPageItemClick?.invoke(it.page)
            }
        }

        binding.btnBottomBarFirst.setOnClickListener {
            onFirstPageClick?.invoke()
        }

        binding.btnBottomBarLast.setOnClickListener {
            onLastPageClick?.invoke()
        }

        binding.cvBottomBar.setOnClickListener {
            (context as MainActivity).openCloseNavigationDrawer(it)
        }
        addView(binding.root)
    }

    private var preState: Parcelable? = null

    fun setPage(page: BaseVm.Page) {
        context.run {

            for (i in 1..page.maxPage) {
                val btItem = BottomBarPageItem()
                btItem.page = i
                btItem.isSelected = i == page.currentPage

                val idx = arrayList.indexOfFirst { it.page == i }
                if (idx == -1) {
                    arrayList.add(i - 1, btItem)
                    pageAdapter.notifyItemInserted(i - 1)
                } else {
                    if (!arrayList[i - 1].isSame(btItem)) {
                        arrayList[i - 1] = btItem
                        pageAdapter.notifyItemChanged(i - 1)
                    }
                }
            }

            if (page.currentPage == 1 || page.currentPage == page.maxPage) {
                binding.rcyBottomBarPage.scrollToPosition(page.currentPage - 1)
            }

//            Looper.myLooper()?.let {
//                Handler(it).postDelayed({
//                    val sm = CenterSmoothScroller(context)
//                    sm.targetPosition = page.currentPage - 1
//                    binding.rcyBottomBarPage.layoutManager?.startSmoothScroll(sm)
//                }, 500)
//            }
        }
    }
}