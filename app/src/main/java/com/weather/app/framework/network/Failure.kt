package com.weather.app.framework.network

sealed class Failure {
    object NetworkConnection : Exception()
    object ExceptionUnknown : Exception()
}
