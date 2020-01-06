package com.multicus.stoprelapsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emoiluj.doubleviewpager.DoubleViewPagerAdapter;
import com.emoiluj.doubleviewpager.VerticalViewPager;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

public class MyOwnViewPager extends DoubleViewPagerAdapter {
    private Context mContext;
    private ArrayList<PagerAdapter> mAdapters;

    public MyOwnViewPager(Context context, ArrayList<PagerAdapter> verticalAdapters) {
        super(context, verticalAdapters);
        mContext = context;
        mAdapters = verticalAdapters;
    }


    @Override
    public int getCount() {
        return mAdapters.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.test_fragment, null, false);

        VerticalViewPager childVP = v.findViewById(R.id.verticalViewPager);
        childVP.setAdapter(mAdapters.get(position));

        container.addView(v);

        return v;
    }
}
