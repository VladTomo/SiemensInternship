package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    @JsonProperty("roomNumber")
    private int roomNumber;

    //Types:
    //1 for Single Room
    //2 for Double Room
    //3 for Suite Room
    //4 for Matrimonial Room
    @JsonProperty("type")
    private int type;

    @JsonProperty("price")
    private float price;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    public float getPrice() {
        return price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", type=" + type +
                ", price=" + price +
                ", Available=" + isAvailable +
                '}';
    }
}

