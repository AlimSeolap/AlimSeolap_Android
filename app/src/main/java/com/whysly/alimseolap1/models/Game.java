package com.whysly.alimseolap1.models;

import java.util.Map;

public class Game {
    Map<String,Integer> data;

    public Map<String,Integer> getData() {
        return data;
    }

    public void setData(Map<String,Integer> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Age{" +
                ", data='" + data + '\'' +
                '}';
    }
}