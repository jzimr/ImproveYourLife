package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.Presenter.HomePresenter;
import com.multicus.stoprelapsing.View.HomeView;

public class HomeFragment extends Fragment implements HomeView {
    HomePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new HomePresenter(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set a random quote on fragment creation
        presenter.setRandomQuote();
    }

    @Override
    public void setQuote(String quote) {
        TextView quoteText = getView().findViewById(R.id.homeQuoteText);

        quoteText.setText(quote);
    }

    @Override
    public String getQuote() {
        TextView quoteText = getView().findViewById(R.id.homeQuoteText);

        return quoteText.getText().toString();
    }
}
