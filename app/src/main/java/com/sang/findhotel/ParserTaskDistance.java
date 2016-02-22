package com.sang.findhotel;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sang on 14/12/2015.
 */
public class ParserTaskDistance extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {
  private GoogleMap map;
  // TextView textView;
  private Polyline polyline;
  ParserTaskDistance(GoogleMap m){
    this.map=m;
    //this.textView=tv;

  }
  @Override
  protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
    JSONObject jObject;
    List<List<HashMap<String, String>>> routes = null;
    try{
      jObject = new JSONObject(jsonData[0]);
      DirectionJsonParse parser = new DirectionJsonParse();
      routes = parser.parse(jObject);
    }catch(Exception e){
      e.printStackTrace();
    }

    return routes;
  }
  @Override
  protected void onPostExecute(List<List<HashMap<String, String>>> result) {
    System.out.println("result size in onPostExcute"+ result.size());
    ArrayList<LatLng> points = null;
    PolylineOptions lineOptions = null;

    //MarkerOptions markerOptions = new MarkerOptions();

    String distance = "";
    String duration = "";
    if(result.size()<1){
      return;
    }
    for(int i=0;i<result.size();i++){
      points = new ArrayList<LatLng>();
      lineOptions = new PolylineOptions();
      List<HashMap<String, String>> path = result.get(i);
      System.out.println(path.size());
      for(int j=0;j<path.size();j++){
        HashMap<String,String> point = path.get(j);
        if(j==0){
          distance = (String)point.get("Distance");
          continue;
        }else if(j==1){
          duration = (String)point.get("Duration");
          continue;
        }
        double lat = Double.parseDouble(point.get("lat"));
        double lng = Double.parseDouble(point.get("lng"));
        LatLng position = new LatLng(lat, lng);
        points.add(position);
        //lineOptions.add(position);

      }

      lineOptions.addAll(points);
      lineOptions.width(10);
      lineOptions.color(Color.BLUE);
      System.out.println("points in polyline option"+lineOptions.getPoints());

    }

    polyline= map.addPolyline(lineOptions);
    System.out.println("finish");
  }
}