package com.yoonkim.bestime.Room;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.City.Airport;
import com.yoonkim.bestime.MainActivity;
import com.yoonkim.bestime.Map.MapActivity;
import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Ticket.ticketGroup;
import com.yoonkim.bestime.Ticket.ticketGroupAdapter;
import com.yoonkim.bestime.Visualizer.VisualizerActivity;

import java.util.ArrayList;
import java.util.List;

public class databaseAdapter extends RecyclerView.Adapter<databaseAdapter.DatabaseViewHolder>{
    private Context context;
    private List<ticketGroup> items;
    private ClickListener mClickListener;
    ticketGroupAdapter adapter;
    private OnDbClickListener dbClickListener;

    public databaseAdapter(List<ticketGroup> items, Context context, OnDbClickListener dbClickListener) {
        this.dbClickListener = dbClickListener;
        this.items = items;
        this.context = context;

    }



    public class DatabaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView originTextView;
        TextView destinationTextView;
        TextView show;
        TextView manage;

        DatabaseViewHolder(View v) {
            super(v);
            originTextView = (TextView) v.findViewById(R.id.tv_origin);
            destinationTextView = (TextView) v.findViewById(R.id.tv_dest);
            show = (TextView) v.findViewById(R.id.show);
            manage = (TextView) v.findViewById(R.id.manage);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null) mClickListener.onLongClick(view, getAdapterPosition());
            return false;
        }
    }

    @Override
    public DatabaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_savedticket, parent, false);
        context = parent.getContext();
        return new DatabaseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DatabaseViewHolder holder, final int position) {
        final ticketGroup item = items.get(position);
        holder.originTextView.setText(item.getOrigin().toUpperCase());
        holder.destinationTextView.setText(item.getDestination().toUpperCase());
        holder.show.setText(" Show ");
        holder.manage.setText(" Manage ");


        holder.show.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ticketGroup item = items.get(position);
                Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
                Intent db =new Intent (context, VisualizerActivity.class);
                db.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle myData = new Bundle();
                myData.putSerializable("tickets", (ArrayList<SavedTicket>)item.getTickets());
                db.putExtras(myData);
                context.startActivity(db);
            }
        });
        holder.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbClickListener.onDbClick(items.get(position), context);
            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    // allows clicks events to be caught
    void setClickListener(ClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}
