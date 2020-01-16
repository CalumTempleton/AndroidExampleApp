package com.calumtempleton.androidexampleapp

class Location : ILocation {
    private lateinit var location: String

    override fun setLocation(location: String): Boolean {
        this.location = location
        return true
    }
}