package com.nvcutu.voz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvcutu.voz.models.ThreadListItemModel
import org.jsoup.nodes.Document
import kotlin.math.max

class ThreadListVm : BaseVm() {
    val titleLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val threadsLiveData: MutableLiveData<List<ThreadListItemModel>> by lazy {
        MutableLiveData<List<ThreadListItemModel>>().apply {
            value = emptyList()
        }
    }

    fun getList() {
        val lst = document
            .getElementsByClass("structItem--thread")
            .map { threadNode ->
                var threadModel = ThreadListItemModel()

                // structItem-cell structItem-cell--icon
                val iconNode = threadNode
                    .getElementsByClass("structItem-cell--icon")[0]
                if (iconNode.getElementsByTag("img").isEmpty()) {
                    val aNode = iconNode.getElementsByTag("a")[0]

                    // background-color: #6666cc; color: #ececf9
                    val colors = aNode.attr("style")
                        .split(";")
                    threadModel.avtBgColor = colors[0].replace("background-color:", "").trim()
                    threadModel.avtTextColor = colors[1].replace("color:", "").trim()
                    threadModel.avtText = iconNode.text()
                } else {
                    threadModel.avtUrl = iconNode.getElementsByTag("img")[0].attr("src")
                }

                // structItem-cell structItem-cell--main
                val mainNode = threadNode.getElementsByClass("structItem-cell--main")[0]

                val titleNode = mainNode.getElementsByClass("structItem-title")[0]
                val aTags = titleNode.getElementsByTag("a")
                if (aTags.count() >= 2) {
                    threadModel.title = aTags[1].text()
                    threadModel.threadUrl = aTags[1].attr("href")
                } else if (aTags.isNotEmpty()) {
                    threadModel.title = aTags[0].text()
                    threadModel.threadUrl = aTags[0].attr("href")
                }

                val minorNode = mainNode.getElementsByClass("structItem-minor")[0]
                val authorTimeNode = minorNode.getElementsByClass("structItem-parts")[0]
                threadModel.author = authorTimeNode.text()

                // structItem-cell structItem-cell--meta
                val metaNode = threadNode.getElementsByClass("structItem-cell--meta")[0]
                val dlNodes = metaNode.getElementsByClass("pairs--justified")
                if (dlNodes.count() >= 2) {
                    threadModel.replies = dlNodes[0].getElementsByTag("dd")[0].text()
                    threadModel.views = dlNodes[1].getElementsByTag("dd")[0].text()
                }

                // structItem-cell structItem-cell--latest
                val latesNode = threadNode.getElementsByClass("structItem-cell--latest")[0]
                val latesTimeNode = latesNode.getElementsByTag("a")[0]
                threadModel.lastTime = latesTimeNode.text()


                // structItem-cell structItem-cell--icon structItem-cell--iconEnd

                threadModel
            }

        threadsLiveData.postValue(lst)
    }

    fun getTitle() {
        val title = document.getElementsByClass("p-title-value")[0].text()
        titleLiveData.postValue(title)
    }

    fun getPage() {
        val blockNodes = document.getElementsByClass("block-outer-main")
        if (blockNodes.isEmpty()) return
        val pageNode = blockNodes[0]
        val currPage = pageNode.getElementsByClass("pageNav-page--current").text().toInt()
        val maxPage = pageNode.getElementsByClass("pageNav-page").last().text().toInt()

        pageLiveData.postValue(Page(currPage, maxPage))
    }
}