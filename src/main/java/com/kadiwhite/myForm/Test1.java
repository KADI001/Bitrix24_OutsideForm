package com.kadiwhite.myForm;

import com.kadiwhite.myForm.bitrix24.Bitrix24;
import com.kadiwhite.myForm.bitrix24.ListElement;
import com.kadiwhite.myForm.bitrix24.properties.Field;
import com.kadiwhite.myForm.bitrix24.properties.Property;

import java.util.HashMap;
import java.util.List;

public class Test1 {
    public static void main(String[] args) {

        Bitrix24 bitrix24 = new Bitrix24("https://10.0.0.88/rest/90/zlfo9zpu6rd0j3eb/");

//        String resp = bitrix24.addProcessElement("40", "776", new HashMap<>()
//        {
//            {
//                put("NAME", "Test7799999");
//                put("recipient", "90");
//                put("SUMMA", "123000");
//                put("isBusinessTrip", "Нет");
//                put("PurchaseTMZ", "Да");
//                put("NAPRAVLENIE_DEYATELNOSTI", "Кухня");
//                put("PROEKT", "60443");
//                put("STATYA_ZATRAT", "Аренда волокон");
//                put("TIP_ZATRAT", "Капитальные");
//                put("TIP_PROEKTA", "Демонтаж");
//                put("GOD_PROEKTA", "2021");
//            }
//        });
//
//        System.out.println(resp);

        List<ListElement> listElements = bitrix24.getListElementsByBlockID("70", "FILTER[PROPERTY_418]=341&FILTER[PROPERTY_418]=342");
        ListElement listElement = new ListElement();
        listElement.getName();
        System.out.println("");

//        List<Field> properties = bitrix24.getProcessFieldsAndProperties("40");
//
//        for(Field property: properties){
//            System.out.println("FieldID: " + property.getName());
//
////            if(property.getType() == PropertyType.List){
////                BListProperty listProperty = (BListProperty) property;
////                Set<String> keys = listProperty.getKeyValueMap().keySet();
////                for (String key: keys){
////                    System.out.println("Name: " + listProperty.getName() + " Key: " + key + " Value: " + listProperty.getValue(key) + "\n");
////                }
////            }
//        }


        //String result1 = bitrix24.addList("25", "TestDK", "");

        //String result2 = bitrix24.addListElementByIBlockCode("25", "14144", "FIELDS[NAME]=Test 123414");

//        List<BList> listElements = bitrix24.getLists(IBlockTypeID.Bitrix_Processes);
//
//        for(BList list: listElements){
//            System.out.println(list.getId());
//            System.out.println(list.getName());
//            System.out.println("\n");
//        }

//        List<BProcess> listElements = bitrix24.getProcesses(new String[]{ "DESCRIPTION_TYPE" });
//
//        for(BProcess list: listElements){
//            System.out.println(list.getId());
//            System.out.println(list.getName());
//
//            for(Object field: list.getFields().values())
//            {
//                System.out.println((String) field);
//            }
//
//            System.out.println("\n");
//        }
    }
}