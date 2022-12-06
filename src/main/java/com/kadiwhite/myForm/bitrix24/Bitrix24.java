package com.kadiwhite.myForm.bitrix24;

import com.kadiwhite.myForm.bitrix24.lists.BList;
import com.kadiwhite.myForm.bitrix24.lists.Process;
import com.kadiwhite.myForm.bitrix24.properties.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Bitrix24 {
    private Map<IBlockTypeID, String> typeIDStringMap;
    private String restURL;
    public Bitrix24(String restURL) {
        this.restURL = restURL;

        typeIDStringMap = new HashMap<>();
        typeIDStringMap.put(IBlockTypeID.Lists, "lists");
        typeIDStringMap.put(IBlockTypeID.Bitrix_Processes, "bitrix_processes");
        typeIDStringMap.put(IBlockTypeID.Lists_Socnet, "lists_socnet");
    }

    public List<Process> getProcesses(String[] additionalFields){
        List<Process> res = new ArrayList<Process>();
        String params = "IBLOCK_TYPE_ID=" + typeIDStringMap.get(IBlockTypeID.Bitrix_Processes);
        String response = callMethod("lists.get", params);

        try{
            Object obj = new JSONParser().parse(response);
            JSONObject jo = (JSONObject) obj;
            JSONArray result = (JSONArray) jo.get("result");
            Iterator resultIterator = result.iterator();

            while (resultIterator.hasNext()){
                JSONObject list = (JSONObject) resultIterator.next();
                Process newBProcess = new Process();
                String id = (String) list.get("ID");
                String name = (String) list.get("NAME");

                if(additionalFields != null){

                    for(String field: additionalFields){
                        Object value = list.get(field);

                        if(value != null){
                            newBProcess.addField(field.toLowerCase(), value);
                        }
                    }
                }

                newBProcess.setId(id);
                newBProcess.setName(name);

                res.add(newBProcess);
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }
        return res;
    }
    public String addProcessElement(String blockID, String elementCode, Map<String, Object> fields){

        String resp = null;
        StringBuilder params = new StringBuilder();

        params.append( "IBLOCK_TYPE_ID=bitrix_processes&ELEMENT_CODE=" + elementCode +  "&IBLOCK_ID=" + blockID);

        List<Field> processFields = getProcessFieldsAndProperties(blockID);

        for(Field field: processFields){

            if(field.getFieldID().contains("PROPERTY_")){
                Property property = (Property) field;

                if(property.getType() == PropertyType.List){
                    ListProperty listProperty = (ListProperty) property;
                    String value =null;

                    for (String key: listProperty.getKeyValueMap().keySet()){
                        if(listProperty.getValue(key).equals(fields.get(listProperty.getCode()))){
                            value = key;
                        }
                    }

                    if(value == null){
                        value = (String) fields.get(property.getCode());
                    }

                    if(value != null){
                        params.append("&FIELDS[" + property.getFieldID() + "]=" + value);
                    }
                }

                if(property.getType() == PropertyType.EList){
                    EListProperty eListProperty = (EListProperty) property;
                    String value = null;
                    boolean isInt = isInteger((String) fields.get(eListProperty.getCode()));

                    if(isInt == false){
                        List<ListElement> elements = getListElementsByBlockID(eListProperty.getLinkBlockID(), "FILTER[NAME]=" + fields.get(eListProperty.getCode()));

                        if(elements.isEmpty())
                        {
                            throw new IllegalArgumentException("EList element doesn't exist");
                        }

                        Iterator iter = elements.iterator();
                        ListElement element = (ListElement) iter.next();

                        value = element.getId();
                    }
                    else{
                        value = (String) fields.get(property.getCode());
                    }

                    if(value != null){
                        params.append("&FIELDS[" + property.getFieldID() + "]=" + value);
                    }
                }

                if(property.getType() != PropertyType.List && property.getType() != PropertyType.EList){
                    String value = (String) fields.get(property.getCode());

                    if(value != null){
                        params.append("&FIELDS[" + property.getFieldID() + "]=" + value);
                    }
                }
            }
            else{
                String value = (String) fields.get(field.getFieldID());

                if(value != null){
                    params.append("&FIELDS[" + field.getFieldID() + "]=" + value);
                }
            }

        }

        resp = callMethod("lists.element.add", params.toString());

        return resp;
    }
    public List<Field> getProcessFieldsAndProperties(String blockID){
        List<Field> res = new ArrayList<Field>();
        String params = "IBLOCK_TYPE_ID=bitrix_processes&IBLOCK_ID="+blockID;

        String response = callMethod("lists.field.get", params);

        try {
            Object o = new JSONParser().parse(response);
            JSONObject jo = (JSONObject) o;
            JSONObject result = (JSONObject) jo.get("result");
            Set<String> keys = result.keySet();

            for(String key: keys){
                JSONObject prop = (JSONObject) result.get(key);
                Field field = null;

                field = InitFieldOrPropertyFromJSON(prop);

                res.add(field);
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return res;
    }

    public Field getProcessFieldOrProperty(String blockID, String fieldID) {
        Field field = null;

        String params = "IBLOCK_TYPE_ID=bitrix_processes&IBLOCK_ID=" + blockID;

        String response = callMethod("lists.field.get", params);

        try{
            Object o = new JSONParser().parse(response);

            JSONObject jo = (JSONObject) o;
            JSONObject res = (JSONObject) jo.get("result");
            Set<String> keys = res.keySet();

            for(String key: keys){
                JSONObject prop = (JSONObject)res.get(key);
                String s_fieldID = prop.get("FIELD_ID").toString();

                if(s_fieldID.equals(fieldID)){
                    field = InitFieldOrPropertyFromJSON(prop);
                }
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return field;
    }

    private Field InitFieldOrPropertyFromJSON(JSONObject jField){
        Field field = null;

        String blockID = jField.get("IBLOCK_ID").toString();
        String fieldID = jField.get("FIELD_ID").toString();
        String name = jField.get("NAME").toString();

        if(fieldID.contains("PROPERTY_")){

            String s_type = jField.get("TYPE").toString();
            PropertyType type = Property.getPropertyTypeName(s_type);
            String id = jField.get("ID").toString();
            String code = jField.get("CODE").toString();

            if(type == PropertyType.List){
                field = new ListProperty();

                JSONObject elements = (JSONObject) jField.get("DISPLAY_VALUES_FORM");
                Set<String> keys2 = elements.keySet();

                for(String key2: keys2){
                    ((ListProperty)field).addElement(key2, (String) elements.get(key2));
                }
            }
            else if(type == PropertyType.EList){

                field = new EListProperty();

                String linkBlockID = jField.get("LINK_IBLOCK_ID").toString();
                ((EListProperty)field).setLinkBlockID(linkBlockID);
            }
            else if(type == PropertyType.Money){
                field = new MoneyProperty();
            }
            else if(type == PropertyType.SEmployee){
                field = new SEmployeeProperty();
            }
            else if(type == PropertyType.String){
                field = new StringProperty();
            }
            else if(type == PropertyType.File){
                field = new FileProperty();
            }
            else if(type == PropertyType.DetailText){
                field = new DetailTextProperty();
            }

            if(field != null){
                ((Property)field).setId(id);
                ((Property)field).setType(type);
                ((Property)field).setCode(code);
            }
        }

        if(field == null) {
            field = new Field();
        }

        if(jField.get("IS_REQUIRED") != null && jField.get("MULTIPLE") != null){
            String s_isRequired = jField.get("IS_REQUIRED").toString();
            String s_Multiple = jField.get("MULTIPLE").toString();
            Boolean isRequired = fromStringToBoolean(s_isRequired);
            Boolean multiple = fromStringToBoolean(s_Multiple);

            field.setRequired(isRequired);
            field.setMultiple(multiple);
        }

        field.setName(name);
        field.setFieldID(fieldID);
        field.setBlockID(String.valueOf(blockID));

        return field;
    }

    public List<BList> getLists(IBlockTypeID iBlockTypeID){
        List<BList> res = new ArrayList<BList>();
        String params = "IBLOCK_TYPE_ID=" + typeIDStringMap.get(iBlockTypeID);
        String response = callMethod("lists.get", params);

        try{
            Object obj = new JSONParser().parse(response);
            JSONObject jo = (JSONObject) obj;
            JSONArray result = (JSONArray) jo.get("result");
            Iterator resultIterator = result.iterator();

            while (resultIterator.hasNext()){
                JSONObject list = (JSONObject) resultIterator.next();
                BList newBProcess = new BList();
                String id = (String) list.get("ID");
                String iBlockTypeId = (String) list.get("IBLOCK_TYPE_ID");
                String name = (String) list.get("NAME");
                String description = (String) list.get("DESCRIPTION");
                String descriptionType = (String) list.get("DESCRIPTION_TYPE");
                String sectionsName = (String) list.get("SECTIONS_NAME");
                String sectionName = (String) list.get("SECTION_NAME");
                String elementsName = (String) list.get("ELEMENTS_NAME");
                String elementName = (String) list.get("ELEMENT_NAME");

                newBProcess.setId((Integer.parseInt(id)));
                newBProcess.setiBlockTypeID(iBlockTypeId);
                newBProcess.setName(name);
                newBProcess.setDescription(description);
                newBProcess.setDescriptionType(descriptionType);
                newBProcess.setSectionsName(sectionsName);
                newBProcess.setSectionName(sectionName);
                newBProcess.setElementsName(elementsName);
                newBProcess.setElementName(elementName);

                res.add(newBProcess);
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }
        return res;
    }

    public List<ListElement> getListElementsByBlockCode(String IBlockCode, String params){
        return getListsElements("IBLOCK_CODE=" + IBlockCode + "&" + params);
    }
    public List<ListElement> getListElementsByBlockID(String IBlockID, String params){
        return getListsElements("IBLOCK_ID=" + IBlockID + "&" + params);
    }
    private List<ListElement> getListsElements(String params){
        List<ListElement> bListElements = new ArrayList<ListElement>();
        String fullParams = null;
        String prefix = "IBLOCK_TYPE_ID=lists";

        if(params.isBlank() || params.isEmpty()){
            fullParams = prefix;
        }
        else {
            fullParams = prefix + "&" +  params;
        }

        String response = callMethod("lists.element.get", fullParams);

        try {
            Object o = new JSONParser().parse(response);
            JSONObject jo = (JSONObject) o;
            JSONArray result = (JSONArray) jo.get("result");
            Iterator resIter = result.iterator();

            while (resIter.hasNext()){
                JSONObject element = (JSONObject) resIter.next();
                ListElement bListElement = new ListElement();

                String id = (String) element.get("ID");
                String p_iBlockID = (String) element.get("IBLOCK_ID");
                String name = (String) element.get("NAME");
                String createdBy = (String) element.get("CREATED_BY");

                bListElement.setId(id);
                bListElement.setiBlockID(p_iBlockID);
                bListElement.setName(name);
                bListElement.setCreatedBy(createdBy);

                bListElements.add(bListElement);
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return bListElements;
    }

    /**
     * @return The return value is IBLOCK_ID. Its is null, If the list has not been added
     */
    public String addList(String iBlockCodeID, String name, String params){

        String iBlockID = null;
        String fullParams = null;
        String prefix = "IBLOCK_TYPE_ID=lists&IBLOCK_CODE=" + iBlockCodeID +"&FIELDS[Name]=" + name;

        if(params.isBlank() || params.isEmpty()){
            fullParams = prefix;
        }
        else{
            fullParams = prefix + "&" + params;
        }

        String response = callMethod("lists.add", fullParams);

        try{
            if(response != null){
                Object o = new JSONParser().parse(response);
                JSONObject jo = (JSONObject) o;
                iBlockID = (String) jo.get("result");
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return iBlockID;
    }

    /**
     * @return The return value is ELEMENT_ID. Its is null, If the list element has not been added
     */
    public String addListElementByIBlockCode(String iBlockCode, String elementCode, String params){
        return addListElement("IBLOCK_CODE=" + iBlockCode + "&ELEMENT_CODE=" + elementCode + "&" + params);
    }

    /**
     * @return The return value is ELEMENT_ID. Its is null, If the list element has not been added
     */
    public String addListElementByIBlockID(String iBlockID, String elementCode, String params){
        return addListElement("IBLOCK_ID=" + iBlockID + "&ELEMENT_CODE=" + elementCode + "&" + params);
    }

    private String addListElement(String params){
        String resp = null;
        String fullParams = null;
        String prefix = "IBLOCK_TYPE_ID=lists";

        if(params.isEmpty() || params.isBlank()){
            fullParams = prefix;
        }
        else {
            fullParams = prefix + "&" + params;
        }

        String response = callMethod("lists.element.add", fullParams);

        try{
            if(response != null){
                Object o = new JSONParser().parse(response);
                JSONObject jo = (JSONObject) o;
                resp = (String) jo.get("result");
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return resp;
    }
    private String callMethod(String method, String params){

        HttpsURLConnection connection = null;
        StringBuilder response = null;
        String request = null;

        params = params.replace(" ", "%20");

        if(params.isBlank() || params.isEmpty()){
            request = restURL + method;
        }
        else{
            request = restURL + method + "?" + params;
        }

        try {
            connection = (HttpsURLConnection)
                    new URL(request)
                            .openConnection();

            connection.setRequestMethod("GET");
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return true;
                }
            });

            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response =  new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }
            }
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        if(response != null){
            return response.toString();
        }
        else{
            return null;
        }
    }
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean fromStringToBoolean(String bool){
        if(bool == "Y")
        {
            return true;
        }
        else {
            return false;
        }
    }
}