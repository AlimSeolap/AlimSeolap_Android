package com.whysly.alimseolap1.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Z_value {
    public HashMap<String, Double> getZ_value(HashMap<String, Integer> map) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        for(Map.Entry<String,Integer> entry : map.entrySet()){

            values.add(entry.getValue());
            System.out.println(values);
        }
        double mean = calculateMean(values);
        double SD = calculateSD(values , mean);
        //System.out.print(SD);
        HashMap<String, Double> result = new HashMap<>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.print("key: "+ entry.getKey());
            System.out.println(", Value: "+ entry.getValue());
            result.put(entry.getKey(), (mean - entry.getValue()) / SD);
            System.out.println(result);
        }
        return result;
    }

    public static double calculateMean(ArrayList<Integer> values)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = values.size();
        for(double num : values) {
            sum += num;
        }
        double mean = sum/length;
        return mean;
    }

    public static double calculateSD(ArrayList<Integer> values, double mean)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = values.size();
        for(double num : values) {
            sum += num;
        }
        for(double num: values) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }



}
