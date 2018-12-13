package com.yoonkim.bestime;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONfunctions {

    public static JSONObject getJSONfromURL(String url){
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        try{
            URL urlHTTP = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlHTTP.openConnection();

            urlConnection.setRequestMethod("GET");
            is = new BufferedInputStream(urlConnection.getInputStream());
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection" + e.toString());
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        }
        catch(Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        try{
            jArray = new JSONObject(result);
        }
        catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return jArray;
    }
}
