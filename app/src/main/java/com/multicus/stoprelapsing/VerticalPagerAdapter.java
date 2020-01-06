package com.multicus.stoprelapsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emoiluj.doubleviewpager.HorizontalViewPager;
import com.multicus.stoprelapsing.Model.CardXmlParser;
import com.multicus.stoprelapsing.Model.Interactors.CardInteractor;
import com.multicus.stoprelapsing.Presenter.CardChildPresenter;
import com.multicus.stoprelapsing.View.CardChildView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Taken mostly from: https://github.com/juliome10/DoubleViewPager/blob/master/DoubleViewPagerSample/src/main/java/com/emoiluj/doubleviewpagersample/VerticalPagerAdapter.java
 */
public class VerticalPagerAdapter extends PagerAdapter {
    // todo: check https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#dynamic-viewpager-fragments
    // todo: for improved adapter when needed

    private Context mContext;
    private int mParent;            // horizontal card index
    private int mChilds;            // vertical cards amount
    private CardChildPresenter mPresenter;

    private CardXmlParser.CardInfo mCard;

    public VerticalPagerAdapter(Context c, int parent, int childs, CardXmlParser.CardInfo card, CardChildPresenter presenter) {
        mContext = c;
        mParent = parent;
        mChilds = childs;

        mCard = card;       // the card we got

        mPresenter = presenter;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {

        return mChilds;
            /*
            if(CardInteractor.getInstance().getAllCards(category) == null){
                System.out.println("wtf123123");
            }
            return CardInteractor.getInstance().getAllCards(category).size();

             */
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

        //presenter = new CardChildPresenter(getResources(), id,this);
        container.addView(v);
        return v;
    }
}
