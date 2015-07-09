package com.branwenevans.scalaapp

import java.io.InputStream
import java.net.{HttpURLConnection, URL}
import java.text.SimpleDateFormat
import java.util.Date

import org.json.JSONObject

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
    try {
      connection.connect()
      val stream: InputStream = connection.getInputStream
      val string: String = Source.fromInputStream(stream).mkString
      stream.close()
      return string
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
      val temp = day.getJSONObject("temp")
      val weatherNode = day.getJSONArray("weather").getJSONObject(0)
      weather += new Weather(day.getLong("dt"), weatherNode.getString("main"), weatherNode.getString("description"), weatherNode.getInt("id"), temp.getLong("day"), temp.getLong("min"), temp.getLong("max"))
    }

    weather
  }
}

class Weather(private val timestamp: Long, val heading: String, val description: String, val code: Int, val temp: Long, val minTemp: Long, val maxTemp: Long) {
  val date: String = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp * 1000))
}