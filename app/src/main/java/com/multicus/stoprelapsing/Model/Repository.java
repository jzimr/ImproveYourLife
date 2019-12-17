package com.multicus.stoprelapsing.Model;

import android.content.Context;
import android.util.Log;

import com.multicus.stoprelapsing.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that acts as the main gate to get/set permanent data in the application
 */
public class Repository {
    private static Repository repo_instance = null;

    private static ArrayList<CardXmlParser.CardInfo> cardInfos;

    private Repository() {
    }

    public static void init(Context context) {
        // if already initialized
        if (repo_instance != null) {
            Log.e("Repository init()", "Repository has already been initialized!");
            return;
        }

        repo_instance = new Repository();

        // read all cards
        cardInfos = new ArrayList<>();
        CardXmlParser parser = new CardXmlParser();
        InputStream stream = new BufferedInputStream(context.getResources().openRawResource(R.raw.physical_cards));

        try {
            cardInfos.addAll(parser.parse(stream));
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read cards from XML");
            e.printStackTrace();
        }
    }

    public static Repository getInstance() {
        if (repo_instance == null)
            Log.e("Repository getInstance()", "Repository has not been initialized yet!");

        return repo_instance;
    }

    /**
     * Retrieve all cards we have stored
     *
     * @return a collection of CardInfo objects
     */
    public static List<CardXmlParser.CardInfo> getAllCards() {
        return cardInfos;
    }

    // todo count of all cards


}
