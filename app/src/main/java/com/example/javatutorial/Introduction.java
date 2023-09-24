package com.example.javatutorial;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Introduction extends AppCompatActivity {
    // Declare private member variables for UI elements and a counter variable
    private TextView textview, Skip;
    private FrameLayout Next;
    private ImageView Back;
    private int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        // Initialize UI elements by finding them in the layout XML file
        textview = findViewById(R.id.txt);
        Skip = findViewById(R.id.Skip);
        Back = findViewById(R.id.BCK);
        Next = findViewById(R.id.NXT);
        Back.setVisibility(View.INVISIBLE);
        // Check if the instruction has been skipped before
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean instructionSkipped = sharedPreferences.getBoolean("instructionSkipped", false);
        if(instructionSkipped) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        // Button Click Listeners
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!instructionSkipped) {
                    // Create an Intent to navigate to the MainActivity
                    // Add a flag to clear the activity stack (remove Introduction from back stack)
                    // Start the MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // Set the flag to indicate that the instruction has been skipped
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("instructionSkipped", true);
                    editor.apply();
                }
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Decrement the 'a' counter
                // Call the Switch method to handle the counter value
                --a;
                Switch();
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment the 'a' counter
                // Call the Switch method to handle the counter value
                ++a;
                Switch();
            }
        });
    }
    // Custom method to handle UI changes based on the 'a' counter
    // Set the text of the TextView to the string resource
    // Make the Back ImageView visible
    private void Switch() {
        switch (a) {
            case 1:
                textview.setText(R.string.intro2);
                Back.setVisibility(View.VISIBLE);
                break;
            case 2:
                textview.setText(R.string.intro3);
                Back.setVisibility(View.VISIBLE);
                break;
            default:
                // Set the text of the TextView to the default string resource
                // Make the Back ImageView invisible
                textview.setText(R.string.intro1);
                Back.setVisibility(View.INVISIBLE);
            }

    }
}