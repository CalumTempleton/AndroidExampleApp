package com.calumtempleton.androidexampleapp

import java.time.LocalDate
import java.time.LocalTime

// Side note - this class uses java.time, the only date/timing classes that should be used
class MainActivityPresenter(private val view: IView, private val location: ILocation) :
    IMainActivityPresenter {

    override fun getCurrentDay() {
        val currentDay = LocalDate.now().dayOfWeek
        view.notifyDay(currentDay.toString())
    }

    override fun getCurrentTime() {
        val currentTime = LocalTime.now()
        view.notifyTime(currentTime.toString())
    }

    override fun setLocationManually(location: String) {
        val result = this.location.setLocation(location)
        view.notifyLocation(location, result)
    }

    interface IView {
        fun notifyDay(currentDay: String)
        fun notifyTime(currentTime: String)
        fun notifyLocation(location: String, success: Boolean)
    }
}