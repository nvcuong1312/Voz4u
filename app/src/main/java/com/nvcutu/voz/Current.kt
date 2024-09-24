package com.nvcutu.voz

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.models.CookieModel
import com.nvcutu.voz.rest.Fetch
import com.nvcutu.voz.rest.Post
import okhttp3.CookieJar
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.CookieManager
import java.net.CookieStore
import java.net.HttpCookie
import java.net.InetAddress
import java.net.URI

object Current {
    val fetch: Fetch = Fetch()
    val post: Post = Post()
    private var _client = OkHttpClient()
    private val dns = DnsOverHttps.Builder().client(_client)
        .url("https://dns.google/dns-query".toHttpUrl())
        .bootstrapDnsHosts(InetAddress.getByName("8.8.8.8"), InetAddress.getByName("8.8.4.4"))
        .build()

    val cookieManager = CookieManager()

    var client: OkHttpClient
        get() = _client
        private set(value) {
            _client = value
        }

    fun setClient(activity: Activity) {
        val shared = activity.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val cookieJs = shared.getString("cookie", "")
        if (!cookieJs.isNullOrEmpty()) {
            val type: TypeToken<List<CookieModel>> = object : TypeToken<List<CookieModel>>() {}
            val cookieLocal = Gson().fromJson(cookieJs, type)
            cookieLocal.forEach { c ->
                cookieManager.cookieStore.add(URI(Resource.URL_BASE), HttpCookie(c.name, c.value))
            }
        }

        _client = _client
            .newBuilder()
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()
    }
}