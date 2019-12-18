package com.multicus.stoprelapsing.Model;

import android.util.Log;
import android.util.Xml;

import com.multicus.stoprelapsing.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImageXmlParser {
    public static class ImageInfo{
        public final int imageId;
        public final String imageSrc;

        public ImageInfo(int imageId, String imageSrc){
            this.imageId = imageId;
            this.imageSrc = imageSrc;
        }
    }

    // We don't use namespaces
    private static final String ns = null;

    /**
     * Instantiate the parser by inputting a stream (the XML) and process
     * all elements inside the XML
     * @param in the inputstream
     * @return a list of ImageInfo objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readImages(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Read all background images
     * @param
     * @return a list of Image objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readImages(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<ImageInfo> images = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "images");
        // as long as not reached </images>
        while (parser.next() != XmlPullParser.END_TAG) {
            // skip the start tag <images>
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // look for the entry tags
            String name = parser.getName();

            if(name.equals("image")){
                images.add(readImage(parser));
            } else {
                continue;
            }
        }
        return images;
    }

    // process the image
    private ImageInfo readImage(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        int imageId = R.drawable.abigail_mangum;
        String imageSrc = null;

        // as long as not reached </image>
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch(name){
                case "imageName":
                    imageId = readId(parser);
                    break;
                case "imageSrc":
                    imageSrc = readSrc(parser);
                    break;
                default:
                    continue;
            }
        }
        return new ImageInfo(imageId, imageSrc);
    }

    // process the id content in the Image
    private int readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        // first get image name from xml
        parser.require(XmlPullParser.START_TAG, ns, "imageName");
        String imageName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "imageName");

        // then convert to resourceId
        int resID = getResId(imageName, R.drawable.class);

        return resID;
    }

    // process the image source in the Image
    private String readSrc(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "imageSrc");
        String imageSrc = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "imageSrc");
        return imageSrc;
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

    /** Return the resource ID given the resource name and class
     * Thanks to @Macarse:
     * https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
     * @param resName the name of the resource (without filetype)
     * @param c the class the resource can be found at
     * @return the ID of the resource
     */
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (NoSuchFieldException e){
            Log.e("ImageXMLParser getResId()", "Did not found the image resource with name '" + resName + "'");
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
