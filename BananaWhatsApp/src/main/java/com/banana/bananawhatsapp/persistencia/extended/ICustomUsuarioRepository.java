package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.modelos.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface ICustomUsuarioRepository {

    void actualizar(Usuario usuario);
}
