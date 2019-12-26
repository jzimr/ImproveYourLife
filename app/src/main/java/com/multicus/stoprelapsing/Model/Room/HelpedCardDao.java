package com.multicus.stoprelapsing.Model.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HelpedCardDao {
    @Insert
    void insertHelpedCard(HelpedCard card);

    @Update
    void updateHelpedCard(HelpedCard card);

    @Query("SELECT * FROM helpedcard WHERE cardid = :cardId")
    HelpedCard getHelpedCard(String cardId);

    /**
     * Sorted HIGH -> LOW
     * @return
     */
    @Query("SELECT * FROM helpedcard")
    List<HelpedCard> getAllHelpedCards();
}
