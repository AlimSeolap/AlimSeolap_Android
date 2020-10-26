package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.Map;

public class UserKeyword {

private int id;
private String keyword;
private int positive_value_count;
private int negative_value_count;
private int final_value_count;
private double p_value;
private double n_value;
private double z_value;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getKeyword() {
return keyword;
}

public void setKeyword(String keyword) {
this.keyword = keyword;
}

public int getPositive_value_count() {
return positive_value_count;
}

public void setPositive_value_count(int positive_value_count) {
this.positive_value_count = positive_value_count;
}

public double getZ_value() {
    return z_value;
}

public void setZ_value(double z_value) {
    this.z_value = z_value;
}

    public double getN_value() {
        return n_value;
    }

    public double getP_value() {
        return p_value;
    }

    public void setN_value(double n_value) {
        this.n_value = n_value;
    }

    public void setP_value(double p_value) {
        this.p_value = p_value;
    }

    public int getNegative_value_count() {
return negative_value_count;
}

public void setNegative_value_count(int negative_value_count) {
this.negative_value_count = negative_value_count;
}

public int getFinal_value_count() {
return final_value_count;
}

public void setFinal_value_count(int final_value_count) {
this.final_value_count = final_value_count;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}