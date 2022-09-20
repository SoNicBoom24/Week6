package com.example.week6.repository;

import com.example.week6.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public List<Wizard> getWizards(){
        return repository.findAll();
    }

    public Wizard addWizard(Wizard w){
        w.setId(null);
        return repository.save(w);
    }

    public Wizard updateWizard(Wizard w){
        return repository.save(w);
    }

    public boolean deleteWizard(Wizard w){
        repository.deleteById(w.getId());
        return true;
    }
}
