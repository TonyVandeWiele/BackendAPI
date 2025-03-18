package com.hepl.backendapi.exception;

public class RessourceNotFoundException extends RuntimeException {
    public RessourceNotFoundException(String missingRessource, Long id) {
        super("Ressource(s) (" + missingRessource + ") Not Found at ID : " + id);
    }
    public RessourceNotFoundException(String missingRessource, String object) {
        super("Ressource(s) (" + missingRessource + ") Not Found for : " + object);
    }
}
