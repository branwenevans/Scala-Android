package com.branwenevans.scalaapp

import java.net.{HttpURLConnection, URL}
import java.util.Date

import org.json.JSONObject

import scala.io.Source

class WeatherLoader {

  val URL = "http://api.openweathermap.org/data/2.5/forecast?q="

  val UNITS = "&units=metric"

  def getWeather(location: String): String = {
    val connection = new URL(URL + location + UNITS).openConnection().asInstanceOf[HttpURLConnection]
    try {
      connection.connect()
      Source.fromInputStream(connection.getInputStream).mkString("\n")
    } finally {
      connection.disconnect()
    }
  }

  def parseWeatherJson(jsonString: String): Weather = {
    val ob = new JSONObject(jsonString)
    val json = ob.getJSONArray("list").getJSONObject(0);
    val main = json.getJSONObject("main");
    val weather = json.getJSONObject("weather");
    new Weather(new Date(json.getInt("dt")), weather.getString("main"), weather.getInt("id"), main.getLong("temp"), main.getLong("temp_min"), main.getLong("temp_max"))
  }
}

class Weather(timestamp: Date, description: String, code: Int, temp: Long, minTemp: Long, maxTemp: Long) {
}