package com.weather.app.framework.state

sealed class ScreenState {
    object Loading : ScreenState()
    object Success : ScreenState()
    object Error : ScreenState()
    object Empty : ScreenState()
}
