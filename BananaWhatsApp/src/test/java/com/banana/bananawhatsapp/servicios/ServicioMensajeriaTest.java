package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
class ServicioMensajeriaTest {
    @Autowired
    IUsuarioRepository repoUsuario;
    @Autowired
    IServicioMensajeria servicio;

    @BeforeEach
    void cleanAndReloadData() {
        DBUtil.reloadDB();
    }

    @Test
    void testBeans(){
        assertNotNull(repoUsuario);
        assertNotNull(servicio);
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioYTextoValido_cuandoEnviarMensaje_entoncesMensajeValido() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);
        String texto = "Felices Fiestas!";
        Mensaje message = servicio.enviarMensaje(remitente, destinatario, texto);
        assertThat(message.getId(), greaterThan(0));
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioYTextoNOValido_cuandoEnviarMensaje_entoncesExcepcion() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);
        String texto = "SMS < 10";
        assertThrows(Exception.class, () -> {
            servicio.enviarMensaje(remitente, destinatario, texto);
        });
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioValido_cuandoMostrarChatConUsuario_entoncesListaMensajes() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);

         List<Mensaje> userMessages = servicio.mostrarChatConUsuario(remitente, destinatario);
         assertNotNull(userMessages);
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioNOValido_cuandoMostrarChatConUsuario_entoncesExcepcion() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = new Usuario(2, null, null, null, false);
        assertThrows(Exception.class, () -> {
            servicio.mostrarChatConUsuario(remitente, destinatario);
        });
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioValido_cuandoBorrarChatConUsuario_entoncesOK() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = repoUsuario.getReferenceById(2);
        boolean borrarChat = servicio.borrarChatConUsuario(remitente, destinatario);
        assertTrue(borrarChat);
    }

    @Test
    @Transactional
    void dadoRemitenteYDestinatarioNOValido_cuandoBorrarChatConUsuario_entoncesExcepcion() {
        Usuario remitente = repoUsuario.getReferenceById(1);
        Usuario destinatario = new Usuario(2, null, null, null, false);
        assertThrows(Exception.class, () -> {
             servicio.borrarChatConUsuario(remitente, destinatario);
        });
    }

}