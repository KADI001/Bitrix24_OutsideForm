package com.example.application.bitrix24.properties;

public class EListProperty extends Property {

    private String linkBlockID;

    public EListProperty() {
    }

    public EListProperty(String linkBlockID) {
        this.linkBlockID = linkBlockID;
    }

    public EListProperty(String id, PropertyType type, String code, String linkBlockID) {
        super(id, type, code);
        this.linkBlockID = linkBlockID;
    }

    public EListProperty(String fieldID, String blockID, String name, boolean isRequired, boolean multiple, String id, PropertyType type, String code, String linkBlockID) {
        super(fieldID, blockID, name, isRequired, multiple, id, type, code);
        this.linkBlockID = linkBlockID;
    }

    public String getLinkBlockID() {
        return linkBlockID;
    }

    public void setLinkBlockID(String linkBlockID) {
        this.linkBlockID = linkBlockID;
    }
}
