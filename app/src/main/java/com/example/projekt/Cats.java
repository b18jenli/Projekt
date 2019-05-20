package com.example.projekt;

public class Cats {
    private String name;
    private String location;
    private String category;
    private String auxdata;
    private int cost;


    public Cats(String inName, String inLocation, String inCategory, String inAuxdata, int inCost){
        name = inName;
        location = inLocation;
        category = inCategory;
        auxdata = inAuxdata;
        cost = inCost;
    }
    public Cats(String inName){
        name = inName;
        location = "";
        category = "";
        auxdata = "";
        cost = -1;
    }

    public String toString() {
        return name;
    }
    public String info(){
        String str=name+"\n" ;
        str+=" Originates from ";
        str+=location +"\n" ;
        str+=" Type: ";
        str+=category+"\n" ;
        str+=" Features: ";
        str+=auxdata+"\n" ;
        str+=" Price: ";
        str+=Integer.toString(cost);
        str+="SEK.";
        return str;
    }
    public void setCost(int newCost){
        cost = newCost;
    }
    public String getName(){
        return name;
    }
}
