package com.yoonkim.bestime.Ticket;

import com.yoonkim.bestime.Room.SavedTicket;

public class ForAlert {
    private SavedTicket sc;
    private int price;

    public SavedTicket getSc(){
        return sc;
    }
    public void setSc(SavedTicket sc){
        this.sc = sc;
    }
    public int getPrice(){
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }
}
