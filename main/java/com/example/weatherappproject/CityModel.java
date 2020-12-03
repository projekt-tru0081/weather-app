package com.example.weatherappproject;

public class CityModel {

    private int id;
    private String city;
//CONSTRUCTORS
    public CityModel(int id, String city) {
        this.id = id;
        this.city = city;
    }

    public CityModel() {
    }
    //toString to take these methods
    @Override
    public String toString() {
        return "CityModel{" +
                "id=" + id +
                ", city='" + city + '\'' +
                '}';
    }
    public String toStringCity(){
        return  city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
