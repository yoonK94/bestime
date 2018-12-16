package com.yoonkim.bestime.Ticket;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Room.SavedTicket;
import com.yoonkim.bestime.Room.SavedTicketDatabase;
import com.yoonkim.bestime.Room.databaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ticketGroupAdapter extends RecyclerView.Adapter<ticketGroupAdapter.ticketGroupViewHolder>{
    private Context context;
    private List<SavedTicket> items;
    private ClickListener mClickListener;
    private List<databaseAdapter> adapters;

    public ticketGroupAdapter(List<SavedTicket> items, Context context) {
        this.items = items;
        this.context = context;

    }

    public class ticketGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView date;
        TextView price;
        TextView delete;

        ticketGroupViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.tv_timestamp);
            price = (TextView) v.findViewById(R.id.tv_price);
            delete = (TextView) v.findViewById(R.id.del);

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
    public ticketGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_savedticket_detail, parent, false);
        context = parent.getContext();
        return new ticketGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ticketGroupViewHolder holder, final int position) {
        final SavedTicket item = items.get(position);
        holder.date.setText(item.getDate());
        holder.price.setText("$" + item.getPrice());
        holder.delete.setText(" Delete ");
        holder.delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SavedTicket item = items.get(position);
                Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
                new DatabaseAsync().execute(item, new Integer(position));
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


    private class DatabaseAsync extends AsyncTask<Object, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            SavedTicket st = (SavedTicket) params[0];
            Integer position = (Integer) params[1];

            List<SavedTicket> search = SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().doesExist(st.getId());
            if(search.isEmpty()){
                return -1;
            }
            else {
                SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().deleteSavedTicket(st);
                return position;
            }
        }

        @Override
        protected void onPostExecute(Integer success) {
            super.onPostExecute(success);
            if(success == -1){
                Toast.makeText(context, "Oops, I have already deleted the ticket. Please reopen the dialog to update data", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Ticket successfully deleted from database", Toast.LENGTH_SHORT).show();
                items.remove(success.intValue());
                notifyDataSetChanged();
            }

        }
    }
}