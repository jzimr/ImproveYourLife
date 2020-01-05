package com.multicus.stoprelapsing.Presenter;

import android.os.Bundle;

import com.multicus.stoprelapsing.CardViewpagerFragment;
import com.multicus.stoprelapsing.HomeFragment;
import com.multicus.stoprelapsing.Model.ImageXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.HomeInteractor;
import com.multicus.stoprelapsing.R;
import com.multicus.stoprelapsing.View.MainView;


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
                bundle.putString(CardViewpagerFragment.CARD_CATEGORY, "Physical");
                break;
            case R.id.nav_spritual:
                bundle.putString(CardViewpagerFragment.CARD_CATEGORY, "Spiritual");
                break;
            case R.id.nav_mind:
                bundle.putString(CardViewpagerFragment.CARD_CATEGORY, "Mind");
                break;
            case R.id.nav_food:
                bundle.putString(CardViewpagerFragment.CARD_CATEGORY, "Food");
                break;
            case R.id.nav_habit:
                bundle.putString(CardViewpagerFragment.CARD_CATEGORY, "Habit");
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
    public void setBackground() {
        // get list of all available images
        ImageXmlParser.ImageInfo image = HomeInteractor.getInstance().getCurrentImage();

        mMainView.setBackground(image.imageId);
    }
}
