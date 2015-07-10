package com.branwenevans.scalaapp

import java.io.InputStream
import java.net.{HttpURLConnection, URL}
import java.text.SimpleDateFormat
import java.util.{TimeZone, Date}

import android.location.Location
import android.text.format.DateUtils
import org.json.JSONObject

import scala.collection.mutable
import scala.io.Source

object WeatherLoader {

  val URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="
  val LAT = "lat="
  val LNG = "&lon="
  val UNITS = "&units=metric"
  val DAYS = "&cnt=7"


  def getWeather(location: Location): mutable.MutableList[Weather] = {
    parseWeatherJson(getWeatherJson(location))
  }

  def getWeatherJson(location: Location): String = {
    val connection = new URL(URL + LAT + location.getLatitude + LNG + location.getLongitude + UNITS + DAYS).openConnection().asInstanceOf[HttpURLConnection]
    var stream = None: Option[InputStream]
    try {
      connection.connect()
      stream = Some(connection.getInputStream)
      Source.fromInputStream(stream.get).mkString
    }
    finally {
      stream.foreach(s => s.close())
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
  val formatter = new SimpleDateFormat("EEEE yyyy-MM-dd")
  formatter.setTimeZone(TimeZone.getDefault)
  val date = if (DateUtils.isToday(timestamp * 1000)) "Today" else formatter.format(new Date(timestamp * 1000))
}