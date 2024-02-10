package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMensajeRepository  extends JpaRepository<Mensaje, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Mensaje m SET m.remitente = null WHERE m.remitente = :usuario")
    void actualizarReferenciasRemitente(Usuario usuario);

    @Transactional
    @Modifying
    @Query("UPDATE Mensaje m SET m.destinatario = null WHERE m.destinatario = :usuario")
    void actualizarReferenciasDestinatario(Usuario usuario);

    @Transactional
    @Modifying
    @Query("DELETE FROM Mensaje m WHERE m.remitente IS NULL AND m.destinatario IS NULL")
    void eliminarMensajesConReferenciasNulas();

    @Query("SELECT m FROM Mensaje m WHERE m.remitente.id = :id OR m.destinatario.id = :id")
    List<Mensaje> obtenerValidado(@Param("id")Integer idUsuario);

    default List<Mensaje> obtener(Usuario usuario) {
        usuario.valido(false);
        return obtenerValidado(usuario.getId());
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Mensaje m WHERE m.remitente.id = :idRemitente AND m.destinatario.id = :idDestinatario")
    int borrarEntreValidado(Integer idRemitente, Integer idDestinatario);

    default Boolean borrarEntre(Usuario remitente, Usuario destinatario) {
        remitente.valido(false);
        destinatario.valido(false);
        return borrarEntreValidado(remitente.getId(), destinatario.getId()) > 0;
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Mensaje m WHERE m.remitente.id = :id OR m.destinatario.id = :id")
    int borrarTodosValidado(@Param("id")Integer idUsuario);

    default Boolean borrarTodos(Usuario usuario) {
        usuario.valido(false);
        return borrarTodosValidado(usuario.getId()) > 0;
    }
}
