package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.multicus.stoprelapsing.Model.Repository;

public class CardViewpagerFragment extends Fragment {
    //final static int NUM_ITEMS = 10;

    CardViewAdapter mAdapter;
    ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_viewpager, container, false);

        mAdapter = new CardViewAdapter(getFragmentManager());

        mPager = (ViewPager)v.findViewById(R.id.cardViewPager);
        mPager.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return v;
    }

    public static class CardViewAdapter extends FragmentStatePagerAdapter {
        // todo: check https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#dynamic-viewpager-fragments
        // todo: for improved adapter when needed

        public CardViewAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return Repository.getInstance().getAllCards().size();
        }

        @Override
        public Fragment getItem(int position) {
            return CardChildFragment.newInstance(position);
        }
    }
}
