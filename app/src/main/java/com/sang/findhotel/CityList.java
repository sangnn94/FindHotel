package com.sang.findhotel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 15/12/2015.
 */
/*Singleton to fetch name + url of city list in splash screen*/
public class CityList {
  private List<City> mCities;
  private static CityList sCityList;
  private CityList(Context context){
    mCities = new ArrayList<>();
  }
  public static CityList get(Context context){
    if(sCityList == null)
      sCityList = new CityList(context.getApplicationContext());
    return sCityList;
  }
  public List<City> getCities(){
    return mCities;
  }
}
