package com.example.javatutorial.account;
public class Data {
    String userID, userName, userEmail, userPassword, userTimeReg;
    public Data(String User_ID, String User_Name, String User_Email, String User_Password, String User_TimeREgeistered){
        this.userTimeReg = User_TimeREgeistered;
        this.userID = User_ID;
        this.userName = User_Name;
        this.userEmail = User_Email;
        this.userPassword = User_Password;
    }
    public Data(String User_Email, String User_Password){
        this.userEmail = User_Email;
        this.userPassword = User_Password;
    }
}

