package com.example.lenovo.myapp.Classes;



public class Notifiation {

    private String name;
    private String id;

    public Notifiation(){

    }

    public Notifiation(String name, String data) {
        this.name = name;
        this.id = data;
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
}
