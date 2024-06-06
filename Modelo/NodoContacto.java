package org.example.Modelo;
import java.io.Serializable;

// La clase NodoContacto también implementa Serializable para permitir la serialización del nodo.
public class NodoContacto implements Serializable {//a esta clase solo se le agrego e implemento  el Serializable
    private Contacto contacto;
    private NodoContacto izdo;
    private NodoContacto dcho;

    // Constructor de la clase NodoContacto
    public NodoContacto(Contacto contacto) {
        this.contacto = contacto;
        this.izdo = null;
        this.dcho = null;
    }

    // Métodos getter y setter para cada atributo
    public Contacto getContacto() {
        return contacto;
    }

    public NodoContacto getIzdo() {
        return izdo;
    }

    public NodoContacto getDcho() {
        return dcho;
    }

    public void setIzdo(NodoContacto izdo) {
        this.izdo = izdo;
    }

    public void setDcho(NodoContacto dcho) {
        this.dcho = dcho;
    }
}