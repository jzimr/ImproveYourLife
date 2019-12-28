package com.multicus.stoprelapsing;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used to show a loading screen to user while the stuff in "MainActivity" loads
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("onCreate is called");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
