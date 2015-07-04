package com.branwenevans.scalaapp

import java.net.{HttpURLConnection, URL}

import org.json.JSONObject

import scala.io.Source

class Weather {

  val URL: String = "http://api.openweathermap.org/data/2.5/forecast?q="

  def getWeather(location: String): String = {
    val connection = new URL(URL + location).openConnection().asInstanceOf[HttpURLConnection]
    try {
      connection.connect()
      Source.fromInputStream(connection.getInputStream).mkString("\n")
    } finally {
      connection.disconnect()
    }
  }

}
