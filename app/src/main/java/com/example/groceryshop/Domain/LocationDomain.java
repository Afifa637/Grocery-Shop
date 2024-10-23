package com.example.groceryshop.Domain;

public class LocationDomain {
    private int ID;
    private String loc;

    public LocationDomain(){

    }

    @Override
    public String toString() {
        return  loc ;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
