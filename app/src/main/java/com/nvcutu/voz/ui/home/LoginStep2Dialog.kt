package com.nvcutu.voz.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.nvcutu.voz.Current
import com.nvcutu.voz.R
import com.nvcutu.voz.common.ResultState
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document

class LoginStep2Dialog(document: Document, context: Context) : Dialog(context) {
    private val doc = document
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_dialog_step_2)
    }

    fun loginStep2(
        code: String,
        callBack: (ResultState<Boolean, String>) -> Unit
    ) {
        doc
            .getElementsByTag("input")
            .firstOrNull { it.attr("name") == "_xfToken" }
            ?.let {
                val token = it.attr("value")
                Current.post.loginStep2(token = token, code = code, callBack = callBack)
            }
    }
}