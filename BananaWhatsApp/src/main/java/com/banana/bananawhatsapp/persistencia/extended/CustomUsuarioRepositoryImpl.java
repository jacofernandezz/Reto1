package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class CustomUsuarioRepositoryImpl implements CustomUsuarioRepository {

    @PersistenceContext
    EntityManager em;

    @Autowired
    IMensajeRepository mensajeRepo;

    @Override
    public void actualizar(Usuario usuario) {
        usuario.valido(false);
        Query q = em.createQuery("UPDATE Usuario u SET u.nombre=:nombre, u.email=:email, u.alta=:alta, u.activo=:activo WHERE u.id=:id");
        q.setParameter("nombre", usuario.getNombre());
        q.setParameter("email", usuario.getEmail());
        q.setParameter("alta", usuario.getAlta());
        q.setParameter("activo", usuario.isActivo());
        q.setParameter("id", usuario.getId());
        q.executeUpdate();
    }

    @Transactional
    @Override
    public boolean borrar(Integer idUsuario) {

        try {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                // Actualizamos las referencias, mientras una de las partes siga siendo un usuario de la aplicacion
                // es conveniente guardar la informacion de sus mensajes:
                mensajeRepo.actualizarReferenciasRemitente(usuario);
                mensajeRepo.actualizarReferenciasDestinatario(usuario);
                //Si ninguna de las partes es ya usuaria eliminamos los mensajes.
                mensajeRepo.eliminarMensajesConReferenciasNulas();

                em.remove(usuario);
                return true;
            } else {
                throw new UsuarioException("Error al encontrar el usuario");
            }
        } catch (Exception e) {
            throw new UsuarioException("Error al intentar borrar el usuario");
        }
    }
}
