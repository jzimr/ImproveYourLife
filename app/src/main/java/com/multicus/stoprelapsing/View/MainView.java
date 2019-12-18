package com.multicus.stoprelapsing.View;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public interface MainView {
    /**
     * Set the background of the Home ImageView
     * @param imageId the resource id of the Drawable
     */
    void setBackground(int imageId);

    /**
     * Get the background drawable of the Home ImageView
     * @return the Drawable background object
     */
    Drawable getBackground();

    /**
     * Set the fragment that is to be showed to the user (will cover most of screen)
     * @param fragment the fragment to show
     */
    void setShowingFragment(Fragment fragment);

    /**
     * Get the fragment that is currently shown to the user
     * @return the Fragment object shown
     */
    Fragment getShowingFragment();
}
