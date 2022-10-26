package com.example.fuelqueuemanagement.dto;

public class FuelStation {

    private String id, stationName, OwnerName, email;
    private boolean petrol, diesel;

    public FuelStation(String id, String stationName, String ownerName, String email, boolean petrol, boolean diesel) {
        this.id = id;
        this.stationName = stationName;
        OwnerName = ownerName;
        this.email = email;
        this.petrol = petrol;
        this.diesel = diesel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPetrol() {
        return petrol;
    }

    public void setPetrol(boolean petrol) {
        this.petrol = petrol;
    }

    public boolean isDiesel() {
        return diesel;
    }

    public void setDiesel(boolean diesel) {
        this.diesel = diesel;
    }
}
