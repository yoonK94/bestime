package com.yoonkim.bestime.Ticket;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.yoonkim.bestime.Room.SavedTicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ticketGroup implements Serializable {
    private String origin;
    private String destination;
    private String month;
    private String monthToPrint;
    List<SavedTicket> tickets;


    public ticketGroup(String origin, String destination, String YearMonth){
        this.origin = origin;
        this.destination = destination;
        this.month = YearMonth;
        switch (month.substring(5,7)) {
            case "01":
                monthToPrint = "  Jan " + month.substring(0, 4);
                break;
            case "02":
                monthToPrint = "  Feb " + month.substring(0, 4);
                break;
            case "03":
                monthToPrint = "  Mar " + month.substring(0, 4);
                break;
            case "04":
                monthToPrint = "  Apr " + month.substring(0, 4);
                break;
            case "05":
                monthToPrint = "  May " + month.substring(0, 4);
                break;
            case "06":
                monthToPrint = "  Jun " + month.substring(0, 4);
                break;
            case "07":
                monthToPrint = "  Jul " + month.substring(0, 4);
                break;
            case "08":
                monthToPrint = "  Aug " + month.substring(0, 4);
                break;
            case "09":
                monthToPrint = "  Sep " + month.substring(0, 4);
                break;
            case "10":
                monthToPrint = "  Oct " + month.substring(0, 4);
                break;
            case "11":
                monthToPrint = "  Nov " + month.substring(0, 4);
                break;
            case "12":
                monthToPrint = "  Dec " + month.substring(0, 4);
        }
        tickets = new ArrayList<>();
    }

    public void addDate(SavedTicket ticket){
        tickets.add(ticket);
    }

    public String getOrigin(){
        return origin;
    }
    public String getDestination(){
        return destination;
    }
    public List<SavedTicket> getTickets(){
        return tickets;
    }
    public String getMonth() {
        return month;
    }
    public String getMonthToPrint(){
        return monthToPrint;
    }
}
