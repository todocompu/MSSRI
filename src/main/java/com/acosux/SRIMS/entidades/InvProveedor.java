package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvProveedor implements Serializable {

    private String provIdTipo;
    private String provRazonSocial;
    private String provIdNumero;
    private String provDireccion;
    private String provTelefono;
    private String provEmail;

    public InvProveedor() {
    }

    public String getProvIdNumero() {
        return provIdNumero;
    }

    public void setProvIdNumero(String provIdNumero) {
        this.provIdNumero = provIdNumero;
    }

    public String getProvRazonSocial() {
        return provRazonSocial;
    }

    public void setProvRazonSocial(String provRazonSocial) {
        this.provRazonSocial = provRazonSocial;
    }

    public String getProvDireccion() {
        return provDireccion;
    }

    public void setProvDireccion(String provDireccion) {
        this.provDireccion = provDireccion;
    }

    public String getProvTelefono() {
        return provTelefono;
    }

    public void setProvTelefono(String provTelefono) {
        this.provTelefono = provTelefono;
    }

    public String getProvEmail() {
        return provEmail;
    }

    public void setProvEmail(String provEmail) {
        this.provEmail = provEmail;
    }

    public String getProvIdTipo() {
        return provIdTipo;
    }

    public void setProvIdTipo(String provIdTipo) {
        this.provIdTipo = provIdTipo;
    }

}
