package com.multicus.stoprelapsing.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.multicus.stoprelapsing.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Singleton class that acts as the main gate to get/set permanent data in the application
 */
public class Repository {
    private static Repository repo_instance = null;

    private HashMap<String, List<CardXmlParser.CardInfo>> cardInfos;    // key: category, value: cards
    private ImageXmlParser.ImageInfo currentImage;                  // the image that will be displayed on Home
    private QuoteXmlParser.QuoteInfo currentQuote;                  // the quote that will be displayed on Home

    private Repository() {
        cardInfos = new HashMap<>();
        currentImage = null;
        currentQuote = null;
    }

    public static void init(Context context) {
        // if already initialized
        /*
        if (repo_instance != null) {
            Log.e("Repository init()", "Repository has already been initialized!");
            return;
        }
         */

        long startTime = System.nanoTime(); // for debugging

        repo_instance = new Repository();
        List<ImageXmlParser.ImageInfo> imageInfos = new ArrayList<>();
        List<QuoteXmlParser.QuoteInfo> quoteInfos = new ArrayList<>();

        /**
         * Read all cards
         */
        CardXmlParser cardParser = new CardXmlParser();
        InputStream cardStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.cards));

        try {
            // first we get list of all cards
            List<CardXmlParser.CardInfo> cards = cardParser.parse(cardStream);

            // we go through each card and apply it to the appropriate category in hashmap
            for (CardXmlParser.CardInfo card : cards) {
                // if key already exists
                if (repo_instance.cardInfos.containsKey(card.category)) {
                    repo_instance.cardInfos.get(card.category).add(card);
                } else {
                    // if not we create new entry in hashmap containing list
                    List<CardXmlParser.CardInfo> newCardsList = new ArrayList<>();
                    newCardsList.add(card);

                    repo_instance.cardInfos.put(card.category, newCardsList);
                }
            }


            // we go through each category and randomize the cards inside
            for (String key : repo_instance.cardInfos.keySet()) {
                System.out.println(key);
                List<CardXmlParser.CardInfo> cardsList = repo_instance.cardInfos.get(key);
                Collections.shuffle(cardsList, new Random(System.nanoTime()));
            }

        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read cards from XML");
            e.printStackTrace();
        }

        /**
         * Read all images
         */
        ImageXmlParser imageParser = new ImageXmlParser();
        InputStream imageStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.background_images));

        try {
            imageInfos.addAll(imageParser.parse(imageStream));
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read images from XML");
            e.printStackTrace();
        }

        /**
         * Read all quotes
         */
        QuoteXmlParser quoteParser = new QuoteXmlParser();
        InputStream quoteStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.quotes));

        try {
            quoteInfos.addAll(quoteParser.parse(quoteStream));
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read quotes from XML");
            e.printStackTrace();
        }

        /**
         * Get key-value pairs from internal storage
         */
        int defaultValue = 0;   // default value if last image update not found
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.shared_preference_home), Context.MODE_PRIVATE);

        // get values
        long lastHomeUpdate = sharedPref.getLong(context.getResources().getString(R.string.saved_last_home_update), defaultValue);
        int storedImageIndex = sharedPref.getInt(context.getResources().getString(R.string.saved_image_to_use), defaultValue);
        int storedQuoteIndex = sharedPref.getInt(context.getResources().getString(R.string.saved_quote_to_use), defaultValue);

        // no value has been set yet (first-time start) OR if at least 12 hours have passed since last update
        if(lastHomeUpdate == 0 || lastHomeUpdate + 43200000 < System.currentTimeMillis()){
            SharedPreferences.Editor editor = sharedPref.edit();
            Random rnd = new Random(System.currentTimeMillis());
            storedImageIndex = rnd.nextInt(imageInfos.size());
            storedQuoteIndex = rnd.nextInt(quoteInfos.size());

            // update last-update, image and quote values (using indexes for images & quotes)
            editor.putLong(context.getResources().getString(R.string.saved_last_home_update), System.currentTimeMillis());
            editor.putInt(context.getResources().getString(R.string.saved_image_to_use), storedImageIndex);
            editor.putInt(context.getResources().getString(R.string.saved_quote_to_use), storedQuoteIndex);
            editor.apply();
        }

        // we set the images and quotes that are to be displayed
        repo_instance.currentImage = imageInfos.get(storedImageIndex);
        repo_instance.currentQuote = quoteInfos.get(storedQuoteIndex);


        Log.d("Repository init()", "Initiation of data took: " + ((System.nanoTime() - startTime) / 1000000) + "ms");
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
     *
     * @return all categories in a String array
     */
    public String[] getAllCategories() {
        return (String[]) cardInfos.keySet().toArray();
    }

    /**
     * Retrieve all images we have stored
     *
     * @return a collection of all ImageInfo objects
     */
    public ImageXmlParser.ImageInfo getCurrentImage() {
        return currentImage;
    }

    /**
     * Retrieve all quotes we have stored
     *
     * @return a collection of all QuoteInfo objects
     */
    public QuoteXmlParser.QuoteInfo getCurrentQuote() {
        return currentQuote;
    }


    // todo count of all cards
    // todo count of all images
    // todo count of all quotes


}
