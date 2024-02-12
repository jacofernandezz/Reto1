package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles({"dev"})
class ControladorMensajesTest {

    @Autowired
    ControladorMensajes controladorMensajes;
    @Autowired
    IUsuarioRepository repoUser;

    @BeforeEach
    void cleanAndReloadData() {
        DBUtil.reloadDB();
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioYTextoValidos_cuandoEnviarMensaje_entoncesOK() {
        Integer remitente = 1;
        Integer destinatario = 2;
        String texto = "Perfecto! Muchas gracias!";
        ResponseEntity<Boolean> sendMessage = controladorMensajes.enviarMensaje(remitente, destinatario, texto);
        assertThat(sendMessage.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioYTextoNOValidos_cuandoEnviarMensaje_entoncesExcepcion() {
        Integer remitente = 1;
        Integer destinatario = 2;
        String texto = "SMS < 10";
        assertThrows(Exception.class, () -> {
            controladorMensajes.enviarMensaje(remitente, destinatario, texto);
        });
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioValidos_cuandoMostrarChat_entoncesOK() {
        Integer remitente = 1;
        Integer destinatario = 2;
        ResponseEntity<List<Mensaje>> mostrarChat = controladorMensajes.mostrarChat(remitente, destinatario);
        assertNotNull(mostrarChat.getBody());
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioNOValidos_cuandoMostrarChat_entoncesExcepcion() {
        Integer remitente = 2;
        Integer destinatario = -1;
        assertThrows(UsuarioException.class, () -> {
            controladorMensajes.mostrarChat(remitente, destinatario);
        });
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioValidos_cuandoEliminarChatConUsuario_entoncesOK() {
        Integer remitente = 1;
        Integer destinatario = 2;
        ResponseEntity<Boolean> eliminarChat = controladorMensajes.eliminarChatConUsuario(remitente, destinatario);
        assertThat(eliminarChat.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioNOValidos_cuandoEliminarChatConUsuario_entoncesExcepcion() {
        Integer remitente = -1;
        Integer destinatario = 5;
        assertThrows(Exception.class, () -> {
            controladorMensajes.eliminarChatConUsuario(remitente, destinatario);
        });
    }
}