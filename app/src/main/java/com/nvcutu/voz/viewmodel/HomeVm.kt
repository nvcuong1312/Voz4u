package com.nvcutu.voz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvcutu.voz.models.HeaderHomeItemModel
import com.nvcutu.voz.models.HeaderHomeModel
import org.jsoup.nodes.Document

class HomeVm: BaseVm() {
    val headersLiveData: MutableLiveData<List<HeaderHomeModel>> by lazy {
        MutableLiveData<List<HeaderHomeModel>>().apply {
            value = emptyList()
        }
    }

    fun getList() {
        val lst = document.getElementsByClass("block--category")
            .map { groupNode ->
                val headerModel: HeaderHomeModel = HeaderHomeModel()

                // Header
                headerModel.name = groupNode
                    .getElementsByClass("block-header")[0]
                    .text()

                // Box
                headerModel.items = ArrayList()
                groupNode
                    .getElementsByClass("node-body")
                    .forEach { boxNode ->
                        val itemModel = HeaderHomeItemModel()
                        headerModel.items.add(itemModel)

                        itemModel.name = boxNode.getElementsByClass("node-title").text()
                        itemModel.url = boxNode
                            .getElementsByClass("node-title")[0]
                            .getElementsByTag("a")[0]
                            .attr("href")
                        itemModel.totalThread = boxNode
                            .getElementsByClass("pairs--rows")[0]
                            .getElementsByTag("dd")
                            .text()
                        itemModel.totalMessage = boxNode
                            .getElementsByClass("pairs--rows")[1]
                            .getElementsByTag("dd")
                            .text()
                    }

                headerModel
            }

        headersLiveData.postValue(lst)
    }
}