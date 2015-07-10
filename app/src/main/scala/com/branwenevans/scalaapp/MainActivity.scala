package com.branwenevans.scalaapp

import android.app.Activity
import android.content.Context
import android.location.{LocationManager, Location}
import android.os.{AsyncTask, Bundle}
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view.{Menu, MenuInflater, MenuItem}
import com.google.android.gms.common.{GooglePlayServicesUtil, ConnectionResult}
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{Builder, ConnectionCallbacks, OnConnectionFailedListener}
import com.google.android.gms.location.LocationServices

import scala.collection.mutable

class MainActivity extends Activity with ConnectionCallbacks with OnConnectionFailedListener {

  val dataset = mutable.MutableList[Weather]()

  val adapter = new WeatherListAdapter(dataset)

  var googleApiClient = None: Option[GoogleApiClient]

  var location = None: Option[Location]

  val defaultLocation: Location = new Location("default")

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    defaultLocation.setLatitude(-43.5358331);
    defaultLocation.setLongitude(172.6054242);

    val layoutManager = new LinearLayoutManager(getBaseContext)
    val view = findViewById(R.id.recycler_view).asInstanceOf[RecyclerView]

    view.setElevation(5)
    view.setAdapter(adapter)
    view.setHasFixedSize(true)
    view.setLayoutManager(layoutManager)

    val result: Int = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
    if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
      GooglePlayServicesUtil.showErrorDialogFragment(result, this, 1)
    }

    googleApiClient = Option(new Builder(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build())
  }


  override def onStart() {
    super.onStart()
    googleApiClient.get.connect()
    refreshWeather()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    val inflater: MenuInflater = getMenuInflater
    inflater.inflate(R.menu.menu_main, menu)
    super.onCreateOptionsMenu(menu)
  }


  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    super.onOptionsItemSelected(item)
    item.getItemId match {
      case R.id.action_location => updateLocation()
    }
  }

  def updateLocation(): Boolean = {
    location = Option(LocationServices.FusedLocationApi.getLastLocation(googleApiClient.get))
    refreshWeather()
    true
  }

  def refreshWeather() {
    new RefreshWeatherDataTask().execute(location.getOrElse(defaultLocation))
  }

  class RefreshWeatherDataTask extends AsyncTask[AnyRef, AnyRef, AnyRef] {

    //params are AnyRef to get around Scala's overriding vararg method issue: http://issues.scala-lang.org/browse/SI-1459
    override def doInBackground(params: AnyRef*): AnyRef = {
      dataset.clear()
      dataset ++= WeatherLoader.getWeather(params.head.asInstanceOf[Location])
    }

    override def onPostExecute(result: AnyRef) {
      adapter.notifyDataSetChanged()
    }
  }

  override def onConnectionSuspended(i: Int) {
    print("connection suspended")
  }

  override def onConnected(bundle: Bundle) {
    location = Option(LocationServices.FusedLocationApi.getLastLocation(googleApiClient.get))
  }

  override def onConnectionFailed(connectionResult: ConnectionResult) {
    print("connection failed")
  }
}



