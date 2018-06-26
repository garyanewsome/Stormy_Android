package com.teamtreehouse.stormy

import java.text.SimpleDateFormat
import java.util.*

class CurrentWeather {
    var locationLabel: String? = null
    var icon: String? = null
    var time: Long? = null
    var temperature: Double? = null
    var humidity: Double? = null
    var precipChance: Double? = null
    var summary: String? = null
    var timezone: String? = null

    constructor()

    constructor(locationLabel: String?, icon: String?, time: Long?, temperature: Double?, humidity: Double?, precipChance: Double?, summary: String?, timezone: String?) {
        this.locationLabel = locationLabel
        this.icon = icon
        this.time = time
        this.temperature = temperature
        this.humidity = humidity
        this.precipChance = precipChance
        this.summary = summary
        this.timezone = timezone
    }

    fun getFormatedTime(): String? {
        val formatter = SimpleDateFormat("h:mm a")

        formatter.timeZone = TimeZone.getTimeZone(timezone)

        val dateTime = Date(time!! * 1000)

        return formatter.format(dateTime)
    }

    fun getIconId(): Int {
        // clear-day, clear-night, rain, snow, sleet, wind, fog,
        // cloudy, partly-cloudy-day, or partly-cloudy-night

        var iconId: Int = R.drawable.clear_day

        when(icon) {
            "clear-day" ->  iconId = R.drawable.clear_day
            "clear-night" -> iconId = R.drawable.clear_night
            "rain" -> iconId = R.drawable.rain
            "snow" -> iconId = R.drawable.snow
            "sleet" -> iconId = R.drawable.sleet
            "wind" -> iconId = R.drawable.wind
            "fog" -> iconId = R.drawable.fog
            "cloudy" -> iconId = R.drawable.cloudy
            "partly-cloudy" -> iconId = R.drawable.partly_cloudy
            "partly-cloudy-night" -> iconId = R.drawable.cloudy_night
        }

        return iconId
    }
}