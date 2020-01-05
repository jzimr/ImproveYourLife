package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.Presenter.CardChildPresenter;
import com.multicus.stoprelapsing.View.CardChildView;


public class CardChildFragment extends Fragment implements CardChildView {
    public static final String CARD_NUM = CardViewpagerFragment.CARD_NUM;
    public static final String CARD_ID = "CARD_ID";
    public static final String CARD_CATEGORY = CardViewpagerFragment.CARD_CATEGORY;
    public static final String CARD_TITLE = "CARD_TITLE";
    public static final String CARD_BODY = "CARD_BODY";

    private CardChildPresenter presenter;
    private Button helpedButton;
    private int mNum;           // the position of this fragment in the list (of subcards)
    private String id;          // the id of the card
    private String category;    // the category of the card
    private String title;       // the title of the card
    private String body;        // the body of the card

    /**
     * Create a new instance of CardChildFragment, providing "num"
     * as an argument.
     */
    static CardChildFragment newInstance(int num, String cardId, String category, String title, String body) {
        CardChildFragment fragment = new CardChildFragment();

        // Supply card info as arguments
        Bundle args = new Bundle();
        args.putInt(CARD_NUM, num);
        args.putString(CARD_ID, cardId);
        args.putString(CARD_CATEGORY, category);
        args.putString(CARD_TITLE, title);
        args.putString(CARD_BODY, body);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt(CARD_NUM) : 1;
        id = getArguments() != null ? getArguments().getString(CARD_ID) : "";
        category = getArguments() != null ? getArguments().getString(CARD_CATEGORY) : "";
        title = getArguments() != null ? getArguments().getString(CARD_TITLE) : "";
        body = getArguments() != null ? getArguments().getString(CARD_BODY) : "";
    }

    /**
     * Setup the fragments UI components
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_child, container, false);
        TextView cardTitle = v.findViewById(R.id.basicCardTitle);
        TextView cardBody = v.findViewById(R.id.basicCardBody);

        helpedButton = v.findViewById(R.id.cardHelpedButton);
        // set click listener for the "it helped" button
        helpedButton.setOnClickListener(btn -> presenter.onHelpedCardButtonClick(id));

        cardTitle.setText(title);
        cardBody.setText(body);

        presenter = new CardChildPresenter(getResources(), id,this);

        return v;
    }

    @Override
    public void setHelpedButtonText(String newText) {
        helpedButton.setText(newText);
    }

    @Override
    public String getHelpedButtonText() {
        return helpedButton.getText().toString();
    }
}
