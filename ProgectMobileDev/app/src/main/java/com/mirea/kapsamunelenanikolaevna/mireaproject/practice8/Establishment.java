package com.mirea.kapsamunelenanikolaevna.mireaproject.practice8;

import org.osmdroid.util.GeoPoint;

//модель данных для заведения
public class Establishment {
    private String id;
    private String name;
    private String type;
    private String address;
    private String phone;
    private String hours;
    private double rating;
    private String description;
    private double latitude;
    private double longitude;

    public Establishment(String id, String name, String type, String address,
                         String phone, String hours, double rating,
                         String description, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.hours = hours;
        this.rating = rating;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getHours() { return hours; }
    public double getRating() { return rating; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }

}