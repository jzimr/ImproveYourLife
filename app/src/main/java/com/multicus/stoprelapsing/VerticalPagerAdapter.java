package com.multicus.stoprelapsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Presenter.HelpedButtonPresenter;
import com.multicus.stoprelapsing.View.HelpedButtonView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Specifies the vertical cards for one parent card
 * Taken mostly from: https://github.com/juliome10/DoubleViewPager/blob/master/DoubleViewPagerSample/src/main/java/com/emoiluj/doubleviewpagersample/VerticalPagerAdapter.java
 */
public class VerticalPagerAdapter extends PagerAdapter implements HelpedButtonView {
    // todo: check https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#dynamic-viewpager-fragments
    // todo: for improved adapter when needed

    private Context mContext;
    private int mParent;            // horizontal card index
    private int mChilds;            // vertical cards amount

    private Button mHelpedButton;
    private HelpedButtonPresenter mPresenter;

    private CardXmlParser.CardInfo mCard;

    public VerticalPagerAdapter(Context c, int parent, int childs, CardXmlParser.CardInfo card) {
        mContext = c;
        mParent = parent;
        mChilds = childs;

        mCard = card;       // the card we got
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mChilds;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * Similiar to onCreateView().
     * Called for each vertical card
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.fragment_card_child, null, false);

        TextView cardTitle = v.findViewById(R.id.basicCardTitle);
        TextView cardBody = v.findViewById(R.id.basicCardBody);

        cardTitle.setText(mCard.title);
        cardBody.setText(mCard.cards[position]);

        container.addView(v);
        return v;
    }

    /**
     * Set the button click listener to the specified presenter of this main card
     * @param helpedButton
     */
    @Override
    public void setButton(Button helpedButton) {
        mHelpedButton = helpedButton;
        mPresenter = new HelpedButtonPresenter(mContext.getResources(), this, mCard.id);
        mHelpedButton.setOnClickListener(v -> mPresenter.onHelpedCardButtonClick(mCard.id));
    }

    @Override
    public void setHelpedButtonText(String newText) {
        mHelpedButton.setText(newText);
    }

    @Override
    public String getHelpedButtonText() {
        return mHelpedButton.getText().toString();
    }
}
