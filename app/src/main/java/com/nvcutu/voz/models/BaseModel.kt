package com.nvcutu.voz.models

interface BaseModel<T> {
    fun isSame(model: T) : Boolean
}