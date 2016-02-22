package com.sang.findhotel;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADMIN on 20-Dec-15.
 */
public class StackSearchLocation extends AsyncTask<String,Integer,String> {
    private String data="";
    private GoogleMap mMap;
    private String namehotel,priceday;
    private JSONObject jsonObject;
    public StackSearchLocation(GoogleMap map, String namehotel,String priceday){
       this.mMap= map;
        this.namehotel= namehotel;
        this.priceday=priceday;
    }
    @Override
    protected String doInBackground(String... url) {
        try{
            data= downloadUrl(url[0]);
        }catch(Exception e){
        }
        if(data.equals("{   \"results\" : [],   \"status\" : \"ZERO_RESULTS\"}"))
            return null;
        return data;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        System.out.println(strUrl);
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    @Override
    protected void onPostExecute(String result) {
        if(result!=null) {
            addMarkerHotel(ParserTaskSearchLocation(result));

        }

    }

    private List<HashMap<String, String>>  ParserTaskSearchLocation(String result){
        List<HashMap<String, String>> places = null;
        GeocodeJSONParser parser = new GeocodeJSONParser();
        try {
            jsonObject = new JSONObject(result);
            places = parser.parse(jsonObject);
        } catch (Exception e) {
        }
        return places;

    }
    private void addMarkerHotel(List<HashMap<String, String>> list) {
        LatLng latLng_hotel_loction = new LatLng(Double.valueOf(list.get(0).get("lat")),Double.valueOf(list.get(0).get("lng")));
        MarkerOptions mkoption = new MarkerOptions();
        mkoption.position(latLng_hotel_loction).title(namehotel).snippet(priceday + "đ");
        mkoption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        Marker marker = mMap.addMarker(mkoption);
        marker.showInfoWindow();

    }




}
