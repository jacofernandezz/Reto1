package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.extended.IUsuarioRepository;
import com.banana.bananawhatsapp.servicios.IServicioMensajeria;
import com.banana.bananawhatsapp.servicios.IServicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mensajes")
public class ControladorMensajes {

    @Autowired
    private IServicioMensajeria servicioMensajeria;

    @Autowired
    private IServicioUsuarios userServ;


    @PostMapping
    public ResponseEntity<Boolean> enviarMensaje(@RequestParam Integer remitente, @RequestParam Integer destinatario, @RequestParam String texto) {
        try {
            Usuario uRemitente = userServ.obtener(remitente);
            Usuario uDestinatario = userServ.obtener(destinatario);
            // Envía el mensaje
            servicioMensajeria.enviarMensaje(uRemitente, uDestinatario, texto);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de excepciones
            System.out.println("Ha habido un error: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Mensaje>> mostrarChat(@RequestParam Integer remitente, @RequestParam Integer destinatario) {
            Usuario uRemitente = userServ.obtener(remitente);
            Usuario uDestinatario = userServ.obtener(destinatario);

            List<Mensaje> mensajes = servicioMensajeria.mostrarChatConUsuario(uRemitente, uDestinatario);
            if (mensajes != null && mensajes.size() > 0) {
                System.out.println("Mensajes entre: " + remitente + " y " + destinatario);
                for (Mensaje mensaje : mensajes) {
                    System.out.println(mensaje);
                }
            } else {
                System.out.println("NO hay mensajes entre: " + remitente + " y " + destinatario);
                return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(mensajes, HttpStatus.OK);

    }

    @DeleteMapping("/{idRemitente}/{idDestinatario}")
    public ResponseEntity<Boolean> eliminarChatConUsuario(@PathVariable Integer idRemitente, @PathVariable Integer idDestinatario) {
        try {

            Usuario uRemitente = userServ.obtener(idRemitente);
            Usuario uDestinatario = userServ.obtener(idDestinatario);

            boolean isOK = servicioMensajeria.borrarChatConUsuario(uRemitente, uDestinatario);
            if (isOK) {
                System.out.println("Mensajes borrados entre: " + idRemitente + " y " + idDestinatario);
            } else {
                System.out.println("NO se han borrado mensajes entre: " + idRemitente + " y " + idDestinatario);
                return new ResponseEntity<>(true, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Ha habido un error: " + e.getMessage());
            throw e;
        }

    }


}
