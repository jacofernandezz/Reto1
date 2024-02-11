package com.banana.bananawhatsapp.modelos;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.persistencia.listeners.UsuarioEntityListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@EntityListeners(UsuarioEntityListener.class)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String email;
    private LocalDate alta;
    private boolean activo;
    @ToString.Exclude
    @OneToMany(mappedBy = "remitente", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesEnviados;
    @ToString.Exclude
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesRecibidos;

    public Usuario(Integer id, String nombre, String email, LocalDate alta, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.alta = alta;
        this.activo = activo;
    }

    public boolean validarId() {
        if (this.id == null || this.id <= 0) {
            throw new UsuarioException("El ID no es válido");
        }
        return true;
    }

    private boolean validarNombre() {
        return this.nombre != null && this.nombre.length() >= 3;
    }

    private boolean validarEmail() {
        return this.email != null && this.email.indexOf("@") > 0 && this.email.indexOf(".") > 0;
    }

    private boolean validarAlta() {
        return this.alta != null && this.alta.compareTo(LocalDate.now()) <= 0;
    }

    public boolean valido(boolean esInsert) throws UsuarioException {
        boolean condicionesValidas = activo && validarNombre() && validarEmail() && validarAlta();

        if (esInsert && condicionesValidas) {
            return true;
        } else if (!esInsert && validarId() && condicionesValidas) {
            return true;
        } else {
            throw new UsuarioException("Usuario no válido");
        }
    }
}
