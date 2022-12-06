package com.kadiwhite.myForm.bitrix24.lists;

import java.util.HashMap;
import java.util.Map;

public class Process {

    private String id;
    private String name;

    private Map<String, Object> fields;

    public Process() {
        setFields(new HashMap<String, Object>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public void addField(String name, Object value){
        fields.put(name, value);
    }

    public void removeField(String name){
        fields.remove(name);
    }

    public Object getField(String name){
        return fields.get(name);
    }

    public void setField(String name, String value){
        fields.replace(name, value);
    }
}