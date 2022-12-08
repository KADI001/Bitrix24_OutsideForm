package com.example.application.views.main;

import com.example.application.bitrix24.Bitrix24;
import com.example.application.bitrix24.ListElement;
import com.example.application.bitrix24.properties.ListProperty;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.swing.text.Document;
import java.util.*;

@Route("/ISPC3NAVIDACHUAVANSA")
public class ISP_C3_NA_VIDACHU_AVANSA extends VerticalLayout {

    private final String tipZatratID = "82";
    private final String processID = "40";
    private final String ndID = "79";
    private final String projectsID = "70";
    private final String statyaZatratId = "80";
    private final String tipProektaID = "71";
    private final String godProektaID = "81";
    private final String params = "FILTER[PROPERTY_418][]=341&FILTER[PROPERTY_418][]=342&FILTER[PROPERTY_419]=";
    private static Bitrix24 bitrix24 = new Bitrix24("https://10.0.0.88/rest/90/zlfo9zpu6rd0j3eb/");
    private static Random random = new Random();

    private TextField obosnovanie;
    private TextField recipient;
    private TextField summa;
    private Select<String> isBusinessTrip;
    private Select<String> purchaseTMZ;
    private Select<String> napravlenieDeyatelnosti;
    private Select<String> projects;
    private Select<String> statyaZatrat;
    private Select<String> tipZatrat;
    private Select<String> tipProekta;
    private Select<String> godProekta;
    private Button createProcess;


    public ISP_C3_NA_VIDACHU_AVANSA() {
        obosnovanie = createTextField("Обоснование");
        recipient = createTextField("Кому выдать деньги?");
        summa = createTextField("Сумма (в рублях)");
        isBusinessTrip = createListSelect("Это командировка?", "PROPERTY_163");
        purchaseTMZ = createListSelect("На закупку материалов?", "PROPERTY_164");
        napravlenieDeyatelnosti = createNDSelect("Направление деятельности");
        projects = createStatusSelect("Проекты");
        statyaZatrat = createEListSelect("Статья затрат", statyaZatratId);
        tipZatrat = createEListSelect("Тип затрат", tipZatratID);
        tipProekta = createEListSelect("Тип проекта", tipProektaID);
        godProekta = createEListSelect("Год проекта", godProektaID);

        createProcess = new Button("Create");
        createProcess.addClickListener(e -> {
            bitrix24.addProcessElement(processID, String.valueOf(random.nextInt(99999)), new HashMap<>(){
                {
                    put("NAME", obosnovanie.getValue());
                    put("recipient", recipient.getValue());
                    put("SUMMA", summa.getValue());
                    put("isBusinessTrip", isBusinessTrip.getValue());
                    put("PurchaseTMZ", purchaseTMZ.getValue());
                    put("NAPRAVLENIE_DEYATELNOSTI", napravlenieDeyatelnosti.getValue());
                    put("PROEKT", projects.getValue());
                    put("STATYA_ZATRAT", statyaZatrat.getValue());
                    put("TIP_ZATRAT", tipZatrat.getValue());
                    put("TIP_PROEKTA", tipProekta.getValue());
                    put("GOD_PROEKTA", godProekta.getValue());
                }
            });
        });

        add(obosnovanie);
        add(recipient);
        add(summa);
        add(isBusinessTrip);
        add(purchaseTMZ);
        add(napravlenieDeyatelnosti);
        add(projects);
        add(statyaZatrat);
        add(tipZatrat);
        add(tipProekta);
        add(godProekta);
        add(createProcess);
    }

    private TextField createTextField(String placeHolder) {
        TextField textField = new TextField();
        textField.setPlaceholder(placeHolder);
        return textField;
    }

    private Select<String> createListSelect(String label, String fieldID) {
        Select<String> select = new Select<>();
        select.setLabel(label);
        List<String> items = new ArrayList<>();
        ListProperty prop = (ListProperty) bitrix24.getProcessFieldOrProperty(processID, fieldID);
        Set<String> propElements = prop.getKeyValueMap().keySet();
        for(String key: propElements){
            items.add(prop.getValue(key));
        }
        select.setItems(items);
        select.setValue(items.get(0));
        
        return select;
    }

    private Select<String> createEListSelect(String label, String listID) {
        Select<String> select = new Select<>();
        select.setLabel(label);
        List<ListElement> gp_elements = bitrix24.getListElementsByBlockID(listID, "");
        List<String> godp_elementsName = new ArrayList<>();
        for(ListElement element: gp_elements){
            godp_elementsName.add(element.getName());
        }
        select.setItems(godp_elementsName);
        select.setValue(godp_elementsName.get(0));

        return select;
    }

    private Select<String> createNDSelect(String label) {
        Select<String> select = createEListSelect(label, ndID);

        select.addValueChangeListener(value -> {
            updateStatusSelect();
        });

        return select;
    }

    public void updateStatusSelect(){
        Select<String> oldProjects = projects;
        projects = createStatusSelect(oldProjects.getLabel());
        replace(oldProjects, projects);
    }

    private Select<String> createStatusSelect(String label) {
        Select<String> select = new Select<>();
        select.setLabel(label);
        ListElement el = bitrix24.getListElementByName("lists", ndID, napravlenieDeyatelnosti.getValue());
        List<ListElement> elements = bitrix24.getListElementsByBlockID(projectsID, params + el.getId());

        if(elements.size() == 0){
            select.setItems(new String[0]);
        }
        else{
            List<String> elementsName = new ArrayList<>();

            for(ListElement element: elements){
                elementsName.add(element.getName());
            }

            select.setItems(elementsName);
            select.setValue(elements.get(0).getName());
        }

        return select;
    }
}