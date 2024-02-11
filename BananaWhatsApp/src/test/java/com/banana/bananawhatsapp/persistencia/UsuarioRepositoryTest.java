package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
class UsuarioRepositoryTest {
    @Autowired
    IUsuarioRepository repo;

    @BeforeEach
    void cleanAndReloadData() {
        DBUtil.reloadDB();
    }

    @Test
    void testBeans(){
        assertNotNull(repo);
    }

    @Test
    @Order(1)
    void dadoUnUsuarioValido_cuandoCrear_entoncesUsuarioValido() {
        Usuario nuevo = new Usuario(null, "Ricardo", "r@r.com", LocalDate.now(), true);
        repo.save(nuevo);

        assertThat(nuevo, notNullValue());
        assertThat(nuevo.getId(), greaterThan(0));
    }

    @Test
    @Order(2)
    void dadoUnUsuarioNOValido_cuandoCrear_entoncesExcepcion() {
        Usuario nuevo = new Usuario(null, "Ricardo", "r", LocalDate.now(), true);
        assertThrows(UsuarioException.class, () -> {
            repo.save(nuevo);
        });
    }

    @Test
    @Order(3)
    void dadoUnUsuarioValido_cuandoActualizar_entoncesUsuarioValido() {
        Integer idUser = 1;
        Usuario user = new Usuario(idUser, "Juan", "j@j.com", LocalDate.now(), true);
        Usuario userUpdate = repo.save(user);
        assertThat(userUpdate.getNombre(), is("Juan"));
    }

    @Test
    @Order(4)
    @Transactional
    void dadoUnUsuarioNOValido_cuandoActualizar_entoncesExcepcion() {
        Integer idUser = -1;
        Usuario user = new Usuario(idUser, "Juan", "j@j.com", LocalDate.now(), true);
        assertThrows(UsuarioException.class, () -> {
           repo.actualizar(user);
        });
    }

    @Test
    @Order(5)
    void dadoUnUsuarioValido_cuandoBorrar_entoncesOK() {
        Usuario user = new Usuario(1, null, null, null, true);
        boolean ok = repo.borrar(user.getId());
        assertTrue(ok);
    }

    @Test
    @Order(6)
    void dadoUnUsuarioNOValido_cuandoBorrar_entoncesExcepcion() {
        Usuario user = new Usuario(-1, null, null, null, true);
        assertThrows(Exception.class, () -> {
            repo.deleteById(user.getId());
        });
    }

    @Test
    @Order(7)
    void dadoUnUsuarioValido_cuandoObtenerPosiblesDestinatarios_entoncesLista() {
        Integer idUser = 1;
        int numPosibles = 100;
        Usuario user = new Usuario(idUser, "Juan", "j@j.com", LocalDate.now(), true);
        List<Usuario> conjuntoDestinatarios = repo.obtenerPosiblesDestinatarios(user.getId(), PageRequest.of(0, numPosibles));
        assertTrue(conjuntoDestinatarios.size() <= numPosibles);
    }

    @Test
    @Order(8)
    void dadoUnUsuarioNOValido_cuandoObtenerPosiblesDestinatarios_entoncesExcepcion() {
        Usuario user = new Usuario(-1, null, null, null, true);
        int numPosibles = 100;
        assertThrows(UsuarioException.class, () -> {
            repo.obtenerPosiblesDestinatarios(user.getId(), PageRequest.of(0, numPosibles));
        });
    }


}