package com.branwenevans.scalaapp

import android.support.v7.widget.RecyclerView
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.TextView

import scala.collection.mutable

class ListAdapter(dataset: mutable.MutableList[Weather]) extends RecyclerView.Adapter[ViewHolder] {

  override def getItemCount: Int = dataset.size

  override def onBindViewHolder(vh: ViewHolder, i: Int) {
    val viewHolder = vh.asInstanceOf[ViewHolder]
    val weather: Weather = dataset.get(0).get
    viewHolder.temperature.setText(weather.temp.toString)
    viewHolder.description.setText(weather.description)
    viewHolder.high.setText(weather.maxTemp.toString)
    viewHolder.low.setText(weather.minTemp.toString)
    viewHolder.date.setText(weather.date.toString)
  }

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder = {
    new ViewHolder(LayoutInflater.from(viewGroup.getContext).inflate(R.layout.weather_item, viewGroup, false))
  }

}

class ViewHolder(itemView: View) extends RecyclerView.ViewHolder(itemView) {
  val temperature: TextView = itemView.findViewById(R.id.weather_temp).asInstanceOf[TextView]
  val description: TextView = itemView.findViewById(R.id.weather_description).asInstanceOf[TextView]
  val high: TextView = itemView.findViewById(R.id.weather_high).asInstanceOf[TextView]
  val low: TextView = itemView.findViewById(R.id.weather_low).asInstanceOf[TextView]
  val date: TextView = itemView.findViewById(R.id.weather_date).asInstanceOf[TextView]
}
