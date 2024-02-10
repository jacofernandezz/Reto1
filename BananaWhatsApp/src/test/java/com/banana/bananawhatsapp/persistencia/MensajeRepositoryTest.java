package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
class MensajeRepositoryTest {

    @Autowired
    IUsuarioRepository repoUsuario;

    @Autowired
    IMensajeRepository repoMensaje;

    @BeforeEach
    void cleanAndReloadData() {
        DBUtil.reloadDB();
    }

    @Test
    @Order(1)
    @Transactional
    void dadoUnMensajeValido_cuandoCrear_entoncesMensajeValido() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);
        Mensaje message = new Mensaje(null, remitente, destinatario, "De acuerdo Juana. Un saludo.", LocalDate.now());

        repoMensaje.save(message);
        assertThat(message, notNullValue());
        assertThat(message.getId(), greaterThan(0));
    }

    @Test
    @Order(2)
    void dadoUnMensajeNOValido_cuandoCrear_entoncesExcepcion() {
        Usuario remitente = new Usuario(1, null, null, null, true);
        Usuario destinatario = new Usuario(2, null, null, null, true);
        Mensaje message = new Mensaje(null, destinatario, remitente, "SMS < 10", LocalDate.now());
        assertThrows(Exception.class, () -> {
            repoMensaje.save(message);
        });
    }

    @Test
    @Order(3)
    @Transactional
    void dadoUnUsuarioValido_cuandoObtener_entoncesListaMensajes() {
        Usuario user = repoUsuario.getReferenceById(1);

        List<Mensaje> userMessages = repoMensaje.obtener(user);
        assertNotNull(userMessages);
    }

    @Test
    @Order(4)
    void dadoUnUsuarioNOValido_cuandoObtener_entoncesExcepcion() {
        Usuario user = new Usuario(1, null, null, null, false);
        assertThrows(UsuarioException.class, () -> {
            repoMensaje.obtener(user);
        });
    }

    @Test
    @Order(5)
    @Transactional
    void dadoUnRemitenteValido_cuandoBorrarEntre_entoncesOK() throws Exception {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);

        boolean borrarChat = repoMensaje.borrarEntre(remitente, destinatario);
        assertTrue(borrarChat);
    }

    @Test
    @Order(6)
    @Transactional
    void dadoUnRemitenteNOValido_cuandoBorrarEntre_entoncesExcepcion() throws Exception {
        Usuario remitente = repoUsuario.getReferenceById(1);
        remitente.setActivo(false);
        Usuario destinatario = repoUsuario.getReferenceById(2);

        assertThrows(UsuarioException.class, () -> {
            repoMensaje.borrarEntre(remitente, destinatario);
        });
    }

    @Test
    @Order(7)
    @Transactional
    void dadoUnUsuarioValido_cuandoBorrarTodos_entoncesOK() throws Exception {
        Usuario user = repoUsuario.getReferenceById(1);

        boolean borrarChat = repoMensaje.borrarTodos(user);
        assertTrue(borrarChat);
    }

    @Test
    @Order(8)
    @Transactional
    void dadoUnUsuarioNOValido_cuandoBorrarTodos_entoncesExcepcion() throws Exception {
        Usuario user = new Usuario(1, null, null, null, true);
        assertThrows(UsuarioException.class, () -> {
            repoMensaje.borrarTodos(user);
        });
    }

}