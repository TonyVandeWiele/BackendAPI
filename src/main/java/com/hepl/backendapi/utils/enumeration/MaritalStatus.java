package com.hepl.backendapi.utils.enumeration;

public enum MaritalStatus {
    CELIBATAIRE("Célibataire"),
    MARIEE("Marié"),
    VEUF("Veuf"),
    DIVORCEE("Divorcé");

    private final String label;

    MaritalStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
