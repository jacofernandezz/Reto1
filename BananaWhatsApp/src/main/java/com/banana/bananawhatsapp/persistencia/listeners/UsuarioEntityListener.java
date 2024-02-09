package com.banana.bananawhatsapp.persistencia.listeners;

import com.banana.bananawhatsapp.modelos.Usuario;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class UsuarioEntityListener {

    @PrePersist
    void prePersist(Usuario usuario) {
        usuario.valido(true);
    }

    @PreUpdate
    void preUpdate(Usuario usuario) {
        usuario.valido(false);
    }

    @PreRemove
    void preRemove(Usuario usuario) {
        usuario.validarId();
    }

}
