package com.yoonkim.bestime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.City.Airport;
import com.yoonkim.bestime.City.CityHttpClient;
import com.yoonkim.bestime.City.CityJSONParser;
import com.yoonkim.bestime.Map.MapActivity;
import com.yoonkim.bestime.Ticket.HttpClient;
import com.yoonkim.bestime.Ticket.JSONParser;
import com.yoonkim.bestime.Ticket.MonthYearPickerDialog;
import com.yoonkim.bestime.Ticket.Schedule;
import com.yoonkim.bestime.Ticket.viewTicket;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageButton iata;
    private ImageButton ticket;
    private ImageButton room;
    private static TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iata = (ImageButton) findViewById(R.id.iatabutton);
        ticket = (ImageButton)findViewById(R.id.ticketbutton);
        room = (ImageButton)findViewById(R.id.savedbutton);

        iata.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showIATADialog();
            }
        });
        ticket.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTicketDialog();
            }
        });

    }

    private class JSONTask extends AsyncTask<String, Void, List<Schedule>> {
        @Override
        protected List<Schedule> doInBackground(String... params) {
            List<Schedule> scList = new ArrayList<>();
            JSONObject jobj;
            String origin = params[0];
            String destination = params[1];
            String date = params[2];
            jobj = ((new HttpClient()).getData(origin, destination, date));
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
            if(scList.isEmpty()){
                Toast.makeText(MainActivity.this, "Sorry, I could not find any ticket based on your search", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent ticket =new Intent (MainActivity.this, viewTicket.class);
                Bundle myData = new Bundle();
                myData.putSerializable("tickets", (ArrayList<Schedule>)scList);
                ticket.putExtras(myData);
                startActivity(ticket);
            }

        }
    }

    private void showTicketDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.ticket_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        final EditText edt_origin = (EditText) view.findViewById(R.id.edt_origin);

        final EditText edt_destination = (EditText) view.findViewById(R.id.edt_dest);

        tv_date = (TextView) view.findViewById(R.id.tv_date);

        Button btn_setdate = (Button) view.findViewById(R.id.btn_setdate);

        //add listener to button to open datepickerdialog
        btn_setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open datepickerdialog
                showDateDialog();
            }
        });

        dialog_title.setText("Ticket Search");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Search!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(edt_origin.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the departing city", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(edt_destination.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the arriving city", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(tv_date.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please select the departing month", Toast.LENGTH_SHORT).show();
                }
                else {
                    alertDialog.dismiss();
                    JSONTask task = new JSONTask();
                    task.execute(edt_origin.getText().toString(), edt_destination.getText().toString(), tv_date.getText().toString());
                }


            }
        });


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

            if(apList.isEmpty()){
                Toast.makeText(MainActivity.this, "Sorry, I could not find a city based on your search", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent map =new Intent (MainActivity.this, MapActivity.class);
                Bundle myData = new Bundle();

                myData.putSerializable("cities", (ArrayList<Airport>)apList);
                map.putExtras(myData);
                startActivity(map);
            }
        }
    }

    private void showIATADialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.iata_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        final EditText edt_title = (EditText) view.findViewById(R.id.edt_title);


        dialog_title.setText("IATA Search");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( "Search!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(edt_title.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the name of a city", Toast.LENGTH_SHORT).show();
                }

                else {
                    alertDialog.dismiss();
                    CityTask task = new CityTask();
                    task.execute(edt_title.getText().toString());
                }

            }
        });
    }


    public static class MonthPickerListener implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String monthVal, dayVal = "" + day;
            if ((month) < 10) {
                monthVal = "0" + month;
            } else {
                monthVal = "" + month;
            }

            tv_date.setText(year + "-" +  monthVal + "-01");

        }
    }

    private void showDateDialog() {
        MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
        newFragment.setListener(new MonthPickerListener());
        newFragment.show(getSupportFragmentManager(), "MonthYearPickerDialog");
    }

}


