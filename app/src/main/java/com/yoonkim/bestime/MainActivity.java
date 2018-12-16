package com.yoonkim.bestime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.City.Airport;
import com.yoonkim.bestime.City.CityHttpClient;
import com.yoonkim.bestime.City.CityJSONParser;
import com.yoonkim.bestime.Map.MapActivity;
import com.yoonkim.bestime.Room.DatabaseActivity;
import com.yoonkim.bestime.Room.OnDbClickListener;
import com.yoonkim.bestime.Room.SavedTicket;
import com.yoonkim.bestime.Room.SavedTicketDatabase;
import com.yoonkim.bestime.Room.databaseAdapter;
import com.yoonkim.bestime.Ticket.ticketGroup;
import com.yoonkim.bestime.Ticket.HttpClient;
import com.yoonkim.bestime.Ticket.JSONParser;
import com.yoonkim.bestime.Ticket.MonthYearPickerDialog;
import com.yoonkim.bestime.Ticket.Schedule;
import com.yoonkim.bestime.Ticket.TicketActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDbClickListener {

    private ImageButton iata;
    private ImageButton ticket;
    private ImageButton room;
    private static TextView tv_date;
    private List<SavedTicket> stList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iata = (ImageButton) findViewById(R.id.iatabutton);
        ticket = (ImageButton)findViewById(R.id.ticketbutton);
        room = (ImageButton)findViewById(R.id.savedbutton);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setImageResource(R.drawable.instructionbutton30dp);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showInstructionDialog();
            }
        });
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
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatabaseAsync().execute();
            }
        });

    }

    private void showInstructionDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_instruction, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        TextView dialog_body = (TextView) view.findViewById(R.id.dialog_main);

        dialog_title.setText("  Welcome");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton("Got it",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

    }
    /*
        A dialog to search desired city
     */
    private void showIATADialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_iata, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        final EditText edt_title = (EditText) view.findViewById(R.id.edt_title);


        dialog_title.setText("  IATA Search");


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

    /*
        AsyncTask to lookup nearby airports of the city from aviation-edge.com and show in Google Maps
     */

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



    /*
        A dialog to pick the origin, destination, and desired departure month
     */

    private void showTicketDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_ticket, null);

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

        dialog_title.setText("  Ticket Search");

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

    /*
        AsyncTask to search the purchased tickets from travelpayouts.com within the recent 48 hours
     */

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
                Intent ticket =new Intent (MainActivity.this, TicketActivity.class);
                Bundle myData = new Bundle();
                myData.putSerializable("tickets", (ArrayList<Schedule>)scList);
                ticket.putExtras(myData);
                startActivity(ticket);
            }
        }
    }

    private void showDatabaseDialog(List<ticketGroup> tgList) {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_database, null);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_events);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);
        OnDbClickListener dbClickListener = (OnDbClickListener) new MainActivity();
        databaseAdapter adapter = new databaseAdapter(tgList, getApplicationContext(), dbClickListener);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        dialog_title.setText("  Saved Tickets");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();



        alertDialog.show();
    }

    private class DatabaseAsync extends AsyncTask<Object, Void, List<SavedTicket>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<SavedTicket> doInBackground(Object... params) {
            List<SavedTicket> tickets;
            tickets = SavedTicketDatabase.getSavedTicketDatabase(MainActivity.this).savedTicketDao().getSavedTickets();
            return tickets;
        }

        @Override
        protected void onPostExecute(List<SavedTicket> tickets) {
            super.onPostExecute(tickets);
            stList = tickets;
            List<ticketGroup> tg = sortTickets(stList);
            showDatabaseDialog(tg);
        }
    }

    private List<ticketGroup> sortTickets(List<SavedTicket> tickets){
        Iterator<SavedTicket> it = tickets.iterator();
        List<ticketGroup> tgList = new ArrayList<ticketGroup>();
        boolean exist = false;
        while(it.hasNext()){
            SavedTicket t = it.next();
            exist = false;
            String YearMonth = t.getDate().substring(0, 7);
            for(int i = 0; i < tgList.size(); i++){
                ticketGroup temp = tgList.get(i);
                if(temp.getOrigin().equals(t.getOrigin()) && temp.getDestination().equals(t.getDestination()) && temp.getMonth().equals(YearMonth)){
                    temp.getTickets().add(t);
                    exist = true;
                }
            }
            if(!exist){
                ticketGroup tg = new ticketGroup(t.getOrigin(), t.getDestination(), YearMonth);
                tg.getTickets().add(t);
                tgList.add(tg);
            }
        }
        return tgList;
    }

    public void onDbClick(ticketGroup item, Context context) {
        Intent db =new Intent (context, DatabaseActivity.class);
        db.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        myData.putSerializable("tickets", (ArrayList<SavedTicket>)item.getTickets());
        db.putExtras(myData);
        context.startActivity(db);
    }

}


