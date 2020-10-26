package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.Map;

public class Area {

private Integer id;
private String state;
private String city;
private String province;
private String createdAt;
private Object updatedAt;
private Object deletedAt;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getState() {
return state;
}

public void setState(String state) {
this.state = state;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getProvince() {
return province;
}

public void setProvince(String province) {
this.province = province;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public Object getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(Object updatedAt) {
this.updatedAt = updatedAt;
}

public Object getDeletedAt() {
return deletedAt;
}

public void setDeletedAt(Object deletedAt) {
this.deletedAt = deletedAt;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}