package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;
import com.multicus.stoprelapsing.Presenter.CardChildPresenter;
import com.multicus.stoprelapsing.View.CardChildView;
import com.multicus.stoprelapsing.View.HelpedButtonView;


public class CardChildFragment extends Fragment implements CardChildView, HelpedButtonView {
    public static final String CATEGORY_TYPE = CardViewpagerFragment.CATEGORY_TYPE;

    private CardChildPresenter presenter;
    private Button helpedButton;
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
    }

    /**
     * Setup the fragments UI components
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_child, container, false);
        TextView cardBody = v.findViewById(R.id.basicCardBody);
        helpedButton = v.findViewById(R.id.cardHelpedButton);
        // set click listener for the "it helped" button
        helpedButton.setOnClickListener(btn -> presenter.onHelpedCardButtonClick(card.id));

        cardBody.setText(card.body);

        presenter = new CardChildPresenter(getResources(), card.id,this, this);

        return v;
    }

    @Override
    public void setButtonText(String newText) {
        helpedButton.setText(newText);
    }

    @Override
    public String getButtonText() {
        return helpedButton.getText().toString();
    }
}
