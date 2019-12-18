package com.multicus.stoprelapsing.Model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuoteXmlParser {
    public static class QuoteInfo{
        public final String content;
        public final String author;

        public QuoteInfo(String content, String author){
            this.content = content;
            this.author = author;
        }
    }

    // We don't use namespaces
    private static final String ns = null;

    /**
     * Instantiate the parser by inputting a stream (the XML) and process
     * all elements inside the XML
     * @param in the inputstream
     * @return a list of QuoteInfo objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readQuotes(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Read all quotes
     * @param
     * @return a list of QuoteInfo objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readQuotes(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<QuoteInfo> quotes = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "quotes");
        // as long as not reached </quotes>
        while (parser.next() != XmlPullParser.END_TAG) {
            // skip the start tag <quotes>
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // look for the entry tags
            String name = parser.getName();

            if(name.equals("quote")){
                quotes.add(readQuote(parser));
            } else {
                continue;
            }
        }
        return quotes;
    }

    // process the quote
    private QuoteInfo readQuote(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "quote");
        String quoteContent = "";
        String quoteAuthor = "";

        // as long as not reached </quote>
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch(name){
                case "content":
                    quoteContent = readContent(parser);
                    break;
                case "author":
                    quoteAuthor = readAuthor(parser);
                    break;
                default:
                    continue;
            }
        }
        return new QuoteInfo(quoteContent, quoteAuthor);
    }

    // process the content in the quote (i.e. the quote itself)
    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "content");
        String quoteContent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "content");

        return quoteContent;
    }

    // process the author that wrote the quote
    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "author");
        String quoteAuthor = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "author");
        return quoteAuthor;
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
