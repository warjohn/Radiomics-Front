package com.example.applicationradiomics.utils;

public class DataReport {

    private String data;
    private boolean isenable = false;

    public DataReport() {}
    public DataReport(String data) {this.data = data;}

    public String getData() {return data;}
    public void setData (String data) {this.data = data; isenable = !isenable;System.out.println(data);}
//   TODO добавить проверку флага , если будет необходимость в дополнительных элементах data tabpane
}
