package com.multicus.stoprelapsing.Model;

import android.content.Context;
import android.util.Log;

import com.multicus.stoprelapsing.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton class that acts as the main gate to get/set permanent data in the application
 */
public class Repository {
    private static Repository repo_instance = null;

    //private ArrayList<CardXmlParser.CardInfo> cardInfos;
    private HashMap<String, List<CardXmlParser.CardInfo>> cardInfos;    // key: category, value: cards

    private ArrayList<ImageXmlParser.ImageInfo> imageInfos;
    private ArrayList<QuoteXmlParser.QuoteInfo> quoteInfos;

    private Repository() {
        cardInfos = new HashMap<>();
        imageInfos = new ArrayList<>();
        quoteInfos = new ArrayList<>();
    }

    public static void init(Context context) {
        // if already initialized
        if (repo_instance != null) {
            Log.e("Repository init()", "Repository has already been initialized!");
            return;
        }

        repo_instance = new Repository();

        // read all cards
        CardXmlParser cardParser = new CardXmlParser();
        InputStream cardStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.cards));

        try {
            // first we get list of all cards
            List<CardXmlParser.CardInfo> cards = cardParser.parse(cardStream);

            // we go through each card and apply it to the appropriate category in hashmap
            for(CardXmlParser.CardInfo card : cards){
                // if key already exists
                if(repo_instance.cardInfos.containsKey(card.category)){
                    repo_instance.cardInfos.get(card.category).add(card);
                } else {
                    // if not we create new entry in hashmap containing list
                    List<CardXmlParser.CardInfo> newCardsList = new ArrayList<>();
                    newCardsList.add(card);

                    repo_instance.cardInfos.put(card.category, newCardsList);
                }
            }

        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read cards from XML");
            e.printStackTrace();
        }

        // read all images
        ImageXmlParser imageParser = new ImageXmlParser();
        InputStream imageStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.background_images));

        try {
            repo_instance.imageInfos.addAll(imageParser.parse(imageStream));
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read images from XML");
            e.printStackTrace();
        }

        // read all quotes
        QuoteXmlParser quoteParser = new QuoteXmlParser();
        InputStream quoteStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.quotes));

        try {
            repo_instance.quoteInfos.addAll(quoteParser.parse(quoteStream));
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read quotes from XML");
            e.printStackTrace();
        }
    }

    public static Repository getInstance() {
        if (repo_instance == null)
            Log.e("Repository getInstance()", "Repository has not been initialized yet!");

        return repo_instance;
    }

    /**
     * Retrieve all cards of a category
     *
     * @return List of CardInfo objects or null if category doesn't exist
     */
    public List<CardXmlParser.CardInfo> getCards(String category) {
        return cardInfos.containsKey(category) ? cardInfos.get(category) : null;
    }

    /**
     * Get all categories that we have
     * @return all categories in a String array
     */
    public String[] getAllCategories(){
        return (String[]) cardInfos.keySet().toArray();
    }

    /**
     * Retrieve all images we have stored
     *
     * @return a collection of all ImageInfo objects
     */
    public List<ImageXmlParser.ImageInfo> getAllImages() {
        return imageInfos;
    }

    /**
     * Retrieve all quotes we have stored
     *
     * @return a collection of all QuoteInfo objects
     */
    public List<QuoteXmlParser.QuoteInfo> getAllQuotes() {
        return quoteInfos;
    }

    // todo count of all cards
    // todo count of all images
    // todo count of all quotes


}
