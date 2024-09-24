package com.nvcutu.voz.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nvcutu.voz.R
import org.jsoup.HttpStatusException

open class BaseFragment : Fragment() {
    fun setTitle(title: String) {
        (activity as AppCompatActivity?)?.supportActionBar?.title = title
    }
}