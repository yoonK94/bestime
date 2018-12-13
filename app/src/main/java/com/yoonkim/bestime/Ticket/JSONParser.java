package com.yoonkim.bestime.Ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    public static List<Schedule> getSchedule(JSONObject jobj, String origin, String destination) throws JSONException {
        List<Schedule> scList = new ArrayList<>();

        if(jobj == null){
            return scList;
        }

        if(getBool("success", jobj)){
            JSONArray data = getArray("data", jobj);
            for(int i = 0; i < data.length(); i++){
                JSONObject jobj2 = getObj(i, data);
                Schedule sc = new Schedule();

                sc.setPrice(getInt("value", jobj2));
                sc.setOrigin(origin);
                sc.setDest(destination);
                sc.setDepart(getString("depart_date", jobj2));
                scList.add(sc);
            }
        }

        return scList;

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

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
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
