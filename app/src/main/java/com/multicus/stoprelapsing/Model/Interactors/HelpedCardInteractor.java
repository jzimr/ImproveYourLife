package com.multicus.stoprelapsing.Model.Interactors;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.multicus.stoprelapsing.Model.Room.AppDatabase;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;

import java.util.List;

public class HelpedCardInteractor {
    public interface OnFinishedListener {
        void onFinishedGettingHelpedCards(List<HelpedCard> helpedCards);
    }

    /**
     * Increase the "it helped" amount by 1 in the database using the card id
     * @param cardId the ID of the card that helped
     * @param database the database instance
     */
    public static void addHelpedCard(@NonNull String cardId, @NonNull AppDatabase database) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HelpedCard card = database.helpedCardDao().getHelpedCard(cardId);

                // if no entry in database yet, we create new instance and insert into database
                if (card == null) {
                    card = new HelpedCard(cardId);
                    database.helpedCardDao().insertHelpedCard(card);
                } else {
                    database.helpedCardDao().updateHelpedCard(card);
                }

                return null;
            }
        }.execute();
    }

    /**
     * Get all entries for cards that have helped from database
     * @param listener the listener to be called when function finishes
     * @param database the Appdatabase instance to get data from
     */
    public static void getAllHelpedCards(@NonNull OnFinishedListener listener, @NonNull AppDatabase database){
        new AsyncTask<Void, Void, List<HelpedCard>>() {
            @Override
            protected List<HelpedCard> doInBackground(Void... voids) {
                List<HelpedCard> cards = database.helpedCardDao().getAllHelpedCards();
                return cards;
            }

            @Override
            protected void onPostExecute(List<HelpedCard> helpedCards) {
                listener.onFinishedGettingHelpedCards(helpedCards);
            }
        }.execute();
    }
}
