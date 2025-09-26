package com.sgdc.roguelike;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;

class MainViewModel : ViewModel() {

    // splash ready state
    private val _isReady = MutableLiveData(false)
    val isReady: LiveData<Boolean> get() = _isReady

    // navigation state
    private val _currentScreen = MutableLiveData<Screen>(Screen.Menu)
    val currentScreen: LiveData<Screen> get() = _currentScreen

    fun finishLoading() {
        _isReady.value = true
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }
}

enum class Screen {
    Menu, Battle, Settings, Rest, Death
}