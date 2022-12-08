package com.example.application.views.main;

import com.example.application.Application;
import com.example.application.bitrix24.Bitrix24;
import com.example.application.bitrix24.lists.Process;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Pane;
import com.vaadin.flow.component.charts.model.PaneList;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.internal.StateNode;
import com.vaadin.flow.internal.nodefeature.TextNodeMap;
import com.vaadin.flow.router.Route;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

import java.awt.*;
import java.util.List;

@Route("/")
public class MainPage extends VerticalLayout {

    private Bitrix24 bitrix24 = new Bitrix24("https://10.0.0.88/rest/90/zlfo9zpu6rd0j3eb/");
    private List<Process> processes;

    public MainPage() {
        Process ispC3NaVidachyAvansa = bitrix24.getProcess("40", new String[0]);
        Process ispC3NaOplatyRabotIUslug = bitrix24.getProcess("18", new String[0]);
        Process ispC3NaOplatyScheta = bitrix24.getProcess("78", new String[0]);

        Label label1 = new Label(ispC3NaVidachyAvansa.getName());
        Label label2 = new Label(ispC3NaOplatyRabotIUslug.getName());
        Label label3 = new Label(ispC3NaOplatyScheta.getName());

        Button button1 = addNavigateButton("Add", "/ISPC3NAVIDACHUAVANSA");;
        Button button2 = addNavigateButton("Add", "/ISPC3NAOPLATYRABOTIUSLUG");
        Button button3 = addNavigateButton("Add", "/ISPC3NAOPLATYSCHETA");

        add(label1);
        add(button1);
        add(new Paragraph());
        add(label2);
        add(button2);
        add(new Paragraph());
        add(label3);
        add(button3);
    }

    private Button addNavigateButton(String text, String url){
        Button button = new Button();
        button.setText(text);
        button.addClickListener(e -> button.getUI().ifPresent(ui -> ui.navigate(url)));

        return button;
    }
}
