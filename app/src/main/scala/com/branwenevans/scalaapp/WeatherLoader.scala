package com.branwenevans.scalaapp

import java.io.InputStream
import java.net.{HttpURLConnection, URL}
import java.text.SimpleDateFormat
import java.util.Date

import org.json.{JSONArray, JSONObject}

import scala.collection.mutable
import scala.io.Source

object WeatherLoader {

  val URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="

  val UNITS = "&units=metric"

  val DAYS = "&cnt=7"

  def getWeather(location: String): mutable.MutableList[Weather] = {
    parseWeatherJson(getWeatherJson(location))
  }

  def getWeatherJson(location: String): String = {
    val connection = new URL(URL + location + UNITS + DAYS).openConnection().asInstanceOf[HttpURLConnection]
    var stream: InputStream = null
    try {
      connection.connect()
      stream = connection.getInputStream
      Source.fromInputStream(stream).mkString
    }
    finally {
      stream.close()
      connection.disconnect()
    }
  }

  def parseWeatherJson(jsonString: String): mutable.MutableList[Weather] = {
    val weather = mutable.MutableList[Weather]()
    val json = new JSONObject(jsonString).getJSONArray("list")
    for (i <- 0 to json.length() - 1) {
      weather += createWeather(json.getJSONObject(i))
    }

    weather
  }

  def createWeather(day: JSONObject): Weather = {
    val temp = day.getJSONObject("temp")
    val weatherNode = day.getJSONArray("weather").getJSONObject(0)
    new Weather(day.getLong("dt"), weatherNode.getString("main"), weatherNode.getString("description"), weatherNode.getInt("id"), temp.getLong("day"), temp.getLong("min"), temp.getLong("max"))
  }
}

class Weather(private val timestamp: Long, val heading: String, val description: String, val code: Int, val temp: Long, val minTemp: Long, val maxTemp: Long) {
  val date: String = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp * 1000))
}