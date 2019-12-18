package com.multicus.stoprelapsing.View;

public interface HomeView {
    /**
     * Set the quote of the Home TextView
     * @param quote the quote to display
     */
    void setQuote(String quote);

    /**
     * Get the quote of Home TextView
     * @return the quote as is or null if none set
     */
    String getQuote();
}
