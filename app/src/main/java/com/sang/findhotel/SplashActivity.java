package com.sang.findhotel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 13/12/2015.
 */
public class SplashActivity extends AppCompatActivity {
  public static final String TAG = "SplashActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new FetchCities().execute();
  }
  private class FetchCities extends AsyncTask<Void, Void, List<City>>{

    @Override
    protected List<City> doInBackground(Void... params) {
      List<City> cities = CityList.get(SplashActivity.this).getCities();
      ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
      try {
        List<ParseObject> objects = query.find();
        Log.i(TAG, objects.toString());
        for(ParseObject object : objects){
          City city = new City();
          city.setName(object.getString("Name"));
          city.setUrl(object.getParseFile("Photo").getUrl());
          cities.add(city);
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return cities;
    }

    @Override
    protected void onPostExecute(List<City> cities) {
      super.onPostExecute(cities);
      Intent intent = new Intent(SplashActivity.this, FindHotelActivity.class);
      startActivity(intent);
      finish();
    }
  }
}
