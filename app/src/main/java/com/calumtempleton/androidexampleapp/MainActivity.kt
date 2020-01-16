package com.calumtempleton.androidexampleapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var presenter: IMainActivityPresenter
    private lateinit var location: ILocation

    private val view = object : MainActivityPresenter.IView {
        override fun notifyDay(currentDay: String) {
            current_day.text = currentDay
        }

        override fun notifyTime(currentTime: String) {
            current_time.text = currentTime
        }

        override fun notifyLocation(location: String, success: Boolean) {
            displayMessage("Your location has been set to $location: $success")
            current_location.text = location
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        location = Location()
        presenter = MainActivityPresenter(view, location)

        presenter.getCurrentDay()
        presenter.getCurrentTime()

        set_location.setOnClickListener {
            presenter.setLocationManually(set_location_et.text.toString())
        }
    }

    private fun displayMessage(message: String) {
        AlertDialog.Builder(
            ContextThemeWrapper(
                this,
                R.style.AppTheme
            )
        )
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
