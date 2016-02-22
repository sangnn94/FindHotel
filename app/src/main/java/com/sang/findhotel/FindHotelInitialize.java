package com.sang.findhotel;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by sang on 08/12/2015.
 */
public class FindHotelInitialize extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Parse.enableLocalDatastore(this);
    ParseObject.registerSubclass(Hotel.class);
    Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
  }
}
