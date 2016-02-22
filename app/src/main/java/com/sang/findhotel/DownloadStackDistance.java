package com.sang.findhotel;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sang on 14/12/2015.
 */


public class DownloadStackDistance extends AsyncTask<String,Void,String> {
  private GoogleMap m;
  //private TextView tv;
  String a;
  DownloadStackDistance(GoogleMap map){
    this.m=map;
    //this.tv=textView;
  }
  @Override
  protected String doInBackground(String... params) {
    String data = "";
    try{
      data = downloadUrl(params[0]);
    }catch(Exception e){
    }
    return data;

  }




  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    System.out.println(result);
    ParserTaskDistance parserTask1 = new ParserTaskDistance(m);
    parserTask1.execute(result);
  }
  private String downloadUrl(String strUrl) throws IOException {
    System.out.println(strUrl);
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try{
      URL url = new URL(strUrl);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.connect();
      iStream = urlConnection.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
      StringBuffer sb = new StringBuffer();
      String line = "";
      while( ( line = br.readLine()) != null) {
        sb.append(line);
      }
      data = sb.toString();
      br.close();
    }catch(Exception e){
    }finally{
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }
}