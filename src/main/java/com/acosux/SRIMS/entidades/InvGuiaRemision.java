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
public class InvGuiaRemision implements Serializable {

    protected InvGuiaRemisionPK invGuiaRemisionPK;
    private String guiaDocumentoNumero;
    private String guiaPlaca;
    @Temporal(TemporalType.TIMESTAMP)
    private Date guiaFechaInicioTransporte;
    @Temporal(TemporalType.TIMESTAMP)
    private Date guiaFechaFinTransporte;
    private String guiaPuntoPartida;
    private String nroDocumento;
    private String nroAutorizacion;
    private String guiaMotivoTraslado;
    private String guiaPuntoLlegada;
    private String guiaCodigoEstablecimiento;
    private String guiaDocumentoAduanero;
    private String guiaRuta;
    private String guiaInformacionAdicional;
    private String usrCodigo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date guiaFechaEmision;

    public InvGuiaRemision() {
    }

    public InvGuiaRemisionPK getInvGuiaRemisionPK() {
        return invGuiaRemisionPK;
    }

    public void setInvGuiaRemisionPK(InvGuiaRemisionPK invGuiaRemisionPK) {
        this.invGuiaRemisionPK = invGuiaRemisionPK;
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

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getGuiaDocumentoNumero() {
        return guiaDocumentoNumero;
    }

    public void setGuiaDocumentoNumero(String guiaDocumentoNumero) {
        this.guiaDocumentoNumero = guiaDocumentoNumero;
    }

    public String getNroAutorizacion() {
        return nroAutorizacion;
    }

    public void setNroAutorizacion(String nroAutorizacion) {
        this.nroAutorizacion = nroAutorizacion;
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

    public String getGuiaCodigoEstablecimiento() {
        return guiaCodigoEstablecimiento;
    }

    public void setGuiaCodigoEstablecimiento(String guiaCodigoEstablecimiento) {
        this.guiaCodigoEstablecimiento = guiaCodigoEstablecimiento;
    }

    public String getGuiaDocumentoAduanero() {
        return guiaDocumentoAduanero;
    }

    public void setGuiaDocumentoAduanero(String guiaDocumentoAduanero) {
        this.guiaDocumentoAduanero = guiaDocumentoAduanero;
    }

    public String getGuiaRuta() {
        return guiaRuta;
    }

    public void setGuiaRuta(String guiaRuta) {
        this.guiaRuta = guiaRuta;
    }

    public String getGuiaInformacionAdicional() {
        return guiaInformacionAdicional;
    }

    public void setGuiaInformacionAdicional(String guiaInformacionAdicional) {
        this.guiaInformacionAdicional = guiaInformacionAdicional;
    }

    public String getUsrCodigo() {
        return usrCodigo;
    }

    public void setUsrCodigo(String usrCodigo) {
        this.usrCodigo = usrCodigo;
    }

    public Date getGuiaFechaEmision() {
        return guiaFechaEmision;
    }

    public void setGuiaFechaEmision(Date guiaFechaEmision) {
        this.guiaFechaEmision = guiaFechaEmision;
    }

}
