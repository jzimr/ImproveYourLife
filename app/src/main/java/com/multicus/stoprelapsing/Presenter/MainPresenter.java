package com.multicus.stoprelapsing.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.multicus.stoprelapsing.CardViewpagerFragment;
import com.multicus.stoprelapsing.Model.ImageXmlParser;
import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.R;
import com.multicus.stoprelapsing.Utilities.ImageLoaderTask;
import com.multicus.stoprelapsing.View.MainView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class MainPresenter implements BasePresenter {
    private MainView mMainView;

    public MainPresenter(MainView mMainView) {
        this.mMainView = mMainView;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume() {

    }

    /**
     * Called when user clicks an item in the navigation drawer
     *
     * @param itemId the id of the item clicked
     */
    public void onNavigationItemSelected(int itemId) {
        switch (itemId) {
            case R.id.nav_physical:
                mMainView.setShowingFragment(new CardViewpagerFragment());
                break;
        }

        // todo: put all ifs below into switch case
        if (itemId == R.id.nav_gallery) {

        } else if (itemId == R.id.nav_slideshow) {

        } else if (itemId == R.id.nav_tools) {

        } else if (itemId == R.id.nav_physical) {

        } else if (itemId == R.id.nav_send) {

        }
    }

    /**
     * When called will get a random background image and set it as the background of the app
     */
    public void setRandomBackground(Context context) {
        // get list of all available images
        List<ImageXmlParser.ImageInfo> images = Repository.getInstance().getAllImages();
        Random rand = new Random();
        int randomNum = rand.nextInt(images.size());    // get number from 0-imagesCount
        ImageXmlParser.ImageInfo image = images.get(randomNum);     // the image that was chosen

        Log.d("MainPresenter setRandomBackground()", "random number: " + randomNum + ", trying to load imagesrc: " + image.imageSrc);

        mMainView.setBackground(image.imageId);
    }
}
