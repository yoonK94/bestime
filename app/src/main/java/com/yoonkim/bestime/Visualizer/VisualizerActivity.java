package com.yoonkim.bestime.Visualizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Room.SavedTicket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

public class VisualizerActivity extends AppCompatActivity {

    AppBarLayout abl;
    TextView title;
    TextView instruction;
    List<SavedTicket> stList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);

        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();
        stList = (ArrayList<SavedTicket>) bundle.getSerializable("tickets");
        System.out.println(stList);
        String text = "Visualizer";

        if(!(stList.isEmpty())){
            text = stList.get(0).getOrigin().toUpperCase() + "  to  " + stList.get(0).getDestination().toUpperCase();
        }

        Comparator<SavedTicket> cp = new Comparator<SavedTicket>() {
            @Override
            public int compare(SavedTicket o1, SavedTicket o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };

        //minSDKVersion had to be changed from 23 to 24
        //sort dates in ascending order
        stList.sort(cp);

        title = (TextView) findViewById(R.id.dialog_title);
        abl = (AppBarLayout) findViewById(R.id.app_bar);
        instruction = (TextView) findViewById(R.id.instructions);

        title.setText(text);

        SortingVisualizer visualizer = new SortingVisualizer(getApplicationContext());
        visualizer.setZoom(1000);
        visualizer.setFontSize(15);
        int[] data = new int[stList.size()];
        String[] labels = new String[stList.size()];

        //initialize data set for the visualizer
        for(int i = 0; i < data.length; i++){
            data[i] = stList.get(i).getPrice();
            String date = stList.get(i).getDate();
            date = date.substring(date.length()- 2);
            int d = date.charAt(date.length()-1) - '0';
            switch(d % 10){
                case 1: date += "st";
                break;
                case 2: date += "nd";
                break;
                case 3: date += "rd";
                break;
                default: date += "th";
            }

            labels[i] = date;
        }
        DataSet ds = new DataSet(data, labels);

        visualizer.setData(ds);
        abl.addView(visualizer);

    }





}
