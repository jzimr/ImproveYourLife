package com.multicus.stoprelapsing.Presenter;

import android.util.Log;

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
     * When called will get a random quote and set it as the quote of the Home view
     */
    public void setRandomQuote() {
        // get list of all available quote
        List<QuoteXmlParser.QuoteInfo> quotes = Repository.getInstance().getAllQuotes();
        Random rand = new Random();
        int randomNum = rand.nextInt(quotes.size());    // get number from 0-quotesCount
        QuoteXmlParser.QuoteInfo quote = quotes.get(randomNum);     // the quote that was chosen

        Log.d("HomePresenter setRandomBackground()", "random number: " + randomNum + ", trying to load quote from: " + quote.author);

        homeView.setQuote("\"" + quote.content + "\" \n- " + quote.author);
    }


}
