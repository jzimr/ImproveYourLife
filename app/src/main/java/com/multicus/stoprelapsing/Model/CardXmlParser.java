package com.multicus.stoprelapsing.Model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CardXmlParser {
    public static class CardInfo{
        public final String category;
        public final String body;

        public CardInfo(String category, String body){
            this.category = category;
            this.body = body;
        }
    }

    // We don't use namespaces
    private static final String ns = null;

    /**
     * Instantiate the parser by inputting a stream (the XML) and process
     * all elements inside the XML
     * @param in the inputstream
     * @return a list of Card objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCards(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Read all cards
     * @param
     * @return a list of Card objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readCards(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<CardInfo> cards = new ArrayList<>();
        String category = null, body;

        parser.require(XmlPullParser.START_TAG, ns, "cards");
        // as long as not reached </cards>
        while (parser.next() != XmlPullParser.END_TAG) {
            body = null;

            // skip the start tag <cards>
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // look for the entry tags
            String name = parser.getName();

            if(name.equals("category")){
                category = readCategory(parser);
            } else if (name.equals("card")) {
                cards.add(readCard(parser, category));
            } else {
                continue;
            }
        }
        return cards;
    }

    // process the category
    private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return category;
    }

    // process the Card
    private CardInfo readCard(XmlPullParser parser, String category) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "card");
        String body = null;

        // as long as not reached </card>
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch(name){
                case "body":
                    body = readBody(parser);
                    break;
                default:
                    continue;
            }
        }
        return new CardInfo(category, body);
    }

    // process the body content in the Card
    private String readBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "body");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "body");
        return category;
    }


    // Extract text values from tags containing text
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
