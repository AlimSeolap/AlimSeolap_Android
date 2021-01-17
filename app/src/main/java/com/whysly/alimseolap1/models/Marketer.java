package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.Map;

public class Marketer {

private int id;
private String email;
private String nickname;
private String gender;
private int ageId;
private int areaId;
private String profileImg;
private String signupChannel;
private String lastSignInDate;
private String company_name;
private String role;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getCompany_name() {
    return company_name;
}

public void setCompany_name(String company_name) {
    this.company_name = company_name;
}

public String getEmail() {
        return email;
    }

public void setEmail(String email) {
this.email = email;
}

public String getNickname() {
return nickname;
}

public void setNickname(String nickname) {
this.nickname = nickname;
}

public String getGender() {
return gender;
}

public void setGender(String gender) {
this.gender = gender;
}

public Integer getAgeId() {
return ageId;
}

public void setAgeId(int ageId) {
this.ageId = ageId;
}

public Integer getAreaId() {
return areaId;
}

public void setAreaId(int areaId) {
this.areaId = areaId;
}

public String getProfileImg() {
return profileImg;
}

public void setProfileImg(String profileImg) {
this.profileImg = profileImg;
}

public String getSignupChannel() {
return signupChannel;
}

public void setSignupChannel(String signupChannel) {
this.signupChannel = signupChannel;
}

public String getLastSignInDate() {
return lastSignInDate;
}

public void setLastSignInDate(String lastSignInDate) {
this.lastSignInDate = lastSignInDate;
}

public String getRole() {
return role;
}

public void setRole(String role) {
this.role = role;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
