package com.branwenevans.scalaapp

import android.app.Activity
import android.graphics.Bitmap
import android.os.{AsyncTask, Bundle}
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}

import scala.collection.mutable

class MainActivity extends Activity {

  var dataset = mutable.MutableList[Weather]()

  val adapter = new ListAdapter(dataset)

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val layoutManager = new LinearLayoutManager(getBaseContext)
    val view = findViewById(R.id.recycler_view).asInstanceOf[RecyclerView]

    view.setHasFixedSize(true)
    view.setLayoutManager(layoutManager)
    view.setAdapter(adapter)

    DownloadWeatherDataTask.execute()
  }

  object DownloadWeatherDataTask extends AsyncTask {
    override def doInBackground(params: Nothing*) {
      dataset = WeatherLoader.getWeather("Christchurch,NZ")
    }

    override def onPostExecute(result: Nothing) {
      adapter.notifyDataSetChanged()
    }
  }

}

