package com.kadiwhite.myForm.mvc;

import com.kadiwhite.myForm.bitrix24.Bitrix24;
import com.kadiwhite.myForm.bitrix24.ListElement;
import com.kadiwhite.myForm.bitrix24.lists.Process;
import com.kadiwhite.myForm.bitrix24.properties.EListProperty;
import com.kadiwhite.myForm.bitrix24.properties.Field;
import com.kadiwhite.myForm.bitrix24.properties.Property;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static Bitrix24 bitrix24 = new Bitrix24("https://10.0.0.88/rest/90/zlfo9zpu6rd0j3eb/");

    @RequestMapping("/")
    public String showMainPage(Model model){

        List<Process> processes = bitrix24.getProcesses(new String[0]);

        model.addAttribute("processes", processes);

        return "main-page";
    }

    @RequestMapping("/AddProcessElement")
    public String addProcessElement(@RequestParam("ProcessID") String processID, Model model){

        List<ListElement> statusListElements = new ArrayList<>();
        List<Field> fields = bitrix24.getProcessFieldsAndProperties(processID);

        model.addAttribute("fields", fields);
        model.addAttribute("bx24", bitrix24);

        for(Field field: fields){
            if(field.getFieldID().contains("PROPERTY_")){
                Property property = (Property) field;

                if(property.getCode().equals("PROEKT")){

                    EListProperty eListProperty = (EListProperty) property;
                    List<ListElement> listElements1 = bitrix24.getListElementsByBlockID(eListProperty.getLinkBlockID(), "FILTER[PROPERTY_418]=341");
                    List<ListElement> listElements2 = bitrix24.getListElementsByBlockID(eListProperty.getLinkBlockID(), "FILTER[PROPERTY_418]=342");

                    for(ListElement listElement: listElements1){
                        statusListElements.add(listElement);
                    }

                    for(ListElement listElement: listElements2){
                        statusListElements.add(listElement);
                    }

                    model.addAttribute("statusElements", statusListElements);
                }
            }
        }

        return "AddProcessElement";
    }
}