package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.modelos.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class CustomUsuarioRepositoryImpl implements ICustomUsuarioRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public void actualizar(Usuario usuario) {
        usuario.valido(false);
        TypedQuery<Usuario> q = em.createQuery("UPDATE Usuario u SET u.nombre=:nombre, u.email=:email, u.alta=:alta, u.activo=:activo WHERE u.id=:id", Usuario.class);
        q.setParameter("nombre", usuario.getNombre());
        q.setParameter("email", usuario.getEmail());
        q.setParameter("alta", usuario.getAlta());
        q.setParameter("activo", usuario.isActivo());
        q.setParameter("id", usuario.getId());
        q.executeUpdate();
    }
}
