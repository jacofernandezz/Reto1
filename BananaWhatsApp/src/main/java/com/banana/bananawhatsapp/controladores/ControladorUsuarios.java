package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.servicios.IServicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuarios {

    @Autowired
    private IServicioUsuarios servicioUsuarios;

    @PostMapping
    public ResponseEntity<Usuario> alta(@RequestBody Usuario nuevo) {
        Usuario usuario = servicioUsuarios.crearUsuario(nuevo);
        System.out.println("Usuario creado: " + nuevo);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Usuario> actualizar(@RequestBody Usuario usuario) {
        servicioUsuarios.actualizarUsuario(usuario);
        System.out.println("Usuario actualizado: " + usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Boolean> baja(@PathVariable Integer idUsuario) {
        servicioUsuarios.borrarUsuario(idUsuario);
        System.out.println("Borrado usuario con ID: " + idUsuario);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
