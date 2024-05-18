package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;
    @JsonProperty("rooms")
    private List<Room> rooms=new ArrayList<>();

    public Hotel() {
    }

    public Hotel(int id, String name, float latitude, float longitude, List<Room> rooms) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rooms=" + rooms +
                '}';
    }
}

