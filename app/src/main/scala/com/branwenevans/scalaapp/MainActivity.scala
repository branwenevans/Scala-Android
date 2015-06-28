package com.branwenevans.scalaapp;

import android.app.Activity
import android.widget.TextView
import android.os.Bundle

class MainActivity extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}