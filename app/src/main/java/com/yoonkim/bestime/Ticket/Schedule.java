package com.yoonkim.bestime.Ticket;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String origin;
    private String dest;
    private int price;
    private String depart;

    public String getOrigin(){
        return origin;
    }
    public void setOrigin(String origin){
        this.origin = origin;
    }

    public String getDest(){
        return dest;
    }
    public void setDest(String dest){
        this.dest = dest;
    }

    public int getPrice(){
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }

    public String getDepart(){
        return depart;
    }
    public void setDepart(String depart){
        this.depart = depart;
    }


}
