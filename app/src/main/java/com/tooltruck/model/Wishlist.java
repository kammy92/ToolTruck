package com.tooltruck.model;

/**
 * Created by Admin on 11-01-2017.
 */

public class Wishlist {
    int id;
    String text;
    
    public Wishlist (int id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getText () {
        return text;
    }
    
    public void setText (String text) {
        this.text = text;
    }
}
