package com.multicus.stoprelapsing.Presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.multicus.stoprelapsing.CardViewpagerFragment;
import com.multicus.stoprelapsing.HomeFragment;
import com.multicus.stoprelapsing.Model.ImageXmlParser;
import com.multicus.stoprelapsing.Model.Repository;
import com.multicus.stoprelapsing.R;
import com.multicus.stoprelapsing.View.MainView;

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
        Bundle bundle = new Bundle();
        CardViewpagerFragment fragment = new CardViewpagerFragment();

        switch (itemId) {
            case R.id.nav_home:
                mMainView.setShowingFragment(new HomeFragment());
                return;
            case R.id.nav_physical:
                bundle.putString(CardViewpagerFragment.CATEGORY_TYPE, "Physical");
                break;
            case R.id.nav_spritual:
                bundle.putString(CardViewpagerFragment.CATEGORY_TYPE, "Spiritual");
                break;
            case R.id.nav_mind:
                bundle.putString(CardViewpagerFragment.CATEGORY_TYPE, "Mind");
                break;
            case R.id.nav_food:
                bundle.putString(CardViewpagerFragment.CATEGORY_TYPE, "Food");
                break;
            case R.id.nav_habit:
                bundle.putString(CardViewpagerFragment.CATEGORY_TYPE, "Habit");
                break;
            default:
                return;
        }

        // initiate fragment
        fragment.setArguments(bundle);
        mMainView.setShowingFragment(fragment);
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
