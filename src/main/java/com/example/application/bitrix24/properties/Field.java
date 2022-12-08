package com.example.application.bitrix24.properties;

public class Field {

    private String fieldID;
    private String blockID;
    private String name;
    private boolean isRequired;
    private boolean multiple;

    public Field() {
    }

    public Field(String fieldID, String blockID, String name, boolean isRequired, boolean multiple) {
        this.fieldID = fieldID;
        this.blockID = blockID;
        this.name = name;
        this.isRequired = isRequired;
        this.multiple = multiple;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public String getBlockID() {
        return blockID;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public String getName() {
        return name;
    }

    public void setName(String text) {
        this.name = text;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public Property tryGetAsProperty(){

        if(fieldID.contains("PROPERTY_")){
           return (Property) this;
        }

        return null;
    }
}
