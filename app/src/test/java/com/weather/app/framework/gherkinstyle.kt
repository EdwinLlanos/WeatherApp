package com.weather.app.framework

open class GherkinStyle {
    fun given(func: () -> Unit) = func()
    fun execute(func: () -> Unit) = func()
    fun then(func: () -> Unit) = func()
}

fun scenario(name: String, scenario: GherkinStyle.() -> Unit) {
    GherkinStyle().scenario()
}
