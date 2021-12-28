package com.example.projectdairies;

public class firebasemodel {
    String date;
    String dairie;

    public firebasemodel(){

    }
    public firebasemodel(String date, String dairie){
        this.date = date;
        this.dairie = dairie;

    }

    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDairie() {
        return dairie;
    }

    public void setDairie(String dairie) {
        this.dairie = dairie;
    }
}
