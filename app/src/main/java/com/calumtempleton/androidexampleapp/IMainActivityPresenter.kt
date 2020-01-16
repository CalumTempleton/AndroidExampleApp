package com.calumtempleton.androidexampleapp

interface IMainActivityPresenter {
    fun getCurrentDay()
    fun getCurrentTime()
    fun setLocationManually(location: String)
}