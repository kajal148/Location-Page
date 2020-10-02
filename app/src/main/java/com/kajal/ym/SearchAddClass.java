package com.kajal.ym;

import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

public class SearchAddClass {

    private String search_place;
    private String search_distance;

    public SearchAddClass() {
    }

    public SearchAddClass(String search_place, String search_distance) {
        this.search_place = search_place;
        this.search_distance = search_distance;
    }

    public String getSearch_place() {
        return search_place;
    }

    public void setSearch_place(String search_place) {
        this.search_place = search_place;
    }

    public String getSearch_distance() {
        return search_distance;
    }

    public void setSearch_distance(String search_distance) {
        this.search_distance = search_distance;
    }
}
