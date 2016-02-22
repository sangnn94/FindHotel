package com.sang.findhotel;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 16/12/2015.
 */
@ParseClassName("Hotel")

public class Hotel extends ParseObject {
  public Hotel(){}
  public String getName(){
    return getString("Name");
  }
  public void setName(String name){
    put("Name", name);
  }
  public String getCity(){
    return getString("City");
  }
  public void setCity(String city){
    put("City", city);
  }
  public String getAddress(){
    return getString("Address");
  }
  public void setAddress(String address){
    put("Address", address);
  }

  public Number getPhone(){
    return getNumber("Phone");
  }

  public void setPhone(Number phone){
    put("Phone", phone);
  }

  public Number getPriceDay(){
    return getNumber("PriceDay");
  }

  public void setPriceDay(Number priceDay){
    put("PriceDay", priceDay);
  }

  public Number getPriceHour(){
    return getNumber("PriceHour");
  }

  public void setPriceHour(Number priceHour){
    put("PriceHour", priceHour);
  }

  public List<ParseFile> getPhoto(){
    return getList("Photo");
  }
  public void setPhoto(List<ParseFile> list){
    put("Photo", list);
  }

  public void setId(String id){
    put("objectId", id);
  }
  public String getId(){
    return getString("objectId");
  }
}
