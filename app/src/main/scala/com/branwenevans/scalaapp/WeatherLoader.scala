package com.branwenevans.scalaapp

import java.net.{HttpURLConnection, URL}
import java.util.Date

import org.json.JSONObject

import scala.collection.mutable
import scala.io.Source

object WeatherLoader {

  val URL = "http://api.openweathermap.org/data/2.5/forecast?q="

  val UNITS = "&units=metric"

  def getWeather(location: String): mutable.MutableList[Weather] = {
    parseWeatherJson(getWeatherJson(location))
  }

  def getWeatherJson(location: String): String = {
    val connection = new URL(URL + location + UNITS).openConnection().asInstanceOf[HttpURLConnection]
    try {
      connection.connect()
      Source.fromInputStream(connection.getInputStream).mkString("\n")
    }
    finally {
      connection.disconnect()
    }
  }

  def parseWeatherJson(jsonString: String): mutable.MutableList[Weather] = {

    val ob = new JSONObject(jsonString)

    val weather = mutable.MutableList[Weather]()

    val json = ob.getJSONArray("list")
    for (i <- 0 to ob.getJSONArray("list").length() - 1) {
      val day = json.getJSONObject(i)
      val main = day.getJSONObject("main")
      val weatherNode = day.getJSONArray("weather").getJSONObject(0)
      weather += new Weather(new Date(day.getInt("dt")), weatherNode.getString("main"), weatherNode.getInt("id"), main.getLong("temp"), main.getLong("temp_min"), main.getLong("temp_max"))
    }

    weather
  }
}

class Weather(val date: Date, val description: String, val code: Int, val temp: Long, val minTemp: Long, val maxTemp: Long) {
}