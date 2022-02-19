package com.example.smartparkinglot

import java.lang.Exception

sealed class Result<out T: Any> {
    class Success<out T: Any> (val data: T?, val msg: String) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}