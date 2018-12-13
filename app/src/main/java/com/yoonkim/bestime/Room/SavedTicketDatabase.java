package com.yoonkim.bestime.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {SavedTicket.class},version = 1)
public abstract class SavedTicketDatabase extends RoomDatabase {
    private static final String DB_NAME = "SavedTicket_Database.db";
    private static SavedTicketDatabase INSTANCE;
    public abstract SavedTicketDao savedTicketDao();

    public static SavedTicketDatabase getSavedTicketDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, SavedTicketDatabase.class, DB_NAME).build();
        }
        return INSTANCE;
    }
}
