package com.branwenevans.scalaapp

import android.support.v7.widget.RecyclerView
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.TextView

import scala.collection.mutable

class WeatherListAdapter(dataset: mutable.MutableList[Weather]) extends RecyclerView.Adapter[ViewHolder] {

  val DEGREES: Char = '\u00B0'

  override def getItemCount: Int = dataset.size

  override def onBindViewHolder(vh: ViewHolder, i: Int) {
    val viewHolder = vh.asInstanceOf[ViewHolder]

    val weather: Weather = dataset.get(i).get
    viewHolder.heading.setText(weather.heading)
    viewHolder.description.setText(weather.description)

    viewHolder.date.setText(weather.date)

    viewHolder.temperature.setText(weather.temp.toString + DEGREES)
    viewHolder.high.setText(weather.maxTemp.toString + DEGREES)
    viewHolder.low.setText(weather.minTemp.toString + DEGREES)
  }

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder = {
    new ViewHolder(LayoutInflater.from(viewGroup.getContext).inflate(R.layout.weather_item, viewGroup, false))
  }

}

class ViewHolder(itemView: View) extends RecyclerView.ViewHolder(itemView) {
  val temperature = itemView.findViewById(R.id.weather_temp).asInstanceOf[TextView]
  val heading = itemView.findViewById(R.id.weather_heading).asInstanceOf[TextView]
  val description = itemView.findViewById(R.id.weather_description).asInstanceOf[TextView]
  val high = itemView.findViewById(R.id.weather_high).asInstanceOf[TextView]
  val low = itemView.findViewById(R.id.weather_low).asInstanceOf[TextView]
  val date = itemView.findViewById(R.id.weather_date).asInstanceOf[TextView]
}
