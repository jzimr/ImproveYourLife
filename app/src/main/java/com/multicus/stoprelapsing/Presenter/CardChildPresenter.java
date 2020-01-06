package com.multicus.stoprelapsing.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.multicus.stoprelapsing.Model.Interactors.HelpedCardInteractor;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;
import com.multicus.stoprelapsing.R;
import com.multicus.stoprelapsing.View.CardChildView;

public class CardChildPresenter implements BasePresenter{
    //String cardId = null;
    private boolean isHelpedCard = false;
    private CardChildView cardChildView;
    Resources resources;

    public CardChildPresenter(Resources resources, CardChildView cardChildView){
        this.resources = resources;
        //this.cardId = cardId;
        this.cardChildView = cardChildView;

        /*
        // call database to check if this card is a helped card (doing for UI purposes)
        HelpedCardInteractor.getInstance().getHelpedCard(cardId, helpedCard -> {
            // if it is a helped card (we display option to user so that it can be undone if he wants)
            if(helpedCard != null && helpedCard.cardId.equals(cardId)){
                isHelpedCard = true;
                cardChildView.setHelpedButtonText(resources.getString(R.string.cards_did_not_help_button));
            } else {
                cardChildView.setHelpedButtonText(resources.getString(R.string.cards_it_helped_button));
            }
        });
         */
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onResume() {
    }

    /**
     * Button handler for when user clicked "It helped" button on a card
     * @param cardClickedId the ID of the card the button belonged to
     */
    public void onHelpedCardButtonClick(String cardClickedId){
        // if it is a helped card (we display option to user that it can be undone)
        if(this.isHelpedCard){
            HelpedCardInteractor.getInstance().removeHelpedCard(cardClickedId, removingHelpedCardListener);
            cardChildView.setHelpedButtonText(resources.getString(R.string.cards_it_helped_button));
            this.isHelpedCard = false;
        } else {
            HelpedCardInteractor.getInstance().addHelpedCard(new HelpedCard(cardClickedId), addingHelpedCardListener);
            cardChildView.setHelpedButtonText(resources.getString(R.string.cards_did_not_help_button));
            this.isHelpedCard = true;
        }
    }

    HelpedCardInteractor.OnFinishedAddingListener addingHelpedCardListener = success -> {
        // todo do UI stuff?
        if(success) {
        //Log.d("CardChildPresenter onFinishedAddingListener", "Card ID '" + cardId + "' has been added to database helper cards");
        } else {
            // ?
        }
    };

    HelpedCardInteractor.OnFinishedRemovingListener removingHelpedCardListener = success -> {
        // todo do UI stuff?
        if(success) {
           // Log.d("CardChildPresenter OnFinishedRemovingListener", "Card ID '" + cardId + "' has been removed from database helper cards");
        } else {
            // ?
        }
    };
}
