package com.yoonkim.bestime.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.yoonkim.bestime.Room.SavedTicket.TABLE_NAME;


@Entity(tableName = TABLE_NAME)
public class SavedTicket implements Serializable {
    public static final String TABLE_NAME = "savedtickets";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String origin;
    @ColumnInfo
    private String destination;
    @ColumnInfo
    private int price;
    @ColumnInfo
    private String date;

    public SavedTicket(){
    }

    @Ignore
    public SavedTicket(int id, String origin, String destination, int price, String date){
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.date = date;
    }

    public int getId(){return  id;}
    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin(){return  origin;}
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination(){return  destination;}
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrice(){return  price;}
    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate(){return  date;}
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SavedTicket{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date +  '\'' +
                '}';
    }
}
