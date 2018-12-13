package com.yoonkim.bestime.Ticket;

public class Schedule {

    private String date;
    private String origin;
    private String dest;
    private int price;
//    private String airline;
//    private int flight_number;
    private String depart;
//    private String back;

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

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

//    public String getAirline(){
//        return airline;
//    }
//    public void setAirline(String airline){
//        this.airline = airline;
//    }
//
//    public int getFlight_number(){
//        return flight_number;
//    }
//    public void setFlight_number(int flight_number){
//        this.flight_number = flight_number;
//    }

    public String getDepart(){
        return depart;
    }
    public void setDepart(String depart){
        this.depart = depart;
    }

//    public String getBack(){
//        return back;
//    }
//    public void setBack(String back){
//        this.back = back;
//    }


}
