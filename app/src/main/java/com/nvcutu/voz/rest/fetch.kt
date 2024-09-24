package com.nvcutu.voz.rest

import android.net.DnsResolver
import android.os.AsyncTask
import android.os.Looper
import com.nvcutu.voz.Current
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.common.ResultState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.dnsoverhttps.DnsOverHttps
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.InetAddress

class Fetch {
    @OptIn(DelicateCoroutinesApi::class)
    fun getHtml(url: String, callBack: (ResultState<Document?, HttpStatusException?>) -> Unit) {
        GlobalScope.launch {
            val rq = Request
                .Builder()
                .url(url)
                .header("User-Agent", Resource.USER_AGENT)
                .header("Connection", "keep-alive")
                .build()

            Current.client.newCall(rq).execute().use {
                it.body?.string()?.let { str ->
                    callBack(ResultState(Jsoup.parse(str)))
                }
            }
        }
    }
}