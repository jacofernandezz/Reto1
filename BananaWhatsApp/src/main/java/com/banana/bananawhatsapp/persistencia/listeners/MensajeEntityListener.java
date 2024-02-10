package com.banana.bananawhatsapp.persistencia.listeners;

import com.banana.bananawhatsapp.modelos.Mensaje;

import javax.persistence.PrePersist;

public class MensajeEntityListener {

    @PrePersist
    void prePersist(Mensaje mensaje) {
        mensaje.valido();
    }
}
