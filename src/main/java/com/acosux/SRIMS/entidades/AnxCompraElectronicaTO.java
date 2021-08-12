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
public class AnxCompraElectronicaTO {

    private Integer eSecuencial;
    private String eTipoAmbiente;
    private String eClaveAcceso;
    private String eEstado;
    private String eAutorizacionNumero;
    private String eAutorizacionFecha;
    private byte[] eXml;
    private Boolean eEnviadoPorCorreo;
    private String compEmpresa;
    private String compPeriodo;
    private String compMotivo;
    private String compNumero;
    private String usrEmpresa;
    private String usrCodigo;
    private String usrFechaInserta;

    public AnxCompraElectronicaTO() {
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

    public String getCompEmpresa() {
        return compEmpresa;
    }

    public void setCompEmpresa(String compEmpresa) {
        this.compEmpresa = compEmpresa;
    }

    public String getCompPeriodo() {
        return compPeriodo;
    }

    public void setCompPeriodo(String compPeriodo) {
        this.compPeriodo = compPeriodo;
    }

    public String getCompMotivo() {
        return compMotivo;
    }

    public void setCompMotivo(String compMotivo) {
        this.compMotivo = compMotivo;
    }

    public String getCompNumero() {
        return compNumero;
    }

    public void setCompNumero(String compNumero) {
        this.compNumero = compNumero;
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

}
