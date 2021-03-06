package com.example.camiloandresibarrayepes.chatfb.Entidades;

import com.example.camiloandresibarrayepes.chatfb.Entidades.Mensaje;

/**
 * Created by Camilo Ibarra KAPTA on 26/02/2018.
 */

public class MensajeRecibir extends Mensaje {

    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String nombre, String fotoPerfil, String type_mensaje, Long hora) {
        super(mensaje, nombre, fotoPerfil, type_mensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
