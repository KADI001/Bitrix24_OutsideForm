package com.example.application.views.main;

import com.example.application.bitrix24.Bitrix24;
import com.example.application.bitrix24.ListElement;
import com.example.application.bitrix24.properties.ListProperty;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Route("/ISPC3NAOPLATYRABOTIUSLUG")
public class ISP_C3_NA_OPLATY_RABOT_I_USLUG extends VerticalLayout {

    private final String tipZatratID = "82";
    private final String ndID = "79";
    private final String projectsID = "70";
    private final String statyaZatratId = "80";
    private final String tipProektaID = "71";
    private final String godProektaID = "81";
    private final String processID = "78";
    private final String params = "FILTER[PROPERTY_418][]=341&FILTER[PROPERTY_418][]=342&FILTER[PROPERTY_419]=";
    private static Bitrix24 bitrix24 = new Bitrix24("https://10.0.0.88/rest/90/zlfo9zpu6rd0j3eb/");
    private static Random random = new Random();
    private static Base64.Encoder encoder =  Base64.getEncoder();

    private TextField name;
    private TextField summa;

    private MultiFileMemoryBuffer buffer;
    private Upload skanSchyeta;

    private Map<String, String> encodedFile;
    private TextArea obosnovanie;
    private Select<String> napravlenieDeyatelnosti;
    private Select<String> proekt;
    private Select<String> tipProekta;
    private Select<String> godProekta;
    private Select<String> tipZatrat;
    private Select<String> statyaZatrat;

    private Button createProcess;

    public ISP_C3_NA_OPLATY_RABOT_I_USLUG() {
        name = createTextField("Название");
        summa = createTextField("Сумма (в рублях)");

        encodedFile = new HashMap<>();
        buffer = new MultiFileMemoryBuffer();
        skanSchyeta = new Upload(buffer);

        skanSchyeta.addSucceededListener(e ->{
            String fileName = e.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            try{
                encodedFile.put(fileName, encoder.encodeToString(inputStream.readAllBytes()));
            }
            catch (Throwable cause){
                cause.printStackTrace();
            }
        });

        obosnovanie = createTextArea("Обоснование", "");
        napravlenieDeyatelnosti = createNDSelect("Направление деятельности");
        proekt = createStatusSelect("Проект");
        godProekta = createEListSelect("Год проекта", godProektaID);
        tipProekta = createEListSelect("Тип проекта", tipProektaID);
        statyaZatrat = createEListSelect("Статья затрат", statyaZatratId);
        tipZatrat = createEListSelect("Тип затрат", tipZatratID);

        createProcess = new Button("Create");
        createProcess.addClickListener(e -> {
            Map<String, Object> fields = new HashMap<>(){
                {
                    put("NAME", name.getValue());
                    put("SUMMA", summa.getValue());
                    put("DETAIL_TEXT", obosnovanie.getValue());
                    put("NAPRAVLENIE_DEYATELNOSTI", napravlenieDeyatelnosti.getValue());
                    put("PROEKT", proekt.getValue());
                    put("STATYA_ZATRAT", statyaZatrat.getValue());
                    put("TIP_ZATRAT", tipZatrat.getValue());
                    put("TIP_PROEKTA", tipProekta.getValue());
                    put("GOD_PROEKTA", godProekta.getValue());
                }
            };

            Set<String> files = encodedFile.keySet();

            for(String key: files){
                String[] fileInfo = new String[2];
                fileInfo[0] = key;
                fileInfo[1] = encodedFile.get(key);
                fields.put("SKAN_SCHYETA", fileInfo);
            }

            bitrix24.addProcessElement(processID, String.valueOf(random.nextInt(99999)), fields);
        });


        add(name);
        add(summa);
        add(skanSchyeta);
        add(obosnovanie);
        add(napravlenieDeyatelnosti);
        add(proekt);
        add(tipProekta);
        add(godProekta);
        add(tipZatrat);
        add(statyaZatrat);
        add(createProcess);
    }

    private TextArea createTextArea(String label, String placeHolder) {
        TextArea textArea = new TextArea();
        textArea.setLabel(label);
        textArea.setPlaceholder(placeHolder);
        textArea.setMaxLength(500);
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + textArea.getMaxLength());
        });
        return textArea;
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
        Select<String> oldProjects = proekt;
        proekt = createStatusSelect(oldProjects.getLabel());
        replace(oldProjects, proekt);
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

    public String convertFileToStringBase64(File file){
        String res = null;

        try {
            res = encoder.encodeToString(FileUtils.readFileToByteArray(file));
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return res;
    }
}
