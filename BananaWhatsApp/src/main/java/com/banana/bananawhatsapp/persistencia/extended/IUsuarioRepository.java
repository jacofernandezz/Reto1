package com.banana.bananawhatsapp.persistencia.extended;

import com.banana.bananawhatsapp.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Set;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.id = :id")
    int borrar(@Param("id") Integer id);

//    public Usuario obtener(int id) throws SQLException;
//    public Usuario crear(Usuario usuario) throws SQLException;
//
//    public Usuario actualizar(Usuario usuario) throws SQLException;
//
//    public boolean borrar(Usuario usuario) throws SQLException;
//
//    public Set<Usuario> obtenerPosiblesDestinatarios(Integer id, Integer max) throws SQLException;

}
