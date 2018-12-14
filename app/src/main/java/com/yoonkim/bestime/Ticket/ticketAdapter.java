package com.yoonkim.bestime.Ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.MainActivity;
import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Room.SavedTicket;
import com.yoonkim.bestime.Room.SavedTicketDao;
import com.yoonkim.bestime.Room.SavedTicketDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ticketAdapter extends RecyclerView.Adapter<ticketAdapter.TicketViewHolder>{
    private Context context;
    private List<Schedule> items;
    private ClickListener mClickListener;

    public ticketAdapter(List<Schedule> items, Context context) {
        this.items = items;
        this.context = context;

    }

    public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView originTextView;
        TextView dateTextView;
        TextView destinationTextView;
        TextView priceTextView;
        TextView detail;
        TextView saveButton;
        TextView delButton;


        TicketViewHolder(View v) {
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
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_ticket, parent, false);
        context = parent.getContext();
        return new TicketViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder holder, final int position) {
        Schedule item = items.get(position);
        holder.originTextView.setText(item.getOrigin().toUpperCase());
        holder.dateTextView.setText("Departure Date\n" + item.getDepart());
        holder.destinationTextView.setText(item.getDest().toUpperCase());
        holder.priceTextView.setVisibility(View.INVISIBLE);
        holder.saveButton.setVisibility(View.INVISIBLE);
        holder.delButton.setVisibility(View.INVISIBLE);

        holder.saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Schedule item = items.get(position);
                new DatabaseAsync().execute(true, -3, item.getOrigin(), item.getDest(), item.getDepart(), item.getPrice());
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

    private void showUpdateDialog(final SavedTicket st, final int price) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.update_ticket_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialog_body = (TextView) view.findViewById(R.id.dialog_main);
        TextView dialog_old = (TextView) view.findViewById(R.id.dialog_old);
        TextView dialog_new = (TextView) view.findViewById(R.id.dialog_new);

        dialog_title.setText("Ticket already exists");
        dialog_body.setText("The ticket from " + st.getOrigin() + " to " + st.getDestination() + " already exists.\nWould you want to update the ticket?");
        dialog_old.setText("Original Price = " + st.getPrice());
        dialog_new.setText("New Price = " + price);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( "Change", new DialogInterface.OnClickListener() {
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
                alertDialog.dismiss();
                new DatabaseAsync().execute(false, st.getId(), null, null, null, price);
            }
        });
    }

    private class DatabaseAsync extends AsyncTask<Object, Void, ForAlert> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ForAlert doInBackground(Object... params) {
            Boolean shouldSearch = (Boolean) params[0];
            int position = (int) params[1];
            String origin = (String) params[2];
            String destination = (String) params[3];
            String date = (String) params[4];
            int price = (int) params[5];

            //check whether to search a SavedTicket based on if shouldSearch is true
            if(shouldSearch){
                List<SavedTicket> st = SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().getExistingTickets(origin, destination, date);
                if(!(st.isEmpty())){
                    ForAlert fa = new ForAlert();
                    fa.setSc(st.get(0));
                    fa.setPrice(price);
                    return fa;
                }
                else{
                    SavedTicket ticket = new SavedTicket();
                    ticket.setOrigin(origin);
                    ticket.setDestination(destination);
                    ticket.setPrice(price);
                    ticket.setDate(date);
                    SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().addSavedTicket(ticket);
                }
            }
            //savedSearch == false indicates that existing ticket should be updated
            else {
                //auto increment starts from index 1 not zero
                SavedTicket ticket = SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().getSavedTickets().get(position - 1);
                ticket.setPrice(price);
                SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().updateSavedTicket(ticket);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ForAlert toConfirm) {
            super.onPostExecute(toConfirm);
            //Since a ticket with same origin, destination, and departure date exists, check again if the user wants to update
            if(toConfirm != null){
                showUpdateDialog(toConfirm.getSc(), toConfirm.getPrice());
            }
        }
    }
}
