package com.sang.findhotel;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADMIN on 20-Dec-15.
 */
public class GeocodeJSONParser {


    public List<HashMap<String, String>> parse(JSONObject object){

        List<HashMap<String, String>> latlng_location = new ArrayList<>();
        JSONArray rs= null;
        JSONArray address_components= null;
        JSONObject geometry = null;

        try {
            rs = object.getJSONArray("results");
            for (int i=0;i<rs.length();i++){
                address_components = ((JSONObject)rs.get(i)).getJSONArray("address_components");
                geometry = ((JSONObject) rs.get(i)).getJSONObject("geometry");
                HashMap<String, String> hm =new HashMap<>();
                Double latdouble = (Double) ((JSONObject)geometry.get("location")).get("lat");
                Double lngdouble = (Double) ((JSONObject)geometry.get("location")).get("lng");
                hm.put("lat", latdouble.toString());
                hm.put("lng", lngdouble.toString());
                latlng_location.add(hm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latlng_location;

    }

}
