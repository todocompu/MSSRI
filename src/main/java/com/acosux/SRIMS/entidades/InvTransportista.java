/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author CarolValdiviezo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvTransportista implements Serializable {

    private Character transIdTipo;
    private String transIdNumero;
    private String transNombres; //antes era el nombre

    public InvTransportista() {
    }

    public Character getTransIdTipo() {
        return transIdTipo;
    }

    public void setTransIdTipo(Character transIdTipo) {
        this.transIdTipo = transIdTipo;
    }

    public String getTransIdNumero() {
        return transIdNumero;
    }

    public void setTransIdNumero(String transIdNumero) {
        this.transIdNumero = transIdNumero;
    }

    public String getTransNombres() {
        return transNombres;
    }

    public void setTransNombres(String transNombres) {
        this.transNombres = transNombres;
    }

}
