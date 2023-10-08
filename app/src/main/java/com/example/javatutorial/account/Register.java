package com.example.javatutorial.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.javatutorial.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private EditText UserName, UserEmail, UserPassword, UserConfirmPassword;
    private TextView UserError;
    private ProgressBar progressBar;
    private boolean isInputValid;
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
        //Sign Up button
        UserSignUp.setOnClickListener(view -> {
            if(isInputValid){
                progressBar.setVisibility(View.VISIBLE);
                RegistrationProcess();
            } else {
                AccountCheck();
            }
        });
    }
    private void AccountCheck(){
        //EditText to String
        String userName = UserName.getText().toString().trim();
        String userEmail = UserEmail.getText().toString().trim();
        String userPassword = UserPassword.getText().toString().trim();
        String userConfirmPassword = UserConfirmPassword.getText().toString().trim();
        //Check every input if empty
        if(userName.isEmpty()){
            UserName.setError("Enter your Name!");
            UserName.requestFocus();
        }
        if(userEmail.isEmpty()){
            UserEmail.setError("Enter your Email!");
            UserEmail.requestFocus();
        }
        if(userPassword.isEmpty()){
            UserPassword.setError("Enter your Password!");
            UserPassword.requestFocus();
        }
        if(userConfirmPassword.isEmpty()){
            UserConfirmPassword.setError("Confirm Password!");
            UserConfirmPassword.requestFocus();
        }
        if(!userConfirmPassword.equals(userPassword)){
            UserConfirmPassword.setError("Not Match Password!");
            UserConfirmPassword.requestFocus();
        }
        else{
            //Transfer data to Account Validation
            AccountValidation(userName, userEmail, userPassword);
        }
    }
    private void AccountValidation(String Name, String Email,String Password){
        boolean isNameValid = validateName(Name);
        boolean isEmailValid = validateEmail(Email);
        boolean isPasswordValid = validatePassword(Password);
        if(isNameValid || !isEmailValid || !isPasswordValid){
            UserError.setText("Name Format:\n\nFirst-name Middle-name Last-name Suffix\n\n\nEmail Format:\n\nxwx@example.com\n\n\nPassword Format:\n\n-At least one letter (uppercase or lowercase).\n-At least one digit.\n-A minimum length of 8 characters.");
        } else{
            isInputValid = true;
        }
    }
    private void RegistrationProcess(){
        progressBar.setVisibility(View.GONE);
    }
    public static boolean validateName(String name) {
        String regex = "\\b\\d+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
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