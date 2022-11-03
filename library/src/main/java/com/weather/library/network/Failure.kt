package com.weather.library.network

sealed class Failure {
    object NetworkConnection : Exception()
}
