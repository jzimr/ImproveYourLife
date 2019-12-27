package com.multicus.stoprelapsing.Model;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.multicus.stoprelapsing.Model.Room.AppDatabase;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that acts as the main gate to get/set permanent data in the application
 */
public class Repository {
    private static Repository repo_instance = null;
    private static AppDatabase database;

    private Repository() {
    }

    public static void init(Context context) {
        // if not initialized yet
        if (repo_instance == null || database == null) {
            repo_instance = new Repository();
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "schedule-database").build();
        }
    }

    public static Repository getInstance() {
        if (repo_instance == null)
            Log.e("Repository getInstance()", "Repository has not been initialized yet!");

        return repo_instance;
    }

    /**
     * Insert a helped card into the database
     * @param card the ard object with ID to insert
     * @return if insert was success or some error happened
     */
    public boolean insertHelpedCard(HelpedCard card){
        try{
            database.helpedCardDao().insertHelpedCard(card);
            return true;
        } catch(RuntimeException e){
            Log.e("Repository insertHelpedCard()", "Some exception happened whilst trying to insert a card into the database:\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Get a specific helped card from the database
     * @param cardId the card ID of the card
     * @return specific card object or null if not found
     */
    public HelpedCard getHelpedCard(String cardId){
        try{
            return database.helpedCardDao().getHelpedCard(cardId);
        } catch (RuntimeException e){
            Log.e("HelpedCard getHelpedCard()", "Some exception happened whilst trying to get a card from the database:\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Return a list of all cards that have been marked as "helped"
     * @return a list of all helped cards in database, or empty if error
     */
    public List<HelpedCard> getAllHelpedCards(){
        try{
            return database.helpedCardDao().getAllHelpedCards();
        } catch (RuntimeException e){
            Log.e("Repository getAllHelpedCards()", "Some exception happened whilst trying to get all cards from the database:\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Remove a card from the database
     * @param cardId the Id of the particular card to remove from database
     * @return if removal was success or some error happened
     */
    public boolean removeHelpedCard(String cardId){
        try{
            database.helpedCardDao().deleteHelpedCard(cardId);
            return true;
        } catch(RuntimeException e){
            Log.e("Repository removeHelpedCard()", "Some exception happened whilst trying to delete a card from the database:\n" + e.getMessage());
            return false;
        }
    }
}
