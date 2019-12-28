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

    /*
     * Interfaces for callbacks
     */

    public interface OnFinishedAddingListener {
        void onFinishedAddingHelpedCards(boolean success);
    }
    public interface OnFinishedRemovingListener {
        void onFinishedRemovingHelpedCard(boolean success);
    }
    public interface OnFinishedGettingListener {
        void onFinishedGettingHelpedCard(HelpedCard helpedCard);
    }
    public interface OnFinishedGettingAllListener {
        void onFinishedGettingAllHelpedCards(List<HelpedCard> helpedCards);
    }

    private HelpedCardInteractor(){ }

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
     * Upload a "Helped Card" to the database
     * @param card the Card to upload to database
     * @param listener the callback to call when finished
     */
    public void addHelpedCard(@NonNull HelpedCard card, OnFinishedAddingListener listener) {
        new AddHelpedCardTask(card, listener).execute();
    }

    /**
     * Thread class for adding a new card to the database
     */
    private static class AddHelpedCardTask extends AsyncTask<Void, Void, Boolean> {
        private HelpedCard cardToAdd;
        private WeakReference<OnFinishedAddingListener> listener;

        public AddHelpedCardTask(HelpedCard card, OnFinishedAddingListener listener){
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
     * Remove a helped card from the database
     * @param cardId the Card to remove from database
     * @param listener the callback to call when finished
     */
    public void removeHelpedCard(@NonNull String cardId, OnFinishedRemovingListener listener) {
        new RemoveHelpedCardTask(cardId, listener).execute();
    }

    /**
     * Thread class for removing an existing card in database
     */
    private static class RemoveHelpedCardTask extends AsyncTask<Void, Void, Boolean> {
        private String cardToRemove;
        private WeakReference<OnFinishedRemovingListener> listener;

        public RemoveHelpedCardTask(String cardId, OnFinishedRemovingListener listener){
            this.cardToRemove = cardId;
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // if card exists in database
            HelpedCard card = Repository.getInstance().getHelpedCard(cardToRemove);

            // if entry is in database, we remove it from there
            if (card != null) {
                return Repository.getInstance().removeHelpedCard(cardToRemove);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean removalSuccess) {
            // if item is in database already or no callback is provided or there is no pointer to the
            // listener object anymore, we do nothing further with UI
            if(removalSuccess == null || listener == null || listener.get() == null){
                return;
            }

            // else we do UI updates
            listener.get().onFinishedRemovingHelpedCard(removalSuccess);
        }
    }

    /**
     * Get the entry for a specific card that has helped from database
     * @param cardId the ID of the specific card to retrieve
     * @param listener the listener to be called when function finishes
     */
    public void getHelpedCard(@NonNull String cardId, @NonNull OnFinishedGettingListener listener){
        new GetHelpedCardTask(cardId, listener).execute();
    }

    /**
     * Thread class for getting a Helper Card from database
     */
    private static class GetHelpedCardTask extends AsyncTask<Void, Void, HelpedCard> {
        private WeakReference<OnFinishedGettingListener> listener;
        private String cardId;

        public GetHelpedCardTask(String cardId, OnFinishedGettingListener listener){
            this.cardId = cardId;
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected HelpedCard doInBackground(Void... voids) {
            return Repository.getInstance().getHelpedCard(cardId);
        }

        @Override
        protected void onPostExecute(HelpedCard helpedCard) {
            // if listener object has been de-allocated while executing this
            if(listener.get() == null){
                return;
            }

            listener.get().onFinishedGettingHelpedCard(helpedCard);
        }
    }

    /**
     * Get all entries for cards that have helped from database
     * @param listener the listener to be called when function finishes
     */
    public void getAllHelpedCards(@NonNull OnFinishedGettingAllListener listener){
        new GetAllHelpedCardsTask(listener).execute();
    }

    /**
     * Thread class for getting all Helper Cards from database
     */
    private static class GetAllHelpedCardsTask extends AsyncTask<Void, Void, List<HelpedCard>> {
        private WeakReference<OnFinishedGettingAllListener> listener;

        public GetAllHelpedCardsTask(OnFinishedGettingAllListener listener){
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
            listener.get().onFinishedGettingAllHelpedCards(helpedCards);
        }
    }
}
