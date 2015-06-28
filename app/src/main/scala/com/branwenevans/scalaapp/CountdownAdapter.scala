package com.branwenevans.scalaapp

import android.support.v7.widget.RecyclerView
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.TextView

import scala.collection.mutable

class CountdownAdapter(dataset: mutable.MutableList[String]) extends RecyclerView.Adapter[ViewHolder] {

  override def getItemCount: Int = dataset.size

  override def onBindViewHolder(vh: ViewHolder, i: Int) {
    val viewHolder = vh.asInstanceOf[ViewHolder]
    viewHolder.textView.setText(dataset.get(i).get)
  }

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder = {
    new ViewHolder(LayoutInflater.from(viewGroup.getContext).inflate(R.layout.grid_item, viewGroup, false))
  }

}

class ViewHolder(itemView: View) extends RecyclerView.ViewHolder(itemView) {
  val textView: TextView = itemView.findViewById(R.id.grid_item).asInstanceOf[TextView]
}
