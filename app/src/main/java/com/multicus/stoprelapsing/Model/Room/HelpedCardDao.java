package com.multicus.stoprelapsing.Model.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HelpedCardDao {
    @Insert
    void insertHelpedCard(HelpedCard card);

    @Query("DELETE FROM helpedcard WHERE cardId = :cardId")
    void deleteHelpedCard(String cardId);

    //@Update
    //void updateHelpedCard(HelpedCard card);

    @Query("SELECT * FROM helpedcard WHERE cardid = :cardId")
    HelpedCard getHelpedCard(String cardId);

    /**
     * Sorted HIGH -> LOW
     * @return
     */
    @Query("SELECT * FROM helpedcard")
    List<HelpedCard> getAllHelpedCards();
}
