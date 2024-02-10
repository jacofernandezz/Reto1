package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> , CustomUsuarioRepository{

    @Query("SELECT DISTINCT u FROM Usuario u WHERE u.id <> :id AND u.activo = true")
    List<Usuario> obtenerPosiblesDestinatariosValidado(@Param("id")Integer id, Pageable pageable);

    default List<Usuario> obtenerPosiblesDestinatarios(Integer idUsuario, Pageable pageable) {
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        if (!usuario.validarId()) {
            throw new UsuarioException("El ID de usuario no es válido");
        }
        return obtenerPosiblesDestinatariosValidado(idUsuario, pageable);
    }

}
