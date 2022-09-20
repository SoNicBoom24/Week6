package com.example.week6.pojo;

import java.util.ArrayList;
import java.util.List;


public class Wizards {
    private List<Wizard> wizards;

    public Wizards() {
        this.wizards = new ArrayList<Wizard>();
    }

    public List<Wizard> getWizard() {
        return wizards;
    }

    public void setWizard(List<Wizard> wizards) {
        this.wizards = wizards;
    }
}
