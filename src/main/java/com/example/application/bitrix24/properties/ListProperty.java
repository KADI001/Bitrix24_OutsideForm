package com.example.application.bitrix24.properties;

import java.util.HashMap;
import java.util.Map;

public class ListProperty extends Property {

    private Map<String, String> keyValueMap;

    public ListProperty() {
        keyValueMap = new HashMap<String, String>();
    }

    public ListProperty(Map<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }

    public ListProperty(String fieldID, String blockID, String id, PropertyType type, String code, boolean isRequired, boolean multiple, String name, Map<String, String> keyValueMap) {
        super(fieldID, blockID,  name, isRequired, multiple, id, type, code);
        this.keyValueMap = keyValueMap;
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public void setKeyValueMap(Map<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }

    public void addElement(String key, String value){
        keyValueMap.put(key, value);
    }

    public String getValue(String key){
        return keyValueMap.get(key);
    }
}
