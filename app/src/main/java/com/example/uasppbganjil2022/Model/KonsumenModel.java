package com.example.uasppbganjil2022.Model;

public class KonsumenModel {
    String name, id;

    public KonsumenModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public KonsumenModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}