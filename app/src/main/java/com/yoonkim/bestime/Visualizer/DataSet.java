package com.yoonkim.bestime.Visualizer;

import java.io.Serializable;

public class DataSet implements Serializable {

    public int[] arr;
    public String[] labels;

    public DataSet(int[] arr, String[] labels){
        this.arr = arr;
        this.labels = labels;
    }
}
