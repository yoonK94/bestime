package com.yoonkim.bestime.Room;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.R;

import java.util.List;

public class databaseAdapter extends RecyclerView.Adapter<databaseAdapter.DatabaseViewHolder>{
    private Context context;
    private List<SavedTicket> items;
    private ClickListener mClickListener;

    public databaseAdapter(List<SavedTicket> items, Context context) {
        System.out.println(items);
        this.items = items;
        this.context = context;

    }

    public class DatabaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView originTextView;
        TextView dateTextView;
        TextView destinationTextView;
        TextView priceTextView;
        TextView detail;
        TextView saveButton;
        TextView delButton;

        DatabaseViewHolder(View v) {
            super(v);
            originTextView = (TextView) v.findViewById(R.id.tv_origin);
            dateTextView = (TextView) v.findViewById(R.id.tv_timestamp);
            destinationTextView = (TextView) v.findViewById(R.id.tv_dest);
            detail = (TextView) v.findViewById(R.id.tv_detail);
            priceTextView = (TextView) v.findViewById(R.id.tv_price);
            saveButton = (TextView) v.findViewById(R.id.button);
            delButton = (TextView) v.findViewById(R.id.del);

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
        SavedTicket item = items.get(position);
        holder.originTextView.setText(item.getOrigin().toUpperCase());
        holder.dateTextView.setText("Departure Date\n" + item.getDate());
        holder.destinationTextView.setText(item.getDestination().toUpperCase());
        holder.priceTextView.setVisibility(View.INVISIBLE);
        holder.saveButton.setVisibility(View.INVISIBLE);
        holder.delButton.setVisibility(View.INVISIBLE);

        holder.saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SavedTicket item = items.get(position);
                Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //this is for expanding recyclerview
                if (holder.priceTextView.getVisibility() == View.VISIBLE)
                {
                    holder.priceTextView.setVisibility(View.INVISIBLE); //this is used for shrinking
                    holder.priceTextView.setEnabled(false);
                    holder.priceTextView.setText("");

                    holder.saveButton.setVisibility(View.INVISIBLE); //this is used for shrinking
                    holder.saveButton.setEnabled(false);
                    holder.saveButton.setText("");

                    holder.delButton.setVisibility(View.INVISIBLE); //this is used for shrinking
                    holder.delButton.setEnabled(false);
                    holder.delButton.setText("");
                }
                else
                {
                    holder.priceTextView.setVisibility(View.VISIBLE);  //this is used for expanding
                    holder.priceTextView.setEnabled(true);
                    holder.priceTextView.setText("$" + items.get(position).getPrice());

                    holder.saveButton.setVisibility(View.VISIBLE);  //this is used for expanding
                    holder.saveButton.setEnabled(true);
                    holder.saveButton.setText("  Save  ");

                    holder.delButton.setVisibility(View.VISIBLE);  //this is used for expanding
                    holder.delButton.setEnabled(true);
                    holder.delButton.setText(" LongClick to Delete ");
                }
            }
        });
        holder.delButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                items.remove(items.get(position)); //remove object from list of objects
                notifyDataSetChanged();
                return true;
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
