package com.multicus.stoprelapsing.Model.Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HelpedCard {
    @PrimaryKey
    @NonNull
    public String cardId;

    //public int amountHelped;

    public HelpedCard(String cardId){
        this.cardId = cardId;
    }
}
