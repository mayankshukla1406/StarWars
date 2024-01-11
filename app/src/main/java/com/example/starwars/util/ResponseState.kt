package com.example.starwars.util

sealed class ResponseState<T>(val data :T?=null,val errorMessage:String?=null){
    class Loading<T>(data:T?=null):ResponseState<T>(data)
    class Success<T>(data:T):ResponseState<T>(data)
    class Error<T>(errorMessage:String,data:T?=null):ResponseState<T>(data, errorMessage)
}

