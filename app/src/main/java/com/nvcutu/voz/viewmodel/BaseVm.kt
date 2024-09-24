package com.nvcutu.voz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvcutu.voz.models.HeaderHomeItemModel
import com.nvcutu.voz.models.HeaderHomeModel
import org.jsoup.nodes.Document

open class BaseVm: ViewModel() {
    var url = ""
    lateinit var document: Document

    class Page(curr: Int, max: Int) {
        var currentPage = curr
        var maxPage = max
    }
    val pageLiveData: MutableLiveData<Page> by lazy {
        MutableLiveData<Page>()
    }
}