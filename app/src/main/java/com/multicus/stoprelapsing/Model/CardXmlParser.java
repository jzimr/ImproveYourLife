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
        public final String id;
        public final String category;
        public final String title;
        public final String body;

        public CardInfo(String id, String category, String title, String body){
            this.id = id;
            this.category = category;
            this.title = title;
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

        parser.require(XmlPullParser.START_TAG, ns, "cards");
        // as long as not reached </cards>
        while (parser.next() != XmlPullParser.END_TAG) {
            // skip the start tag <cards>
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // look for the entry tags
            String name = parser.getName();

            if(name.equals("card")){
                cards.add(readCard(parser));
            } else {
                continue;
            }
        }
        return cards;
    }

    // process the Card
    private CardInfo readCard(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "card");
        String id = "";
        String category = "";
        String title = "";
        String body = "";

        // as long as not reached </card>
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch(name){
                case "id":
                    id = readId(parser);
                    break;
                case "category":
                    category = readCategory(parser);
                    break;
                case "title":
                    title = readTitle(parser);
                    break;
                case "body":
                    body = readBody(parser);
                    break;
                default:
                    continue;
            }
        }
        return new CardInfo(id, category, title, body);
    }

    // process the id in the Card
    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    // process the category in the Card
    private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return category;
    }

    // process the title in the Card
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }


    // process the body content in the Card
    private String readBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "body");
        String body = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "body");
        return body;
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
