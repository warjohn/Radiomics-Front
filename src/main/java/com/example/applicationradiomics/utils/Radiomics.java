package com.example.applicationradiomics.utils;

import java.util.ArrayList;
import java.util.List;

public class Radiomics {

    private List<String> data = new ArrayList<>();

    public Radiomics() {}
    public Radiomics(List<String> data) {this.data = data;}

    public List<String> getData() {return data;}
    public void addData(String elem) {
        if (!data.contains(elem)) {
            data.add(elem);
        } else {
            data.remove(elem);
        }
        System.out.println(data);
    }

}
