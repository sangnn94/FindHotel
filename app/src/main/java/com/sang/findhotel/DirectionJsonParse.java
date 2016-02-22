package com.sang.findhotel;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sang on 14/12/2015.
 */
public class DirectionJsonParse {

  public List<List<HashMap<String,String>>> parse(JSONObject object){

    List<List<HashMap<String,String>>> routes = new ArrayList<List<HashMap<String, String>>>();
    JSONArray jsonArrayrouts=null;
    JSONArray jsonArraylegs=null;
    JSONObject jsonArraydistance = null;
    JSONObject jsonArrayduration = null;
    JSONArray jsonArraysteps = null;

    try {
      jsonArrayrouts= object.getJSONArray("routes");

      System.out.println("length jsonarray routes: " + jsonArrayrouts.length());
      for (int i = 0;i<jsonArrayrouts.length();i++){

        jsonArraylegs = ((JSONObject) jsonArrayrouts.get(i)).getJSONArray("legs");
        System.out.println("length jsonarray legs: "+jsonArraylegs.length());
        List<HashMap<String,String>> path = new ArrayList<HashMap<String, String>>();
        for (int j=0;j<jsonArraylegs.length();j++){

          // put json distance + add to path arraylist
          jsonArraydistance = ((JSONObject) jsonArraylegs.get(j)).getJSONObject("distance");
          HashMap<String,String> hashMapDistance = new HashMap<String,String>();
          hashMapDistance.put("Distance", jsonArraydistance.getString("text"));
          path.add(hashMapDistance);
          //System.out.println("size list path 1 :  " + path.size());

          // get json duration + add to path arraylist
          jsonArrayduration = ((JSONObject) jsonArraylegs.get(j)).getJSONObject("duration");
          HashMap<String,String> hashMapduration = new HashMap<String,String>();
          hashMapduration.put("Duration", jsonArrayduration.getString("text"));
          path.add(hashMapduration);
          //System.out.println("size list path 2 :  " + path.size());

          //get json steps
          jsonArraysteps = ((JSONObject) jsonArraylegs.get(j)).getJSONArray("steps");
          System.out.println("length jsonarray steps: "+jsonArraysteps.length());
          for (int k = 0; k<jsonArraysteps.length(); k++ ){
            String polyline = (String) ((JSONObject)(((JSONObject) jsonArraysteps.get(k)).get("polyline"))).get("points");
            List<LatLng> listpoint = decodePoly(polyline);
            System.out.println("size list point :  " + listpoint.size());
            for (int l=0;l<listpoint.size();l++){
              HashMap<String,String> hm = new HashMap<String,String>();
              hm.put("lat",Double.toString(((LatLng)listpoint.get(l)).latitude));
              hm.put("lng",Double.toString(((LatLng)listpoint.get(l)).longitude));
              path.add(hm);
              // System.out.println("size list path 3 :  " + path.size());
            }
          }

        }
        routes.add(path);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println("routes size: "+routes.size());

    return routes;
  }

  private List<LatLng> decodePoly(String encoded) {
    List<LatLng> poly = new ArrayList<LatLng>();
    int index = 0, len = encoded.length();
    int lat = 0, lng = 0;
    while (index < len) {
      int b, shift = 0, result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lat += dlat;
      shift = 0;
      result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lng += dlng;
      LatLng p = new LatLng((((double) lat / 1E5)),
              (((double) lng / 1E5)));
      poly.add(p);
    }
    return poly;
  }

}
