package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.Map;

public class User {

private Integer id;
private String email;
private String nickname;
private String gender;
private String age_id;
private String area_id;
private String profile_img;
private String signup_channel;
private Object last_signin_date;
private String role;
private Boolean finished;
private Object company_name;
private Object company_contact;
private Object director_name;
private Object director_contact;
private Object director_email;
private Age age;
private Area area;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
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

public String getAge_id() {
return age_id;
}

public void setAge_id(String age_id) {
this.age_id = age_id;
}

public String getArea_id() {
return area_id;
}

public void setArea_id(String area_id) {
this.area_id = area_id;
}

public String getProfile_img() {
return profile_img;
}

public void setProfile_img(String profile_img) {
this.profile_img = profile_img;
}

public String getSignup_channel() {
return signup_channel;
}

public void setSignup_channel(String signup_channel) {
this.signup_channel = signup_channel;
}

public Object getLast_signin_date() {
return last_signin_date;
}

public void setLast_signin_date(Object last_signin_date) {
this.last_signin_date = last_signin_date;
}

public String getRole() {
return role;
}

public void setRole(String role) {
this.role = role;
}

public Boolean getFinished() {
return finished;
}

public void setFinished(Boolean finished) {
this.finished = finished;
}

public Object getCompany_name() {
return company_name;
}

public void setCompany_name(Object company_name) {
this.company_name = company_name;
}

public Object getCompany_contact() {
return company_contact;
}

public void setCompany_contact(Object company_contact) {
this.company_contact = company_contact;
}

public Object getDirector_name() {
return director_name;
}

public void setDirector_name(Object director_name) {
this.director_name = director_name;
}

public Object getDirector_contact() {
return director_contact;
}

public void setDirector_contact(Object director_contact) {
this.director_contact = director_contact;
}

public Object getDirector_email() {
return director_email;
}

public void setDirector_email(Object director_email) {
this.director_email = director_email;
}

public Age getAge() {
return age;
}

public void setAge(Age age) {
this.age = age;
}

public Area getArea() {
return area;
}

public void setArea(Area area) {
this.area = area;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}