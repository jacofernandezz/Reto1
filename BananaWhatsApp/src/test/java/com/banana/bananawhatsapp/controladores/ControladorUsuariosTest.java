package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
class ControladorUsuariosTest {

    @Autowired
    ControladorUsuarios controladorUsuarios;

    @Autowired
    IUsuarioRepository repoUser;

    @BeforeEach
    void cleanAndReloadData() {
        DBUtil.reloadDB();
    }

    @Test
    void testBeans(){
        assertNotNull(controladorUsuarios);
        assertNotNull(repoUser);
    }

    @Test
    void dadoUsuarioValido_cuandoAlta_entoncesUsuarioValido() {
        Usuario nuevo = new Usuario(null, "Ricardo", "r@r.com", LocalDate.now(), true);
        controladorUsuarios.alta(nuevo);

        assertThat(nuevo, notNullValue());
        assertThat(nuevo.getId(), greaterThan(0));
    }

    @Test
    void dadoUsuarioNOValido_cuandoAlta_entoncesExcepcion() {
        Usuario user = new Usuario(null, "Gema", "g@gccom", LocalDate.now(), true);
        assertThrows(Exception.class, () -> {
            controladorUsuarios.alta(user);
        });
    }

    @Test
    @Transactional
    void dadoUsuarioValido_cuandoActualizar_entoncesUsuarioValido() {
        Integer idUser = 2;
        LocalDate fecha = LocalDate.parse("2023-12-17");
        Usuario user = repoUser.getReferenceById(idUser);
        user.setNombre("Juan Luis");
        user.setEmail("jl@jll.com");
        user.setAlta(fecha);
        controladorUsuarios.actualizar(user);
        assertThat(repoUser.getReferenceById(idUser).getNombre(), is("Juan Luis"));
    }

    @Test
    @Transactional
    void dadoUsuarioNOValido_cuandoActualizar_entoncesExcepcion() {
        assertThrows(UsuarioException.class, () -> {
            Integer idUser = 2;
            LocalDate fecha = LocalDate.parse("2025-12-17");
            Usuario user = repoUser.getReferenceById(idUser);
            user.setNombre("Juan Luis");
            user.setEmail("jl@jll.com");
            user.setAlta(fecha);
            controladorUsuarios.actualizar(user);
        });
    }

    @Test
    @Transactional
    void dadoUsuarioValido_cuandoBaja_entoncesUsuarioValido() {
        Usuario user = repoUser.getReferenceById(1);
        ResponseEntity<Boolean> response = controladorUsuarios.baja(user.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void dadoUsuarioNOValido_cuandoBaja_entoncesExcepcion() {
        Usuario user = new Usuario(-1, null, null, null, true);
        assertThrows(Exception.class, () -> {
            controladorUsuarios.baja(user.getId());
        });
    }
}