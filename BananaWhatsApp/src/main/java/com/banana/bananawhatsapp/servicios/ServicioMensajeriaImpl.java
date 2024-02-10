package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioMensajeriaImpl implements IServicioMensajeria{

    @Autowired
    IMensajeRepository repo;

    @Override
    public Mensaje enviarMensaje(Usuario remitente, Usuario destinatario, String texto) throws UsuarioException, MensajeException {
        Mensaje message = new Mensaje(null, remitente, destinatario, texto, LocalDate.now());

        return repo.save(message);
    }

    @Override
    public List<Mensaje> mostrarChatConUsuario(Usuario remitente, Usuario destinatario) throws UsuarioException, MensajeException {
        // Verificar si los usuarios son válidos
    if (!remitente.valido(false)) {
        throw new UsuarioException("El remitente no es válido");
    }
    if (!destinatario.valido(false)) {
        throw new UsuarioException("El destinatario no es válido");
    }

    // Si los usuarios son válidos, obtener el chat entre ellos
    return repo.mostrarChatConUsuario(remitente, destinatario);
    }

    @Override
    public boolean borrarChatConUsuario(Usuario remitente, Usuario destinatario) throws UsuarioException, MensajeException {
        return repo.borrarEntre(remitente,destinatario);
    }
}
