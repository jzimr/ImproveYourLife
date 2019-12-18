package com.multicus.stoprelapsing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.multicus.stoprelapsing.View.HomeView;

public class HomeFragment extends Fragment implements HomeView {
    //public final String LAYOUT_TO_USE = "LAYOUT_TO_USE"; // todo create own interface for setting up bundles?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        Bundle bundle = savedInstanceState == null ? this.getArguments() : savedInstanceState;
        int layoutToUse = 0;

        // check if all required (or any) keys have been sent, if not we throw error to programmer
        if(bundle != null && bundle.containsKey(LAYOUT_TO_USE)){
            layoutToUse = bundle.getInt(LAYOUT_TO_USE);
        } else {
            Log.e("HomeFragment.onCreateView()", "REQUIRED KEY(S) ARE MISSING WHILST TRYING TO INFLATE HomeFragment");
            return null;
        }
         */

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
