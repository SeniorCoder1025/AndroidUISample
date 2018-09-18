package com.example.androidsample.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Deliver implements Serializable {
    @PrimaryKey
    public int id;

    public String desc;
    public String imageUrl;
    public double lng;
    public double lat;
    public String address;
}
