package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioUsuarioImpl implements IServicioUsuarios{

    @Autowired
    private IUsuarioRepository repo;

    @Override
    public Usuario crearUsuario(Usuario usuario) throws UsuarioException {
        return repo.save(usuario);
    }

    @Override
    public boolean borrarUsuario(Integer idUsuario) throws UsuarioException {
        return repo.borrar(idUsuario);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws UsuarioException {
        repo.actualizar(usuario);
        return usuario;
    }

    @Override
    public List<Usuario> obtenerPosiblesDesinatarios(Usuario usuario, int max) throws UsuarioException {
        return repo.obtenerPosiblesDestinatarios(usuario.getId(), PageRequest.of(0, max));
    }
}
