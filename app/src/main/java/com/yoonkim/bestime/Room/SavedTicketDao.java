package com.yoonkim.bestime.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface SavedTicketDao {

    @Query("SELECT * FROM " + SavedTicket.TABLE_NAME )
    List<SavedTicket> getSavedTickets();

    @Query("SELECT * FROM " + SavedTicket.TABLE_NAME + " WHERE origin = :ori AND  destination = :dest AND date = :depart")
    List<SavedTicket> getExistingTickets(String ori, String dest, String depart);


    @Insert
    void addSavedTicket(SavedTicket savedTicket);

    @Delete
    void deleteSavedTicket(SavedTicket savedTicket);

    @Update
    void updateSavedTicket(SavedTicket savedTicket);

    @Query("DELETE FROM " + SavedTicket.TABLE_NAME)
    public void dropTheTable();

}