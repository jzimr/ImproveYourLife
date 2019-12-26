package com.multicus.stoprelapsing.Presenter;

import com.multicus.stoprelapsing.View.CardChildView;

public class CardChildPresenter implements BasePresenter{
    private CardChildView cardChildView;

    public CardChildPresenter(CardChildView cardChildView){
        this.cardChildView = cardChildView;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume() {

    }
}
