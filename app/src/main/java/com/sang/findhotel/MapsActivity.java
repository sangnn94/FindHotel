package com.sang.findhotel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private Geocoder geocoder;
  private Toolbar mToolbar;
  private List<LatLng> markerPoints;
  private String maddress,name,priceday;
  private String cityname = "HoChiMinh";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    mToolbar  = (Toolbar) findViewById(R.id.tool_bar);
    mToolbar.setTitle("Map");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Map");
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mMap = mapFragment.getMap();
    mMap.setMyLocationEnabled(true);
    mMap.getUiSettings().setZoomControlsEnabled(true);
    mylocation(mMap);
    mapFragment.getMapAsync(this);
    geocoder = new Geocoder(this, Locale.getDefault());
    addHotelList();
    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker marker) {
        routes(marker.getPosition());
      }
    });
    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
      @Override
      public View getInfoWindow(Marker marker) {
        return null;
      }

      @Override
      public View getInfoContents(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.infowindow, null);
        TextView hotelname = (TextView) view.findViewById(R.id.tv_hotelname);
        TextView hotelprice = (TextView) view.findViewById(R.id.tv_hotelprice);

        hotelname.setText(marker.getTitle());
        hotelprice.setText(marker.getSnippet());
        return view;
      }
    });

    mMap.getUiSettings().setMyLocationButtonEnabled(true);
    mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
      @Override
      public boolean onMyLocationButtonClick() {
        mMap.clear();
        mylocation(mMap);
        addHotelList();
        return true;
      }
    });
  }


  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
//    mMap = googleMap;
//
//    // Add a marker in Sydney and move the camera
//    LatLng sydney = new LatLng(-34, 151);
//    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  }

  private void mylocation(GoogleMap map) {
    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    if(location!= null){
      LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
      Geocoder geocoder = new Geocoder(this,Locale.getDefault());
      try {
        List<Address> addressList =  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        cityname = addressList.get(0).getAdminArea();
      } catch (IOException e) {
        e.printStackTrace();
      }
      CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng)
              .zoom(13)
              .bearing(0)
              .tilt(0)
              .build();
      map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

  }

  private void routes(LatLng latLng) {

    markerPoints = new ArrayList<LatLng>();
    if (markerPoints.size() > 1) {
      markerPoints.clear();
    }
    mMap.clear();
    addHotelList();
    Location mylocation = mMap.getMyLocation();
    LatLng mylatlng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
    markerPoints.add(mylatlng);
    markerPoints.add(latLng);
    MarkerOptions options = new MarkerOptions();
    options.position(latLng);
    if (markerPoints.size() == 2) {
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }
    mMap.addMarker(options);
    if (markerPoints.size() == 2) {
      LatLng origin = markerPoints.get(0);
      LatLng dest = markerPoints.get(1);
      String url = getDirectionsUrl(origin, dest);
      DownloadStackDistance downloadTask1 = new DownloadStackDistance(mMap);
      downloadTask1.execute(url);

    }
  }

  private String getDirectionsUrl(LatLng origin,LatLng dest){
    String str_origin = "origin="+origin.latitude+","+origin.longitude;
    String str_dest = "destination="+dest.latitude+","+dest.longitude;
    String sensor = "sensor=false";
    String parameters = str_origin+"&"+str_dest+"&"+sensor;
    String output = "json";
    String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
    return url;
  }
  private void addHotelList() {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        for (ParseObject object : objects) {
          String city =removeAccent(object.getString("City"));
          System.out.println(city);
          String mCity  = removeAccent(cityname);
          System.out.println(mCity);

          if(city.compareToIgnoreCase(mCity)==0) {
            name = object.getString("Name");
            maddress = object.getString("Address");
            priceday = String.valueOf(object.get("PriceDay"));
            SearchLocation(maddress);
          }
        }
      }
    });
  }

  private void SearchLocation(String location){
    if(location==null || location.equals("")){
      return;
    }
    String url = "https://maps.googleapis.com/maps/api/geocode/json?";
    try {
      location = URLEncoder.encode(location, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String address = "address=" + location;
    String sensor = "sensor=false";
    url = url + address + "&" + sensor;
    StackSearchLocation downloadTask = new StackSearchLocation(mMap,name,priceday);
    downloadTask.execute(url);
  }

  private static String removeAccent(String s) {
    s=s.replaceAll(" ","");
    String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(temp).replaceAll("");
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
