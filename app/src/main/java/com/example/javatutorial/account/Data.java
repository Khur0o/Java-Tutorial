package com.example.javatutorial.account;
public class Data {
    String userName, userEmail, userPassword;
    public Data(String User_Name,String User_Email, String User_Password){
        this.userName = User_Name;
        this.userEmail = User_Email;
        this.userPassword = User_Password;
    }
    public Data(String User_Email, String User_Password){
        this.userEmail = User_Email;
        this.userPassword = User_Password;
    }
}

