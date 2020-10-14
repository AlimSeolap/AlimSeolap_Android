package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.Map;

public class UserKeyword {

private int id;
private String keyword;
private int positiveValueCount;
private int negativeValueCount;
private int finalValueCount;
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

public int getPositiveValueCount() {
return positiveValueCount;
}

public void setPositiveValueCount(int positiveValueCount) {
this.positiveValueCount = positiveValueCount;
}

public int getNegativeValueCount() {
return negativeValueCount;
}

public void setNegativeValueCount(int negativeValueCount) {
this.negativeValueCount = negativeValueCount;
}

public int getFinalValueCount() {
return finalValueCount;
}

public void setFinalValueCount(int finalValueCount) {
this.finalValueCount = finalValueCount;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}