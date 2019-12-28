package com.multicus.stoprelapsing.Model.Interactors;

import android.content.Context;
import android.util.Log;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Room.HelpedCard;
import com.multicus.stoprelapsing.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CardInteractor {
    private static CardInteractor card_instance;
    private HashMap<String, List<CardXmlParser.CardInfo>> cardInfos;    // key: category, value: cards

    /*
    // todo we need to check when the cardInfo's are officially ready.
    public interface OnFinishedListener {
        void onFinishedPreparingCards();
    }
     */

    private CardInteractor(){
        card_instance = null;
    }

    public static void init(Context context){
        // if not initialized yet
        if (card_instance == null) {
            card_instance = new CardInteractor();
        }

        card_instance.cardInfos = new HashMap<>();                      // we reset the hashmap

        /*
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
                if (card_instance.cardInfos.containsKey(card.category)) {
                    card_instance.cardInfos.get(card.category).add(card);
                } else {
                    // if not we create new entry in hashmap containing list
                    List<CardXmlParser.CardInfo> newCardsList = new ArrayList<>();
                    newCardsList.add(card);

                    card_instance.cardInfos.put(card.category, newCardsList);
                }
            }
        } catch (Exception e) {
            Log.e("Repository init()", "Some error happened whilst trying to read cards from XML");
            e.printStackTrace();
            return;
        }

        /*
         * Here we do the "Feature Cards" implementation
         */
        HelpedCardInteractor.getInstance().getAllHelpedCards(new HelpedCardInteractor.OnFinishedGettingAllListener() {
            @Override
            public void onFinishedGettingAllHelpedCards(List<HelpedCard> helpedCards) {
                Random rnd = new Random(System.currentTimeMillis());
                HashMap<String, List<CardXmlParser.CardInfo>> cardInfos = card_instance.cardInfos;    // key: category, value: cards
                Collections.shuffle(helpedCards, rnd);      // we shuffle our helped cards

                // How "Featured Cards" will work
                //
                // On each card is a "see this card more often" button. When user clicks this, it will be
                // stored in DB. So when user starts app again, two things will happen:
                //
                // 1. There is a 40% chance in each category that a "featured card" will even show up
                // 2. If the 40% is hit, we go through all "helped" cards and check if we have any of
                //    those in this category. If yes, we take the first card we find and put it to the
                //    front of the list.


                for (String category : cardInfos.keySet()) {
                    // we shuffle the cards in category
                    Collections.shuffle(cardInfos.get(category), rnd);

                    // then check if category should have "featured card" (40% chance atm)
                    if (rnd.nextFloat() < 0.40) {
                        // we loop through to see if we have any "helped cards" in the category
                        List<CardXmlParser.CardInfo> subCardInfos = cardInfos.get(category);
                        for (int i = 0; i < subCardInfos.size(); i++) {
                            final CardXmlParser.CardInfo cardToCheck = subCardInfos.get(i);

                            // check if we have such a helpedCard
                            if (helpedCards.stream().anyMatch(h -> h.cardId.equals(cardToCheck.id))) {
                                // Yes. So we choose this card as the one to be featured (adding to front of list)
                                CardXmlParser.CardInfo chosenCard = cardInfos.get(category).remove(i);
                                cardInfos.get(category).add(0, chosenCard);

                                Log.d("Repository onFinishedGettingHelpedCards()", "Chosen card " +
                                        chosenCard.id + " in category " + chosenCard.category + " to be featured");
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Get the instance of this class
     * @return the Singleton instance
     */
    public static CardInteractor getInstance(){
        if (card_instance == null)
            Log.e("CardInteractor getInstance()", "CardInteractor has not been initialized yet!");

        return card_instance;
    }

    /**
     * Retrieve all cards of a category
     *
     * @return List of CardInfo objects or null if category doesn't exist
     */
    public List<CardXmlParser.CardInfo> getAllCards(String category) {
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
}
