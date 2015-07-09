package com.branwenevans.scalaapp

import android.app.Activity
import android.graphics.Bitmap
import android.os.{AsyncTask, Bundle}
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view.View

import scala.collection.mutable

class MainActivity extends Activity {

  val dataset = mutable.MutableList[Weather]()

  val adapter = new ListAdapter(dataset)

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val layoutManager = new LinearLayoutManager(getBaseContext)
    val view = findViewById(R.id.recycler_view).asInstanceOf[RecyclerView]

    view.setElevation(5)
    view.setAdapter(adapter)
    view.setHasFixedSize(true)
    view.setLayoutManager(layoutManager)

    refreshWeather(view);
  }

  def refreshWeather(view: View) {
    new RefreshWeatherDataTask().execute("Christchurch,NZ")
  }

  class RefreshWeatherDataTask extends AsyncTask[AnyRef, AnyRef, AnyRef] {

    //params are AnyRef to get around Scala's overriding vararg method issue: http://issues.scala-lang.org/browse/SI-1459
    override def doInBackground(params: AnyRef*): AnyRef = {
      dataset.clear
      dataset ++= WeatherLoader.getWeather(params.head.asInstanceOf[String])
    }

    override def onPostExecute(result: AnyRef) {
      val view = findViewById(R.id.recycler_view).asInstanceOf[RecyclerView]
      adapter.notifyDataSetChanged()
    }
  }

}



