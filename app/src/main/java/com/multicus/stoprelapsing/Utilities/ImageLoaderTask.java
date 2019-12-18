package com.multicus.stoprelapsing.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;

import com.multicus.stoprelapsing.View.MainView;

/**
 * NOT USED ATM!!!
 * Utility class that helps to load large and high resolution images efficiently using asynchronous
 * thread in order to not slow down UI thread on startup
 */
public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
    public static class Dimens {
        public int width;
        public int height;

        public Dimens(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private final MainView mainView;
    private final Resources res;
    private final int imageId;
    private final int reqWidth;
    private final int reqHeight;

    public ImageLoaderTask(MainView mainView, Resources res, int imageId, int reqWidth, int reqHeight){
        this.mainView = mainView;
        this.res = res;
        this.imageId = imageId;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
    }

    /**
     * Resize the image to fit the screen
     * @param Void
     * @return
     */
    @Override
    protected Bitmap doInBackground(Void... Void) {
        return decodeSampledBitmapFromResource(res, imageId, reqWidth, reqHeight);
    }

    /**
     * Set background
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // this is just for debugging purposes
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, imageId, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        Log.d("ImageLoaderTask onPostExecute()", "Image has been resized from (" +
                imageWidth + ", " + imageHeight + ") to (" + bitmap.getWidth() + ", " +
                bitmap.getHeight() + ").");

        // set the background at last
        //mainView.setBackground(bitmap);
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int imageId,
                                                         int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeResource(res, imageId, options);

        // Calculate sampleSize
        int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        System.out.println(options.outWidth + ", " + options.outHeight + ", " + options.inSampleSize);

        return Bitmap.createScaledBitmap(bitmap, options.outWidth / sampleSize, options.outHeight / sampleSize, false);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image with both dimensions larger than or
            // equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        System.out.println("Sample size: " + inSampleSize);

        return inSampleSize;
    }

    public static Dimens getScreenDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //System.out.println("Screen dimensions: " + width + ", " + height);

        return new Dimens(width, height);
    }

    public static Dimens getImageDimensions(int imageId, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageId, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        String imageType = options.outMimeType;

        //System.out.println("Image dimensions: " + imageWidth + ", " + imageHeight);

        return new Dimens(imageWidth, imageHeight);
    }
}
