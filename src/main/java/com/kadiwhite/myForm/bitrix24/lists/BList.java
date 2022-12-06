package com.kadiwhite.myForm.bitrix24.lists;

public class BList {
    private int id;
    private String iBlockTypeID;
    private String name;
    private String description;
    private String descriptionType;
    private String sectionsName;
    private String sectionName;
    private String elementsName;
    private String elementName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getiBlockTypeID() {
        return iBlockTypeID;
    }

    public void setiBlockTypeID(String iBlockTypeID) {
        this.iBlockTypeID = iBlockTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }

    public String getSectionsName() {
        return sectionsName;
    }

    public void setSectionsName(String sectionsName) {
        this.sectionsName = sectionsName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getElementsName() {
        return elementsName;
    }

    public void setElementsName(String elementsName) {
        this.elementsName = elementsName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
