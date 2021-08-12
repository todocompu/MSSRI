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
public class AnxGuiaRemisionElectronicaTO {

    private Integer eSecuencial;
    private String eTipoAmbiente;
    private String eClaveAcceso;
    private String eEstado;
    private String eAutorizacionNumero;
    private String eAutorizacionFecha;
    private byte[] eXml;
    private Boolean eEnviadoPorCorreo;
    private String guiaEmpresa;
    private String guiaPeriodo;
    private String guiaNumero;
    private String usrEmpresa;
    private String usrCodigo;
    private String usrFechaInserta;
    private String guiaFecha;

    public AnxGuiaRemisionElectronicaTO() {
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

    public String getGuiaEmpresa() {
        return guiaEmpresa;
    }

    public void setGuiaEmpresa(String guiaEmpresa) {
        this.guiaEmpresa = guiaEmpresa;
    }

    public String getGuiaPeriodo() {
        return guiaPeriodo;
    }

    public void setGuiaPeriodo(String guiaPeriodo) {
        this.guiaPeriodo = guiaPeriodo;
    }

    public String getGuiaNumero() {
        return guiaNumero;
    }

    public void setGuiaNumero(String guiaNumero) {
        this.guiaNumero = guiaNumero;
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

    public String getGuiaFecha() {
        return guiaFecha;
    }

    public void setGuiaFecha(String guiaFecha) {
        this.guiaFecha = guiaFecha;
    }

}
