package com.kadiwhite.myForm.bitrix24.properties;

import java.util.HashMap;
import java.util.Map;

public class Property extends Field {

    private String id;
    private PropertyType type;
    private String code;

    private static Map<PropertyType, String> typeNameMap = new HashMap<PropertyType, String>() {
        {
            put(PropertyType.Name, "Name");
            put(PropertyType.SEmployee, "S:employee");
            put(PropertyType.List, "L");
            put(PropertyType.EList, "E:EList");
            put(PropertyType.DataCreate, "DATE_CREATE");
            put(PropertyType.TimeStampX, "TIMESTAMP_X");
            put(PropertyType.CreatedBy, "CREATED_BY");
            put(PropertyType.ModifiedBy, "MODIFIED_BY");
            put(PropertyType.Money, "S:Money");
            put(PropertyType.String, "S");
            put(PropertyType.File, "F");
            put(PropertyType.DetailText, "DETAIL_TEXT");

        }
    };

    private static Map<String, PropertyType> nameTypeMap = new HashMap<String, PropertyType>() {
        {
            put("Name", PropertyType.Name);
            put("S:employee", PropertyType.SEmployee);
            put("L", PropertyType.List);
            put("E:EList", PropertyType.EList);
            put("DATE_CREATE", PropertyType.DataCreate);
            put("TIMESTAMP_X", PropertyType.TimeStampX);
            put("CREATED_BY", PropertyType.CreatedBy);
            put("MODIFIED_BY", PropertyType.ModifiedBy);
            put("S:Money", PropertyType.Money);
            put("S", PropertyType.String);
            put("F", PropertyType.File);
            put("DETAIL_TEXT", PropertyType.DetailText);
        }
    };

    public static String getPropertyTypeName(PropertyType type){
        return typeNameMap.get(type);
    }
    public static PropertyType getPropertyTypeName(String name){
        return nameTypeMap.get(name);
    }

    public Property() {
    }

    public Property(String id, PropertyType type, String code) {
        this.id = id;
        this.type = type;
        this.code = code;
    }

    public Property(String fieldID, String blockID, String name, boolean isRequired, boolean multiple, String id, PropertyType type, String code) {
        super(fieldID, blockID, name, isRequired, multiple);
        this.id = id;
        this.type = type;
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EListProperty tryGetAsEList(){
        if(type == PropertyType.EList){
            return (EListProperty) this;
        }

        return null;
    }

    public ListProperty tryGetAsList(){
        if(type == PropertyType.List){
            return (ListProperty) this;
        }

        return null;
    }

    public MoneyProperty tryGetAsMoney(){
        if(type == PropertyType.Money){
            return (MoneyProperty) this;
        }

        return null;
    }

    public SEmployeeProperty tryGetAsSEmployee(){
        if(type == PropertyType.SEmployee){
            return (SEmployeeProperty) this;
        }

        return null;
    }
    public StringProperty tryGetAsString(){
        if(type == PropertyType.String){
            return (StringProperty) this;
        }

        return null;
    }

    public FileProperty tryGetAsFile() {
        if(type == PropertyType.File){
            return (FileProperty) this;
        }

        return null;
    }

    public DetailTextProperty tryGetAsDetailText() {
        if(type == PropertyType.DetailText){
            return (DetailTextProperty) this;
        }

        return null;
    }
}