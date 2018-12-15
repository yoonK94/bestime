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
import java.util.List;

public class VisualizerActivity extends AppCompatActivity {

    AppBarLayout abl;
    TextView title;
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

        title = (TextView) findViewById(R.id.dialog_title);
        abl = (AppBarLayout) findViewById(R.id.app_bar);

        title.setText(text);

        SortingVisualizer visualizer = new SortingVisualizer(getApplicationContext());
        visualizer.setZoom(5000);
        visualizer.setFontSize(15);
        int[] data = new int[stList.size()];
        String[] labels = new String[stList.size()];
        for(int i = 0; i < data.length; i++){
            data[i] = stList.get(i).getPrice();
            labels[i] = stList.get(i).getDate().substring(stList.get(i).getDate().length() - 2);
        }
        DataSet ds = new DataSet(data, labels);

        visualizer.setData(ds);
        abl.addView(visualizer);

    }





}
