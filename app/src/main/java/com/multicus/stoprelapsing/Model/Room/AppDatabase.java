package com.multicus.stoprelapsing.Model.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HelpedCard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract  HelpedCardDao helpedCardDao();
}
