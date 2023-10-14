package com.example.javatutorial.account;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javatutorial.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText UserName, UserEmail, UserPassword, UserConfirmPassword;
    private TextView UserError;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private boolean isInputValid,connectedMOBILE, connectedWIFI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize UI elements
        UserName = findViewById(R.id.username);
        UserEmail = findViewById(R.id.useremail);
        UserPassword = findViewById(R.id.userpass);
        UserConfirmPassword = findViewById(R.id.userconfirmpass);
        UserError = findViewById(R.id.usererror);
        progressBar = findViewById(R.id.progressBar);
        Button UserSignUp = findViewById(R.id.usersigup);
        TextView UserSignIn = findViewById(R.id.usersignin);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();




        //Sign Up button
        UserSignUp.setOnClickListener(this);
        UserSignIn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.usersigup) {
            if (isInputValid) {
                // Perform the registration process
                RegProcess();
            } else {
                // Perform account validation
                AccCheck();
            }
        } else if (v.getId() == R.id.usersignin) {
            // Start the sign-in activity
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }
    }
    private void AccCheck(){
        //EditText to String
        String userName = UserName.getText().toString().trim();
        String userEmail = UserEmail.getText().toString().trim();
        String userPassword = UserPassword.getText().toString().trim();
        String userConfirmPassword = UserConfirmPassword.getText().toString().trim();
        //Check every input if empty
        if(userName.isEmpty()){
            UserName.setError("Name required!");
            UserName.requestFocus();
        }
        if(userEmail.isEmpty()){
            UserEmail.setError("Email required!");
            UserEmail.requestFocus();
        }
        if(userPassword.isEmpty()){
            UserPassword.setError("Password required!");
            UserPassword.requestFocus();
        }
        if(userConfirmPassword.isEmpty()){
            UserConfirmPassword.setError("Confirm password required!!");
            UserConfirmPassword.requestFocus();
        }
        if(!userConfirmPassword.equals(userPassword)){
            UserConfirmPassword.setError("Not Match!");
            UserConfirmPassword.requestFocus();
        }
        else{
            //Transfer data to AV for account validation
            AccValid(userEmail, userPassword);
        }
    }
    private void AccValid(String Email,String Password){
        //Validate their Email, and Password by regex
        boolean isEmailValid = validateEmail(Email);
        boolean isPasswordValid = validatePassword(Password);
        //If name no have number, and their email are valid format
        if(isEmailValid && isPasswordValid){
            isInputValid = true;
        }
        else {
            if(!isEmailValid){
                UserName.setError("Enter valid Email");
                UserName.requestFocus();
            }
            if(!isPasswordValid){
                UserName.setError("-At least one letter (uppercase or lowercase).\n-At least one digit.\n-A minimum length of 8 characters.");
                UserName.requestFocus();
            }
        }
    }
    private void RegProcess(){
        //EditText to String
        String userName = UserName.getText().toString().trim();
        String userEmail = UserEmail.getText().toString().trim();
        String userPassword = UserPassword.getText().toString().trim();
        //Get date and time went they registered
        String userTime, currentDateTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US);
        currentDateTime = sdf.format(calendar.getTime());
        userTime = currentDateTime;
        //Check Internet connection
        //clarify if they use mobile data or WIFI
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                connectedMOBILE = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                connectedWIFI = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        }
        //Progress bar start to load
        //for Authentication of email
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            //Checking if their UID are null
            AuthResult authResult = task.getResult();
            if (authResult != null && authResult.getUser() != null) {
                //Get UID user
                String userID = authResult.getUser().getUid();
                if (task.isSuccessful()) {
                    Data user = new Data(userID, userName, userEmail, userPassword, userTime);
                    //Set Firebase database location where to save this data
                    //data format UserData/userUID/Data they input
                    FirebaseDatabase.getInstance().getReference("UserData").child(userID).setValue(user).addOnCompleteListener(task1 -> {
                        //Checking if they are online
                        if (connectedMOBILE || connectedWIFI) {
                            if (task1.isSuccessful()) {
                                startActivity(new Intent(Register.this, SignIn.class));
                                Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registered Unsuccessfully!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show(); //If no internet connection
                        }
                    });
                } else {
                    UserEmail.setError("This Email Already Exists.");
                    UserEmail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Registration was not successful. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validatePassword(String pass) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }
}