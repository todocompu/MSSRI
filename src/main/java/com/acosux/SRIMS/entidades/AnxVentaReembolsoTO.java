/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author Trabajo
 */
@Entity
public class AnxVentaReembolsoTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "reemb_secuencial")
    private Integer reembSecuencial;
    @Column(name = "reemb_documento_tipo")
    private String reembDocumentoTipo;
    @Column(name = "reemb_documento_numero")
    private String reembDocumentoNumero;
    @Column(name = "reemb_fechaemision")
    private String reembFechaemision;
    @Column(name = "reemb_autorizacion")
    private String reembAutorizacion;
    @Column(name = "reemb_baseimponible")
    private BigDecimal reembBaseimponible;
    @Column(name = "reemb_baseimpgrav")
    private BigDecimal reembBaseimpgrav;
    @Column(name = "reemb_basenograiva")
    private BigDecimal reembBasenograiva;
    @Column(name = "reemb_montoice")
    private BigDecimal reembMontoice;
    @Column(name = "reemb_montoiva")
    private BigDecimal reembMontoiva;
    @Column(name = "prov_empresa")
    private String provEmpresa;
    @Column(name = "prov_codigo")
    private String provCodigo;
    @Column(name = "prov_id_tipo")
    private String provIdTipo;
    @Column(name = "prov_razon_social")
    private String provRazonSocial;
    @Column(name = "prov_id_numero")
    private String provIdNumero;
    @Column(name = "comp_empresa")
    private String compEmpresa;
    @Column(name = "comp_periodo")
    private String compPeriodo;
    @Column(name = "comp_motivo")
    private String compMotivo;
    @Column(name = "comp_numero")
    private String compNumero;
    @Column(name = "tc_abreviatura")
    private String auxDocTipoAbreviatura;

    @Column(name = "vta_empresa")
    private String vtaEmpresa;
    @Column(name = "vta_periodo")
    private String vtaPeriodo;
    @Column(name = "vta_motivo")
    private String vtaMotivo;
    @Column(name = "vta_numero")
    private String vtaNumero;
    @Transient
    private String provCodigoCopia;

    public AnxVentaReembolsoTO() {
    }

    public Integer getReembSecuencial() {
        return reembSecuencial;
    }

    public void setReembSecuencial(Integer reembSecuencial) {
        this.reembSecuencial = reembSecuencial;
    }

    public String getReembDocumentoTipo() {
        return reembDocumentoTipo;
    }

    public void setReembDocumentoTipo(String reembDocumentoTipo) {
        this.reembDocumentoTipo = reembDocumentoTipo;
    }

    public String getReembDocumentoNumero() {
        return reembDocumentoNumero;
    }

    public void setReembDocumentoNumero(String reembDocumentoNumero) {
        this.reembDocumentoNumero = reembDocumentoNumero;
    }

    public String getReembFechaemision() {
        return reembFechaemision;
    }

    public void setReembFechaemision(String reembFechaemision) {
        this.reembFechaemision = reembFechaemision;
    }

    public String getReembAutorizacion() {
        return reembAutorizacion;
    }

    public void setReembAutorizacion(String reembAutorizacion) {
        this.reembAutorizacion = reembAutorizacion;
    }

    public BigDecimal getReembBaseimponible() {
        return reembBaseimponible;
    }

    public void setReembBaseimponible(BigDecimal reembBaseimponible) {
        this.reembBaseimponible = reembBaseimponible;
    }

    public BigDecimal getReembBaseimpgrav() {
        return reembBaseimpgrav;
    }

    public void setReembBaseimpgrav(BigDecimal reembBaseimpgrav) {
        this.reembBaseimpgrav = reembBaseimpgrav;
    }

    public BigDecimal getReembBasenograiva() {
        return reembBasenograiva;
    }

    public void setReembBasenograiva(BigDecimal reembBasenograiva) {
        this.reembBasenograiva = reembBasenograiva;
    }

    public BigDecimal getReembMontoice() {
        return reembMontoice;
    }

    public void setReembMontoice(BigDecimal reembMontoice) {
        this.reembMontoice = reembMontoice;
    }

    public BigDecimal getReembMontoiva() {
        return reembMontoiva;
    }

    public void setReembMontoiva(BigDecimal reembMontoiva) {
        this.reembMontoiva = reembMontoiva;
    }

    public String getProvEmpresa() {
        return provEmpresa;
    }

    public void setProvEmpresa(String provEmpresa) {
        this.provEmpresa = provEmpresa;
    }

    public String getProvCodigo() {
        return provCodigo;
    }

    public void setProvCodigo(String provCodigo) {
        this.provCodigo = provCodigo;
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

    public String getAuxDocTipoAbreviatura() {
        return auxDocTipoAbreviatura;
    }

    public void setAuxDocTipoAbreviatura(String auxDocTipoAbreviatura) {
        this.auxDocTipoAbreviatura = auxDocTipoAbreviatura;
    }

    public String getProvIdTipo() {
        return provIdTipo;
    }

    public void setProvIdTipo(String provIdTipo) {
        this.provIdTipo = provIdTipo;
    }

    public String getProvRazonSocial() {
        return provRazonSocial;
    }

    public void setProvRazonSocial(String provRazonSocial) {
        this.provRazonSocial = provRazonSocial;
    }

    public String getProvIdNumero() {
        return provIdNumero;
    }

    public void setProvIdNumero(String provIdNumero) {
        this.provIdNumero = provIdNumero;
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

    public String getProvCodigoCopia() {
        return provCodigoCopia;
    }

    public void setProvCodigoCopia(String provCodigoCopia) {
        this.provCodigoCopia = provCodigoCopia;
    }

}
