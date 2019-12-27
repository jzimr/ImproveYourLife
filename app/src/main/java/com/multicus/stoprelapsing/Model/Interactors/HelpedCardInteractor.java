package com.multicus.stoprelapsing.Model.Interactors;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;

import java.lang.ref.WeakReference;
import java.util.List;

public class HelpedCardInteractor {
    private static HelpedCardInteractor helped_card_instance;

    public interface OnFinishedListener {
        void onFinishedAddingHelpedCards(boolean success);
        void onFinishedGettingHelpedCards(List<HelpedCard> helpedCards);
    }

    private HelpedCardInteractor(){
    }

    /**
     * Initialize this Singleton class with an instance and images / quotes
     */
    public static void init(){
        // if not initialized yet
        if (helped_card_instance == null) {
            helped_card_instance = new HelpedCardInteractor();
        }
    }

    /**
     * Get the instance of this class
     * @return the Singleton instance
     */
    public static HelpedCardInteractor getInstance(){
        if (helped_card_instance == null)
            Log.e("HelpedCardInteractor getInstance()", "HelpedCardInteractor has not been initialized yet!");

        return helped_card_instance;
    }

    /**
     * Increase the "it helped" amount by 1 in the database using the card id
     * @param card the Card to upload to database
     * @param listener the callback to call when finished
     */
    public void addHelpedCard(@NonNull HelpedCard card, OnFinishedListener listener) {
        new AddHelpedCardTask(card, listener).execute();
    }

    /**
     * Thread class for adding a new card to the database
     */
    private static class AddHelpedCardTask extends AsyncTask<Void, Void, Boolean> {
        private HelpedCard cardToAdd;
        private WeakReference<OnFinishedListener> listener;

        public AddHelpedCardTask(HelpedCard card, OnFinishedListener listener){
            this.cardToAdd = card;
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // if card exists in database already
            HelpedCard card = Repository.getInstance().getHelpedCard(cardToAdd.cardId);

            // if no entry in database yet, we create new instance and insert into database
            if (card == null) {
                return Repository.getInstance().insertHelpedCard(cardToAdd);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean uploadSuccess) {
            // if item is in database already or no callback is provided or there is no pointer to the
            // listener object anymore, we do nothing further with UI
            if(uploadSuccess == null || listener == null || listener.get() == null){
                return;
            }

            // else we do UI updates
            listener.get().onFinishedAddingHelpedCards(uploadSuccess);
        }
    }

    /**
     * Get all entries for cards that have helped from database
     * @param listener the listener to be called when function finishes
     */
    public void getAllHelpedCards(@NonNull OnFinishedListener listener){
        new GetAllHelpedCardsTask(listener).execute();
    }

    /**
     * Thread class for getting all Helper Cards from database
     */
    private static class GetAllHelpedCardsTask extends AsyncTask<Void, Void, List<HelpedCard>> {
        private WeakReference<OnFinishedListener> listener;

        public GetAllHelpedCardsTask(OnFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected List<HelpedCard> doInBackground(Void... voids) {
            return Repository.getInstance().getAllHelpedCards();
        }

        @Override
        protected void onPostExecute(List<HelpedCard> helpedCards) {
            if(listener == null || listener.get() == null){
                return;
            }
            listener.get().onFinishedGettingHelpedCards(helpedCards);
        }
    }
}
