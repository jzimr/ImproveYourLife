package com.multicus.stoprelapsing.Presenter;

import com.multicus.stoprelapsing.Model.Interactors.HelpedCardInteractor;
import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;
import com.multicus.stoprelapsing.View.CardViewpagerView;

public class CardViewpagerPresenter implements BasePresenter{
    CardViewpagerView cardViewpagerView;

    public CardViewpagerPresenter(CardViewpagerView view){
        cardViewpagerView = view;
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
        // todo add UI listener
        System.out.println(cardClickedId);
        HelpedCardInteractor.getInstance().addHelpedCard(new HelpedCard(cardClickedId), null);
    }
}