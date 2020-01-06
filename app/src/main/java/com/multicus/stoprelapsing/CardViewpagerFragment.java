package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.emoiluj.doubleviewpager.DoubleViewPager;
import com.emoiluj.doubleviewpager.HorizontalViewPager;
import com.google.android.material.tabs.TabLayout;
import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;

import java.util.ArrayList;
import java.util.List;

public class CardViewpagerFragment extends Fragment {
    public static final String CARD_CATEGORY = "CARD_CATEGORY";
    public static final String CARD_NUM = "CARD_NUM";

    private DoubleViewPager mPager;
    TabLayout mTabLayout;

    private List<CardXmlParser.CardInfo> mCards;
    private String mCategory;
    private int horizontalChilds = 0;

    public CardViewpagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_viewpager, container, false);
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;

        mCategory = getArguments() != null ? getArguments().getString(CARD_CATEGORY) : "";
        mCards = CardInteractor.getInstance().getAllCards(mCategory);

        horizontalChilds = mCards.size();

        // setup adapter and viewpager
        ArrayList<PagerAdapter> verticalAdapters = new ArrayList<PagerAdapter>();
        generateVerticalAdapters(verticalAdapters);

        mPager = v.findViewById(R.id.cardViewPager);
        mPager.setAdapter(new CustomHorizontalViewPager(getContext(), verticalAdapters));
        mTabLayout = (TabLayout) v.findViewById(R.id.cardTabDots);

        mPager.setOnPageChangeListener(onCardSwipe);

        // to enable dots at bottom to show how many cards there are.
        // Thanks to @Juni: https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots
        for(int i = 0; i < horizontalChilds; i++){
            mTabLayout.addTab(mTabLayout.newTab(), i);
        }
        mTabLayout.getTabAt(0).select();    // auto-select the first tab
        // todo: add custom click listeners

        // Inflate the layout for this fragment
        return v;
    }


    private void generateVerticalAdapters(ArrayList<PagerAdapter> verticalAdapters) {
        for (int i = 0; i < horizontalChilds; i++) {
            CardXmlParser.CardInfo card = mCards.get(i);

            verticalAdapters.add(new VerticalPagerAdapter(getActivity(), i, card.cards.length, card));
        }
    }

    /**
     * Using this to detect whenever user swipes horizontally so we can:
     * 1. Change the dots at bottom of screen
     */
    private HorizontalViewPager.OnPageChangeListener onCardSwipe = new HorizontalViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            // do so when user swipes that the dot is then selected
            mTabLayout.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
}
