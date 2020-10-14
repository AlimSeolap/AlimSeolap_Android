package com.whysly.alimseolap1.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

    private int id;
    private String title;
    private String content;
    private String redirecting_url;
    private String targetGender;
    private String type;
    private Boolean attach;
    private String created_at;
    private Object updated_at;
    private Object deleted_at;
    private int user;
    private Marketer marketer;
    private int users_messages_id;
    private List<Keyword> keyword = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRedirecting_url() {
        return redirecting_url;
    }

    public void setRedirecting_url(String redirecting_url) {
        this.redirecting_url = redirecting_url;
    }

    public String getTargetGender() {
        return targetGender;
    }

    public void setTargetGender(String targetGender) {
        this.targetGender = targetGender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAttach() {
        return attach;
    }

    public void setAttach(Boolean attach) {
        this.attach = attach;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Object getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Object updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Marketer getMarketer() {
        return marketer;
    }

    public void setMarketer(Marketer marketer) {
        this.marketer = marketer;
    }

    public int getUsers_messages_id() {
        return users_messages_id;
    }

    public void setUsers_messages_id(int users_messages_id) {
        this.users_messages_id = users_messages_id;
    }

    public List<Keyword> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<Keyword> keyword) {
        this.keyword = keyword;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}