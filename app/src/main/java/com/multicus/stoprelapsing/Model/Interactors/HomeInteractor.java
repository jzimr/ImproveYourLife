package com.multicus.stoprelapsing.Model.Interactors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.multicus.stoprelapsing.Model.ImageXmlParser;
import com.multicus.stoprelapsing.Model.QuoteXmlParser;
import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeInteractor {
    private static HomeInteractor home_instance;
    private ImageXmlParser.ImageInfo currentImage;                  // the image that will be displayed on Home
    private QuoteXmlParser.QuoteInfo currentQuote;                  // the quote that will be displayed on Home

    private HomeInteractor() {
        home_instance = null;
        currentImage = null;
        currentQuote = null;
    }

    /**
     * Initialize this Singleton class with an instance and images / quotes
     * @param context
     */
    public static void init(Context context) {
        // if not initialized yet
        if (home_instance == null) {
            home_instance = new HomeInteractor();
        }

        List<ImageXmlParser.ImageInfo> imageInfos = new ArrayList<>();
        List<QuoteXmlParser.QuoteInfo> quoteInfos = new ArrayList<>();

        /*
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

        /*
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

        /*
         * Get/set key-value pairs in internal storage
         */
        int defaultValue = 0;   // default value if last image update not found
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.shared_preference_home), Context.MODE_PRIVATE);

        // get values
        long lastHomeUpdate = sharedPref.getLong(context.getResources().getString(R.string.saved_last_home_update), defaultValue);
        int storedImageIndex = sharedPref.getInt(context.getResources().getString(R.string.saved_image_to_use), defaultValue);
        int storedQuoteIndex = sharedPref.getInt(context.getResources().getString(R.string.saved_quote_to_use), defaultValue);

        // no value has been set yet (first-time start) OR if at least 12 hours have passed since last update
        if (lastHomeUpdate == 0 || lastHomeUpdate + 43200000 < System.currentTimeMillis()) {
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
        home_instance.currentImage = imageInfos.get(storedImageIndex);
        home_instance.currentQuote = quoteInfos.get(storedQuoteIndex);
    }

    /**
     * Get the instance of this class
     * @return the Singleton instance
     */
    public static HomeInteractor getInstance(){
        if (home_instance == null)
            Log.e("HomeInteractor getInstance()", "HomeInteractor has not been initialized yet!");

        return home_instance;
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
}
