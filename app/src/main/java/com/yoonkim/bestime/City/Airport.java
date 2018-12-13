package com.yoonkim.bestime.City;

import java.io.Serializable;

public class Airport implements Serializable {
    private String name;
    private String IATA;
    private double lat;
    private double lng;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getIATA(){
        return IATA;
    }
    public void setIATA(String IATA){
        this.IATA = IATA;
    }

    public double getLat(){
        return lat;
    }
    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLng(){
        return lng;
    }
    public void setLng(double lng){
        this.lng = lng;
    }

}
