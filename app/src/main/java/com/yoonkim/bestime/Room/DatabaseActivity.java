package com.yoonkim.bestime.Room;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Ticket.Schedule;
import com.yoonkim.bestime.Ticket.ticketAdapter;
import com.yoonkim.bestime.Ticket.ticketGroupAdapter;

import java.util.List;

public class DatabaseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ticketGroupAdapter adapter;
    List<SavedTicket> stList;
    databaseAdapter parentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);


        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();
        stList = (List<SavedTicket>) bundle.getSerializable("tickets");



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list_events);
        adapter = new ticketGroupAdapter(stList, getApplicationContext());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}