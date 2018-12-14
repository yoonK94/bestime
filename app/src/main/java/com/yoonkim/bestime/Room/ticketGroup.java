package com.yoonkim.bestime.Room;

import java.util.ArrayList;
import java.util.List;

public class ticketGroup {
    private String origin;
    private String destination;
    List<SavedTicket> tickets;

    public ticketGroup(String origin, String destination){
        this.origin = origin;
        this.destination = destination;
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
}
