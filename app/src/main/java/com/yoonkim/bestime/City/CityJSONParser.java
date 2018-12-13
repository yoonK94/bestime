package com.yoonkim.bestime.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityJSONParser {
    public static List<Airport> getAirport(JSONObject jobj) throws JSONException {
        List<Airport> apList = new ArrayList<>();
        Airport ap = new Airport();

        if(jobj == null){
            return apList;
        }
        JSONArray jArr = getArray("cities", jobj);
        JSONObject jobj2 = getObj(0, jArr);
        ap.setName(getString("nameCity", jobj2));
        ap.setIATA(getString("codeIataCity", jobj2));
        ap.setLat(getDouble("latitudeCity", jobj2));
        ap.setLng(getDouble("longitudeCity", jobj2));
        apList.add(ap);

        JSONArray jArr2 = getArray("airportsByCities", jobj);
        for(int i = 0; i < jArr2.length(); i++) {
            JSONObject jobj3 = getObj(i, jArr2);
            ap = new Airport();
            ap.setName(getString("nameAirport", jobj3));
            ap.setIATA(getString("codeIataAirport", jobj3));
            ap.setLat(getDouble("latitudeAirport", jobj3));
            ap.setLng(getDouble("longitudeAirport", jobj3));
            apList.add(ap);
        }
        return apList;

    }


    private static JSONArray getArray(String tagName, JSONObject jObj) throws JSONException {
        JSONArray subObj = jObj.getJSONArray(tagName);
        return subObj;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static double getDouble(String tagName, JSONObject jObj) throws JSONException {
        return (double) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static boolean getBool(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getBoolean(tagName);
    }

    private static JSONObject getObj(int i, JSONArray jArr) throws  JSONException{
        JSONObject subObj = jArr.getJSONObject(i);
        return subObj;
    }
}
