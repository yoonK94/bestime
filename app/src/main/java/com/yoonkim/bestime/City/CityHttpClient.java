package com.yoonkim.bestime.City;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yoonkim.bestime.JSONfunctions;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class CityHttpClient {
    private static String BASE_URL = "http://aviation-edge.com/v2/public/autocomplete?key=196ef7-4ff748&city=";


    public JSONObject getData(String city) {
        HttpURLConnection con = null;
        InputStream is = null;
        JSONObject jsonObject;
        try{
            String article = BASE_URL + city;
            jsonObject = JSONfunctions.getJSONfromURL(article);
            return jsonObject;
        }
        catch ( Throwable t){
            t.printStackTrace();
        }
        finally {
            try{
                is.close();
            }
            catch(Throwable t){
            }
            try{
                con.disconnect();;
            }
            catch (Throwable t){
            }
        }
        return null;
    }

}
