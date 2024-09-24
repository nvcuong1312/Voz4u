package com.nvcutu.voz.common

class ResultState<R, E>(
    private val d: R,
    private val e: E? = null
) {
    var result = d
    var error = e
}