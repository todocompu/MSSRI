/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

/**
 *
 * @author mario
 */
public class AnxVentaElectronicaTO {

    private Integer eSecuencial;
    private String eTipoAmbiente;
    private String eClaveAcceso;
    private String eEstado;
    private String eAutorizacionNumero;
    private String eAutorizacionFecha;
    private byte[] eXml;
    private Boolean eEnviadoPorCorreo;
    private String vtaEmpresa;
    private String vtaPeriodo;
    private String vtaMotivo;
    private String vtaNumero;
    private String usrEmpresa;
    private String usrCodigo;
    private String usrFechaInserta;
    private String numeroDcto;
    private String vtaFecha;

    public AnxVentaElectronicaTO() {
    }

    public Integer geteSecuencial() {
        return eSecuencial;
    }

    public void seteSecuencial(Integer eSecuencial) {
        this.eSecuencial = eSecuencial;
    }

    public String geteTipoAmbiente() {
        return eTipoAmbiente;
    }

    public void seteTipoAmbiente(String eTipoAmbiente) {
        this.eTipoAmbiente = eTipoAmbiente;
    }

    public String geteClaveAcceso() {
        return eClaveAcceso;
    }

    public void seteClaveAcceso(String eClaveAcceso) {
        this.eClaveAcceso = eClaveAcceso;
    }

    public String geteEstado() {
        return eEstado;
    }

    public void seteEstado(String eEstado) {
        this.eEstado = eEstado;
    }

    public String geteAutorizacionNumero() {
        return eAutorizacionNumero;
    }

    public void seteAutorizacionNumero(String eAutorizacionNumero) {
        this.eAutorizacionNumero = eAutorizacionNumero;
    }

    public String geteAutorizacionFecha() {
        return eAutorizacionFecha;
    }

    public void seteAutorizacionFecha(String eAutorizacionFecha) {
        this.eAutorizacionFecha = eAutorizacionFecha;
    }

    public byte[] geteXml() {
        return eXml;
    }

    public void seteXml(byte[] eXml) {
        this.eXml = eXml;
    }

    public Boolean geteEnviadoPorCorreo() {
        return eEnviadoPorCorreo;
    }

    public void seteEnviadoPorCorreo(Boolean eEnviadoPorCorreo) {
        this.eEnviadoPorCorreo = eEnviadoPorCorreo;
    }

    public String getVtaEmpresa() {
        return vtaEmpresa;
    }

    public void setVtaEmpresa(String vtaEmpresa) {
        this.vtaEmpresa = vtaEmpresa;
    }

    public String getVtaPeriodo() {
        return vtaPeriodo;
    }

    public void setVtaPeriodo(String vtaPeriodo) {
        this.vtaPeriodo = vtaPeriodo;
    }

    public String getVtaMotivo() {
        return vtaMotivo;
    }

    public void setVtaMotivo(String vtaMotivo) {
        this.vtaMotivo = vtaMotivo;
    }

    public String getVtaNumero() {
        return vtaNumero;
    }

    public void setVtaNumero(String vtaNumero) {
        this.vtaNumero = vtaNumero;
    }

    public String getUsrEmpresa() {
        return usrEmpresa;
    }

    public void setUsrEmpresa(String usrEmpresa) {
        this.usrEmpresa = usrEmpresa;
    }

    public String getUsrCodigo() {
        return usrCodigo;
    }

    public void setUsrCodigo(String usrCodigo) {
        this.usrCodigo = usrCodigo;
    }

    public String getUsrFechaInserta() {
        return usrFechaInserta;
    }

    public void setUsrFechaInserta(String usrFechaInserta) {
        this.usrFechaInserta = usrFechaInserta;
    }

    public String getNumeroDcto() {
        return numeroDcto;
    }

    public void setNumeroDcto(String numeroDcto) {
        this.numeroDcto = numeroDcto;
    }

    public String getVtaFecha() {
        return vtaFecha;
    }

    public void setVtaFecha(String vtaFecha) {
        this.vtaFecha = vtaFecha;
    }

}
