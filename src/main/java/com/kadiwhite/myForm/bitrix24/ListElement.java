package com.kadiwhite.myForm.bitrix24;

public class ListElement {
    private String id;
    private String iBlockID;
    private String name;
    private String createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getiBlockID() {
        return iBlockID;
    }

    public void setiBlockID(String iBlockID) {
        this.iBlockID = iBlockID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
