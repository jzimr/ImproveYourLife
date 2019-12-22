package com.multicus.stoprelapsing.Presenter;

import android.util.Log;
import android.view.View;

import com.multicus.stoprelapsing.Model.QuoteXmlParser;
import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.View.HomeView;

import java.util.List;
import java.util.Random;

public class HomePresenter implements BasePresenter{
    private HomeView homeView;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume() {

    }

    /**
     * When user clicks on a button
     * @param id the ID of the buttons clicked
     */
    public void handlepanicButtons(int id){
        // todo
    }

    /**
     * When called will get a random quote and set it as the quote of the Home view
     */
    public void setRandomQuote() {
        // get list of all available quote
        QuoteXmlParser.QuoteInfo quote = Repository.getInstance().getCurrentQuote();

        homeView.setQuote("\"" + quote.content + "\" \n- " + quote.author);
    }


}
