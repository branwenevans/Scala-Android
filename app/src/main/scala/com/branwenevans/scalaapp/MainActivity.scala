package com.branwenevans.scalaapp;

import android.app.Activity
import android.support.v7.widget.{GridLayoutManager, LinearLayoutManager, RecyclerView}
import android.view.View
import android.widget.TextView
import android.os.Bundle

import scala.collection.mutable

class MainActivity extends Activity {

  val dataset = mutable.MutableList("1","2","3","4")

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val layoutManager = new GridLayoutManager(getBaseContext, 2)
    val adapter = new CountdownAdapter(dataset)

    val view = findViewById(R.id.recycler_view).asInstanceOf[RecyclerView]
    view.setHasFixedSize(true)
    view.setLayoutManager(layoutManager)
    view.setAdapter(adapter)
  }
}