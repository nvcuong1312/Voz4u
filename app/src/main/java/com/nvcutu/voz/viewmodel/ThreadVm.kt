package com.nvcutu.voz.viewmodel

import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.models.ThreadItemModel
import com.nvcutu.voz.models.ThreadListItemModel
import org.jsoup.nodes.Document
import kotlin.math.max

class ThreadVm : BaseVm() {
    val titleLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val threadsLiveData: MutableLiveData<List<ThreadItemModel>> by lazy {
        MutableLiveData<List<ThreadItemModel>>().apply {
            value = emptyList()
        }
    }

    fun getList() {
        val lst = document
            .getElementsByClass("block block--messages")
            .firstOrNull()
            ?.getElementsByClass("message--post")
            ?.filter { item -> item.tagName() == "article" }
            ?.map { postNode ->
                val threadItem = ThreadItemModel()

                // message-cell message-cell--user
                val userNode = postNode.getElementsByClass("message-cell--user")[0]
                val userDetailNode = userNode.getElementsByClass("message-userDetails")[0]
                threadItem.author = userDetailNode.getElementsByClass("message-name")[0].text()
                threadItem.authorTitle =
                    userDetailNode.getElementsByClass("message-userTitle")[0].text()


                // message-cell message-cell--main
                val contentNode = postNode.getElementsByClass("message-cell--main")[0]
                val headerNode = contentNode.getElementsByClass("message-attribution--split")[0]
                threadItem.postTime =
                    headerNode.getElementsByClass("message-attribution-main")[0].text()
                threadItem.postNumber = headerNode
                    .getElementsByClass("message-attribution-opposite")[0]
                    .getElementsByTag("li").lastOrNull()?.text() ?: ""

                val msgNode = contentNode.getElementsByClass("message-content")[0]

                msgNode
                    .getElementsByTag("script")
                    .remove()

                msgNode
                    .getElementsByClass("bbImageWrapper")
                    .forEach { bbNode ->
                        if (bbNode.getElementsByTag("img").any {
                                it.attr("width").isEmpty()
                            }) {
                            bbNode.unwrap()
                        }
                    }

                threadItem.content = msgNode
                    .getElementsByClass("message-userContent")[0]
                    .getElementsByClass("bbWrapper")[0].html()

                threadItem
            }

        lst?.let { threadsLiveData.postValue(it) }
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