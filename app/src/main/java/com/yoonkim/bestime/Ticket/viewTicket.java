package com.yoonkim.bestime.Ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yoonkim.bestime.R;

import java.util.List;

public class viewTicket extends AppCompatActivity {
    RecyclerView recyclerView;
    ticketAdapter adapter;
    List<Schedule> scList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();

        scList = (List<Schedule>) bundle.getSerializable("tickets");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list_events);
        adapter = new ticketAdapter(scList, getApplicationContext());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);




    }
}
