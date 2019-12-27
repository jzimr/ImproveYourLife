package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;
import com.multicus.stoprelapsing.Presenter.CardChildPresenter;
import com.multicus.stoprelapsing.View.CardChildView;


public class CardChildFragment extends Fragment implements CardChildView {
    public static final String CATEGORY_TYPE = CardViewpagerFragment.CATEGORY_TYPE;

    private CardChildPresenter presenter;
    private int mNum;   // the position of this fragment in the list
    private String category;
    private CardXmlParser.CardInfo card;    // the card object of this object

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

        fragment.card = CardInteractor.getInstance().getAllCards(category).get(num);    // the card we got

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

        presenter = new CardChildPresenter(this);
    }

    /**
     * Setup the fragments UI components
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_child, container, false);
        TextView cardBody = v.findViewById(R.id.basicCardBody);

        cardBody.setText(card.body);

        return v;
    }

    /**
     * Get the unique ID of the card belonging to this fragment
     * @return the ID of the card
     */
    public String getCardId(){
        return card.id;
    }
}
