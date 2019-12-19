package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Repository;


public class CardChildFragment extends Fragment {
    public static final String CATEGORY_TYPE = CardViewpagerFragment.CATEGORY_TYPE;

    private int mNum;   // the position of this fragment in the list
    private String category;    // the category of the card

    /**
     * Create a new instance of CardChildFragment, providing "num"
     * as an argument.
     */
    static CardChildFragment newInstance(int num, String category) {
        CardChildFragment fragment = new CardChildFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
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
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        category = getArguments() != null ? getArguments().getString(CATEGORY_TYPE) : "";
    }

    /**
     * Setup the fragments UI components
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_child, container, false);
        TextView body = v.findViewById(R.id.basicCardBody);

        CardXmlParser.CardInfo card = Repository.getInstance().getCards(category).get(mNum);
        body.setText(card.body);

        return v;
    }
}
