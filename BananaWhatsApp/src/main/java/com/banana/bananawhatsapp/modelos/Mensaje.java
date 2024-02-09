package com.banana.bananawhatsapp.modelos;

import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.persistencia.listeners.MensajeEntityListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@EntityListeners(MensajeEntityListener.class)
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_user")
    private Usuario remitente;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_user")
    private Usuario destinatario;
    private String cuerpo;
    private LocalDate fecha;

    private boolean validarFecha() {
        return this.fecha != null && this.fecha.compareTo(LocalDate.now()) <= 0;
    }

    public boolean valido() throws MensajeException {
        if (remitente != null
                && destinatario != null
                && remitente.valido(remitente.getId()==null)
                && destinatario.valido(destinatario.getId()==null)
                && cuerpo != null
                && cuerpo.length() > 10
                && validarFecha()
        ) return true;
        else throw new MensajeException("Mensaje no valido");
    }


}
