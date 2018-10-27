package com.bpcbt.lessons.spring.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Manager {
    private String editedName = "default";

    public String getEditedName() {
        return editedName;
    }

    public void setEditedName(String editedName) {
        this.editedName = editedName;
    }

    public void goToWelcomePage() {

    }
}
