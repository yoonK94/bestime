package com.yoonkim.bestime;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yoonkim.bestime.City.Airport;
import com.yoonkim.bestime.City.CityHttpClient;
import com.yoonkim.bestime.City.CityJSONParser;
import com.yoonkim.bestime.Map.MapActivity;
import com.yoonkim.bestime.Ticket.HttpClient;
import com.yoonkim.bestime.Ticket.JSONParser;
import com.yoonkim.bestime.Ticket.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private Button submit;
    private Button map;
    private Button airport;
    private List<Airport> airportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView) findViewById(R.id.word);
        submit = (Button) findViewById(R.id.button);
        map = (Button)findViewById(R.id.button2);
        airport = (Button)findViewById(R.id.button3);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startTask(v);
            }
        });

        map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent map =new Intent (MainActivity.this, MapActivity.class);
                Bundle myData = new Bundle();

                myData.putSerializable("city", (ArrayList<Airport>)airportList);
                map.putExtras(myData);
                startActivity(map);
            }
        });
        airport.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startTask2(v);
            }
        });
    }
    public void startTask(View view){
        JSONTask task = new JSONTask();
        task.execute("BOS", "NYC");
    }
    public void startTask2(View view){
        CityTask task = new CityTask();
        task.execute("new york");
    }

    private class JSONTask extends AsyncTask<String, Void, List<Schedule>> {
        @Override
        protected List<Schedule> doInBackground(String... params) {
            List<Schedule> scList = new ArrayList<>();
            JSONObject jobj;
            String origin = params[0];
            String destination = params[1];
            jobj = ((new HttpClient()).getData(origin, destination));
            try {
                scList = JSONParser.getSchedule(jobj, origin, destination);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return scList;

        }

        @Override
        protected void onPostExecute(List<Schedule> scList){
            super.onPostExecute(scList);
            // create an Object for Adapter
            if(scList.isEmpty()){show.setText("yay");}
            else{
                String s = "";
                for(int i = 0; i < scList.size();i++){
                    s += scList.get(i).getDepart() + "\n";
                }
                show.setText(s);
            }


//            mAdapter = new CardViewDataAdapter(synList);
//            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();

        }
    }

    private class CityTask extends AsyncTask<String, Void, List<Airport>> {
        @Override
        protected List<Airport> doInBackground(String... params) {
            List<Airport> apList = new ArrayList<>();
            JSONObject jobj;
            String origin = params[0];
            jobj = ((new CityHttpClient()).getData(origin));
            try {
                apList = CityJSONParser.getAirport(jobj);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return apList;

        }

        @Override
        protected void onPostExecute(List<Airport> apList){
            super.onPostExecute(apList);
            airportList = apList;
            // create an Object for Adapter

            if(airportList.isEmpty()){show.setText("yay");}
            else{
                Intent map =new Intent (MainActivity.this, MapActivity.class);
                Bundle myData = new Bundle();

                myData.putSerializable("cities", (ArrayList<Airport>)airportList);
                map.putExtras(myData);
                startActivity(map);
            }


//            mAdapter = new CardViewDataAdapter(synList);
//            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();

        }
    }

}


