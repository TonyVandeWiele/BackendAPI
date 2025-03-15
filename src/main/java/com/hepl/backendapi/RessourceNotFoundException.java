package com.hepl.backendapi;

public class RessourceNotFoundException extends RuntimeException {
    public RessourceNotFoundException(String missingRessource, Long id) {
        super("Ressource(s) (" + missingRessource + ") Not Found at ID : " + id);
    }
}
