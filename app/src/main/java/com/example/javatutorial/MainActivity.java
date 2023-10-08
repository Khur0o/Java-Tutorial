package com.example.javatutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(getApplicationContext(), Introduction.class));

        // Retrieve the identifier of the last visited activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String lastVisitedActivity = preferences.getString("lastVisitedActivity", null);
        if (lastVisitedActivity != null) {
            // Start the last visited activity
            try {
                Class<?> activityClass = Class.forName(lastVisitedActivity);
                startActivity(new Intent(this, activityClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case when there's no last visited activity saved
            // You can start the main activity or another default activity in this case
            startActivity(new Intent(getApplicationContext(), Introduction.class));
        }
    }
}