package com.example.week6.view;

import com.example.week6.pojo.Wizard;
import com.example.week6.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Route(value = "mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField nameField;
    private NumberField dollarsField;
    private RadioButtonGroup<String> gender;
    private ComboBox position, school, house;
    private Button back, create, update, delete, next;
    private Wizards wizards;
    private int Index = 0;



    public MainWizardView(){
        this.wizards = new Wizards();

        nameField = new TextField("","FullName");

        gender = new RadioButtonGroup<String>("Gender : ");
        gender.setItems("Male", "Female");

        position = new ComboBox();
        position.setPlaceholder("Position");
        position.setItems("Student", "Teacher");

        dollarsField = new NumberField("Dollars");
        dollarsField.setPrefixComponent(new Paragraph("$ "));

        school = new ComboBox();
        school.setPlaceholder("School");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        house = new ComboBox();
        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");

        back = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        next = new Button(">>");

        HorizontalLayout btnhori = new HorizontalLayout();
        btnhori.add(back, create, update, delete, next);

        add(nameField, gender, position, dollarsField, school, house, btnhori);
        this.fetchWizards();
//        List<Wizard> w = WebClient
//                .create()
//                .get()
//                .uri("http://localhost:8080/wizards")
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<Wizard>>() {})
//                .block();
//        nameField.setValue(w.get(Index).getName());
//        gender.setValue(w.get(Index).getSex() == 'm' ? "Male" : "Female");
//        position.setValue(w.get(Index).getPosition().equals("student") ? "Student" : "Teacher");
//        dollarsField.setValue((double) w.get(Index).getMoney());
//        school.setValue(w.get(Index).getSchool());
//        house.setValue(w.get(Index).getHouse());

//        wizards.setWizard(w);
//        this.updateAll();

        back.addClickListener(e -> {
            Index = Math.max(Index-1, 0);
            this.updateAll();
        });

        create.addClickListener(e -> {
            String name = nameField.getValue();
            char sex = gender.getValue().equals("Male") ? 'm' : 'f';
            String posi = position.getValue().equals("Student") ? "student" : "teacher";
            int money = dollarsField.getValue().intValue();
            String sch = school.getValue().toString();
            String hou = house.getValue().toString();

            Wizard wiz = new Wizard(null, name, sex, posi, money, sch, hou);
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(wiz), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            Index = wizards.getWizard().size();
            this.updateAll();
        });

        update.addClickListener(e ->{
           String id = wizards.getWizard().get(Index).getId();
           String name = nameField.getValue();
           char sex = gender.getValue().equals("Male") ? 'm' : 'f';
           String posi = position.getValue().toString();
           int money = dollarsField.getValue().intValue();
           String sch = school.getValue().toString();
           String hou = house.getValue().toString();

            WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .body(Mono.just(new Wizard(id, name, sex, posi, money, sch, hou)), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
        });

        delete.addClickListener(e -> {
            String id = wizards.getWizard().get(Index).getId();
            String name = nameField.getValue();
            char sex = gender.getValue().equals("Male") ? 'm' : 'f';
            String posi = position.getValue().toString();
            int money = dollarsField.getValue().intValue();
            String sch = school.getValue().toString();
            String hou = house.getValue().toString();

            WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(wizards.getWizard().get(Index)), Wizard.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            Index = 0;
            this.fetchWizards();
        });

        next.addClickListener(e -> {
            Index = Math.min(Index+1, wizards.getWizard().size()-1);
            this.updateAll();
        });


    }

    private void fetchWizards() {
        List<Wizard> w = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Wizard>>() {})
                .block();
        this.wizards.setWizard(w);
        this.updateAll();
    }

    private void updateAll() {
        back.setEnabled(!(Index == 0));
        nameField.setValue(wizards.getWizard().get(Index).getName());
        gender.setValue(wizards.getWizard().get(Index).getSex() == 'm' ? "Male" : "Female");
        position.setValue(wizards.getWizard().get(Index).getPosition().equals("student") ? "Student" : "Teacher");
        dollarsField.setValue((double) wizards.getWizard().get(Index).getMoney());
        school.setValue(wizards.getWizard().get(Index).getSchool());
        house.setValue(wizards.getWizard().get(Index).getHouse());
        next.setEnabled(!(Index == wizards.getWizard().size()-1));

    }
}
