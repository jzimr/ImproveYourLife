package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;

public class CardViewPagerChildFragment extends Fragment {
    public static final String CARD_NUM = CardViewpagerFragment.CARD_NUM;
    public static final String CATEGORY_TYPE = CardViewpagerFragment.CARD_CATEGORY;

    CardViewPagerChildFragment.CardViewAdapter mAdapter;
    VerticalViewPager mPager;
    private int mNum;
    private CardXmlParser.CardInfo mCard;
    // todo tablayout?

    /**
     * Create a new instance of CardChildFragment, providing "num"
     * as an argument.
     */
    static CardViewPagerChildFragment newInstance(int num, String category) {
        CardViewPagerChildFragment fragment = new CardViewPagerChildFragment();

        // Supply num and card input as arguments
        Bundle args = new Bundle();
        args.putInt(CARD_NUM, num);
        args.putString(CATEGORY_TYPE, category);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int num = getArguments() != null ? getArguments().getInt(CARD_NUM) : 1;
        String category = getArguments() != null ? getArguments().getString(CATEGORY_TYPE) : "";

        mNum = num;
        mCard = CardInteractor.getInstance().getAllCards(category).get(num);    // the card we got
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;

        View v = inflater.inflate(R.layout.fragment_card_viewpager_child, container, false);

        // setup adapter and viewpager
        mAdapter = new CardViewPagerChildFragment.CardViewAdapter(getFragmentManager(), mCard);
        mPager = v.findViewById(R.id.cardViewPagerChild);
        //mTabLayout = (TabLayout) v.findViewById(R.id.cardTabDots);
        mPager.setAdapter(mAdapter);

        // to enable dots at bottom to show how many cards there are.
        // Thanks to @Juni: https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots
        //mTabLayout.setupWithViewPager(mPager, true);

        // Inflate the layout for this fragment
        return v;
    }

    public static class CardViewAdapter extends FragmentStatePagerAdapter {
        // todo: check https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#dynamic-viewpager-fragments
        // todo: for improved adapter when needed

        private CardXmlParser.CardInfo mCard;

        public CardViewAdapter(FragmentManager fm, CardXmlParser.CardInfo card) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mCard = card;
        }

        @Override
        public int getCount() {
            return mCard.cards.length;
        }

        @Override
        public Fragment getItem(int position) {
            return CardChildFragment.newInstance(position, mCard.id, mCard.category, mCard.title, mCard.cards[position]);
        }
    }
}
