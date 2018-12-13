package com.yoonkim.bestime.Ticket;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonkim.bestime.R;
import com.yoonkim.bestime.Room.SavedTicket;
import com.yoonkim.bestime.Room.SavedTicketDatabase;

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
        holder.originTextView.setText(item.getOrigin());
        holder.dateTextView.setText("Departure Date\n" + item.getDepart());
        holder.destinationTextView.setText(item.getDest());
        holder.priceTextView.setVisibility(View.INVISIBLE);
        holder.saveButton.setVisibility(View.INVISIBLE);
        holder.delButton.setVisibility(View.INVISIBLE);

        holder.saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Schedule item = items.get(position);
                SavedTicket ticket = new SavedTicket();
                ticket.setOrigin(item.getOrigin());
                ticket.setDestination(item.getDest());
                ticket.setPrice(item.getPrice());
                ticket.setDate(item.getDepart());
                SavedTicketDatabase.getSavedTicketDatabase(context).savedTicketDao().addSavedTicket(ticket);
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
