package com.nvcutu.voz.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.nvcutu.voz.Current
import com.nvcutu.voz.R
import com.nvcutu.voz.common.ResultState
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document

class LoginDialog(document: Document, context: Context) : Dialog(context) {
    private val doc = document
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_dialog)
    }

    fun login(
        user: String,
        pass: String,
        callBack: (ResultState<Document?, HttpStatusException?>) -> Unit
    ) {
        doc.getElementsByTag("input")
            .firstOrNull { item -> item.attr("name") == "_xfToken" }
            ?.attr("value")
            ?.let {
                Current.post.login(user, pass, it, callBack)
            }
    }
}