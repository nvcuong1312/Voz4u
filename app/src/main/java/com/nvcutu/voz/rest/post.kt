package com.nvcutu.voz.rest

import com.nvcutu.voz.Current
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.common.ResultState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttp
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class Post {

    @OptIn(DelicateCoroutinesApi::class)
    fun login(
        userName: String,
        pw: String,
        token: String,
        callBack: (ResultState<Document?, HttpStatusException?>) -> Unit
    ) {
        GlobalScope.launch {
            val form = FormBody
                .Builder()
                .add("_xfToken", token)
                .add("login", userName)
                .add("password", pw)
                .add("remember", "1")

                .build()

            val rq = Request
                .Builder()
                .post(form)
                .url("https://voz.vn/login/login")
                .header("Connection", "keep-alive")
                .build()

            Current.client.newCall(rq).execute().use { response ->
                if (!response.isSuccessful) {
                    callBack.invoke(ResultState(null, null))
                    return@use
                }

                response.body?.string()?.let { str ->
                    callBack(ResultState(Jsoup.parse(str)))
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loginStep2(
        token: String,
        code: String,
        trust: String = "1",
        confirm: String = "1",
        provider: String = "email",
        remember: String = "1",
        xfRedirect: String = "https://voz.vn/",
        xfRequestUri: String = "/login/two-step?_xfRedirect=https%3A%2F%2Fvoz.vn%2F&remember=1",
        xfWithData: String = "1",
        xfResponseType: String = "json",
        callBack: (ResultState<Boolean, String>) -> Unit
    ) {
        GlobalScope.launch {
            val form = MultipartBody
                .Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("_xfToken", token)
                .addFormDataPart("code", code)
                .addFormDataPart("trust", trust)
                .addFormDataPart("confirm", confirm)
                .addFormDataPart("provider", provider)
                .addFormDataPart("remember", remember)
                .addFormDataPart("_xfRedirect", xfRedirect)
                .addFormDataPart("_xfRequestUri", xfRequestUri)
                .addFormDataPart("_xfWithData", xfWithData)
                .addFormDataPart("_xfResponseType", xfResponseType)
                .build()

            val rq = Request
                .Builder()
                .post(form)
                .url("https://voz.vn/login/two-step")
                .header("Connection", "keep-alive")
                .build()

            Current.client.newCall(rq).execute().use { response ->
                if (!response.isSuccessful) {
                    callBack.invoke(ResultState(false, "Error"))
                    return@use
                }

                callBack.invoke(ResultState(true, ""))
            }
        }
    }
}