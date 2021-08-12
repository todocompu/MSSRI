/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author CarolValdiviezo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvVentaGuiaRemision implements Serializable {

    private InvTransportista invTransportista;
    private String guiaMotivoTraslado;
    private String guiaPuntoLlegada;
    private String guiaPlaca;
    @Temporal(TemporalType.DATE)
    private Date guiaFechaInicioTransporte;
    @Temporal(TemporalType.DATE)
    private Date guiaFechaFinTransporte;
    private String guiaPuntoPartida;
    private String guiaCodigoEstablecimiento;
    private String guiaRuta;

    public InvVentaGuiaRemision() {
    }

    public InvTransportista getInvTransportista() {
        return invTransportista;
    }

    public void setInvTransportista(InvTransportista invTransportista) {
        this.invTransportista = invTransportista;
    }

    public String getGuiaMotivoTraslado() {
        return guiaMotivoTraslado;
    }

    public void setGuiaMotivoTraslado(String guiaMotivoTraslado) {
        this.guiaMotivoTraslado = guiaMotivoTraslado;
    }

    public String getGuiaPuntoLlegada() {
        return guiaPuntoLlegada;
    }

    public void setGuiaPuntoLlegada(String guiaPuntoLlegada) {
        this.guiaPuntoLlegada = guiaPuntoLlegada;
    }

    public String getGuiaPlaca() {
        return guiaPlaca;
    }

    public void setGuiaPlaca(String guiaPlaca) {
        this.guiaPlaca = guiaPlaca;
    }

    public Date getGuiaFechaInicioTransporte() {
        return guiaFechaInicioTransporte;
    }

    public void setGuiaFechaInicioTransporte(Date guiaFechaInicioTransporte) {
        this.guiaFechaInicioTransporte = guiaFechaInicioTransporte;
    }

    public Date getGuiaFechaFinTransporte() {
        return guiaFechaFinTransporte;
    }

    public void setGuiaFechaFinTransporte(Date guiaFechaFinTransporte) {
        this.guiaFechaFinTransporte = guiaFechaFinTransporte;
    }

    public String getGuiaPuntoPartida() {
        return guiaPuntoPartida;
    }

    public void setGuiaPuntoPartida(String guiaPuntoPartida) {
        this.guiaPuntoPartida = guiaPuntoPartida;
    }

    public String getGuiaCodigoEstablecimiento() {
        return guiaCodigoEstablecimiento;
    }

    public void setGuiaCodigoEstablecimiento(String guiaCodigoEstablecimiento) {
        this.guiaCodigoEstablecimiento = guiaCodigoEstablecimiento;
    }

    public String getGuiaRuta() {
        return guiaRuta;
    }

    public void setGuiaRuta(String guiaRuta) {
        this.guiaRuta = guiaRuta;
    }

}
