package com.yoonkim.bestime.Ticket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yoonkim.bestime.JSONfunctions;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {
    private static String BASE_URL = "http://api.travelpayouts.com/v2/prices/latest?currency=usd&period_type=year&page=1&limit=30&show_to_affiliates=true&sorting=price&one_way=true&trip_class=0&origin=";
    private static String middle =        "&destination=";
    private static String token = "&token=32d16457c43ca99e02eb8d017aa61bdf";

    public JSONObject getData(String depart, String dest) {
        HttpURLConnection con = null;
        InputStream is = null;
        JSONObject jsonObject;
        try{
            String article = BASE_URL + depart + middle + dest + token;
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

    public Bitmap getImage(String code) {
        InputStream in = null;
        int resCode = -1;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(code);
            URLConnection urlConn = url.openConnection();
            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            //HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }

            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Throwable t) {
            }
            try {
                httpConn.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }
}
