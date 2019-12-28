package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;

public class CardViewpagerFragment extends Fragment {
    public static final String CATEGORY_TYPE = "CATEGORY_TYPE";

    CardViewAdapter mAdapter;
    ViewPager mPager;
    TabLayout mTabLayout;

    public CardViewpagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;

        View v = inflater.inflate(R.layout.fragment_card_viewpager, container, false);

        // setup adapter and viewpager
        mAdapter = new CardViewAdapter(getFragmentManager(), bundle.getString(CATEGORY_TYPE));
        mPager = (ViewPager) v.findViewById(R.id.cardViewPager);
        mTabLayout = (TabLayout) v.findViewById(R.id.cardTabDots);
        mPager.setAdapter(mAdapter);

        // to enable dots at bottom to show how many cards there are.
        // Thanks to @Juni: https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots
        mTabLayout.setupWithViewPager(mPager, true);

        // Inflate the layout for this fragment
        return v;
    }

    public static class CardViewAdapter extends FragmentStatePagerAdapter {
        // todo: check https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#dynamic-viewpager-fragments
        // todo: for improved adapter when needed

        private String category;

        public CardViewAdapter(FragmentManager fm, String category) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.category = category;
        }

        @Override
        public int getCount() {
            return CardInteractor.getInstance().getAllCards(category).size();
        }

        @Override
        public Fragment getItem(int position) {
            return CardChildFragment.newInstance(position, category);
        }
    }
}
