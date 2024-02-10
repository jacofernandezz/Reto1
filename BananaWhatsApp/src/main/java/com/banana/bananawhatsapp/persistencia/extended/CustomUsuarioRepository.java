package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.modelos.Usuario;

public interface CustomUsuarioRepository {

    void actualizar(Usuario usuario);

    boolean borrar(Integer idUsuario);
}
