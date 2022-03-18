/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author CarolValdiviezo
 */
@Entity
@Table(name = "anx_venta_exportacion", schema = "anexo")
public class AnxVentaExportacion implements Serializable {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "exp_secuencial")
    private Integer expSecuencial;
    @Column(name = "exp_tipo_regimen_fiscal")
    private String expTipoRegimenFiscal;//ok
    @Column(name = "exp_regimen_general")
    private String expRegimenGeneral;//ok
    @Column(name = "exp_paraiso_fiscal")
    private String expParaisoFiscal;//ok
    @Column(name = "exp_regimen_fiscal_preferente")
    private String expRegimenFiscalPreferente;//ok
    @Column(name = "exp_pais_efectua_exportacion")
    private String expPaisEfectuaExportacion;//ok
    @Column(name = "exp_fecha_exportacion")
    @Temporal(TemporalType.DATE)
    private Date expFechaExportacion;//ok
    @Column(name = "exp_valor_fob_exterior")
    private BigDecimal expValorFobExterior;//ok
    @Column(name = "exp_valor_fob_local")
    private BigDecimal expValorFobLocal;
    @Column(name = "exp_tipo_exportacion")
    private String expTipoExportacion;//ok
    @Column(name = "exp_refrendo_documento_transporte")
    private String expRefrendoDocumentoTransporte;//ok
    @Column(name = "exp_refrendo_distrito")
    private String expRefrendoDistrito;//ok
    @Column(name = "exp_refrendo_anio")
    private String expRefrendoAnio;//ok
    @Column(name = "exp_refrendo_regimen")
    private String expRefrendoRegimen;//ok
    @Column(name = "exp_refrendo_correlativo")
    private String expRefrendoCorrelativo;//ok
    @Column(name = "exp_tipo_ingreso_exterior")
    private String expTipoIngresoExterior;//ok
    @Column(name = "exp_impuesto_pagado_exterior")
    private BigDecimal expImpuestoPagadoExterior;//ok
    @Column(name = "exp_observaciones")
    private String expObservaciones;
    @Column(name = "vta_empresa")
    private String vtaEmpresa;
    @Column(name = "vta_periodo")
    private String vtaPeriodo;
    @Column(name = "vta_motivo")
    private String vtaMotivo;
    @Column(name = "vta_numero")
    private String vtaNumero;

    public AnxVentaExportacion() {
    }

    public AnxVentaExportacion(Integer expSecuencial, String expTipoRegimenFiscal, String expRegimenGeneral, String expParaisoFiscal, String expRegimenFiscalPreferente, String expPaisEfectuaExportacion, Date expFechaExportacion, BigDecimal expValorFobExterior, BigDecimal expValorFobLocal, String expTipoExportacion, String expRefrendoDocumentoTransporte, String expRefrendoDistrito, String expRefrendoAnio, String expRefrendoRegimen, String expRefrendoCorrelativo, String expTipoIngresoExterior, BigDecimal expImpuestoPagadoExterior, String vtaEmpresa, String vtaPeriodo, String vtaMotivo, String vtaNumero) {
        this.expSecuencial = expSecuencial;
        this.expTipoRegimenFiscal = expTipoRegimenFiscal;
        this.expRegimenGeneral = expRegimenGeneral;
        this.expParaisoFiscal = expParaisoFiscal;
        this.expRegimenFiscalPreferente = expRegimenFiscalPreferente;
        this.expPaisEfectuaExportacion = expPaisEfectuaExportacion;
        this.expFechaExportacion = expFechaExportacion;
        this.expValorFobExterior = expValorFobExterior;
        this.expValorFobLocal = expValorFobLocal;
        this.expTipoExportacion = expTipoExportacion;
        this.expRefrendoDocumentoTransporte = expRefrendoDocumentoTransporte;
        this.expRefrendoDistrito = expRefrendoDistrito;
        this.expRefrendoAnio = expRefrendoAnio;
        this.expRefrendoRegimen = expRefrendoRegimen;
        this.expRefrendoCorrelativo = expRefrendoCorrelativo;
        this.expTipoIngresoExterior = expTipoIngresoExterior;
        this.expImpuestoPagadoExterior = expImpuestoPagadoExterior;
        this.vtaEmpresa = vtaEmpresa;
        this.vtaPeriodo = vtaPeriodo;
        this.vtaMotivo = vtaMotivo;
        this.vtaNumero = vtaNumero;
    }

    public Integer getExpSecuencial() {
        return expSecuencial;
    }

    public void setExpSecuencial(Integer expSecuencial) {
        this.expSecuencial = expSecuencial;
    }

    public String getExpTipoRegimenFiscal() {
        return expTipoRegimenFiscal;
    }

    public void setExpTipoRegimenFiscal(String expTipoRegimenFiscal) {
        this.expTipoRegimenFiscal = expTipoRegimenFiscal;
    }

    public String getExpRegimenGeneral() {
        return expRegimenGeneral;
    }

    public void setExpRegimenGeneral(String expRegimenGeneral) {
        this.expRegimenGeneral = expRegimenGeneral;
    }

    public String getExpParaisoFiscal() {
        return expParaisoFiscal;
    }

    public void setExpParaisoFiscal(String expParaisoFiscal) {
        this.expParaisoFiscal = expParaisoFiscal;
    }

    public String getExpRegimenFiscalPreferente() {
        return expRegimenFiscalPreferente;
    }

    public void setExpRegimenFiscalPreferente(String expRegimenFiscalPreferente) {
        this.expRegimenFiscalPreferente = expRegimenFiscalPreferente;
    }

    public String getExpPaisEfectuaExportacion() {
        return expPaisEfectuaExportacion;
    }

    public void setExpPaisEfectuaExportacion(String expPaisEfectuaExportacion) {
        this.expPaisEfectuaExportacion = expPaisEfectuaExportacion;
    }

    public Date getExpFechaExportacion() {
        return expFechaExportacion;
    }

    public void setExpFechaExportacion(Date expFechaExportacion) {
        this.expFechaExportacion = expFechaExportacion;
    }

    public BigDecimal getExpValorFobExterior() {
        return expValorFobExterior;
    }

    public void setExpValorFobExterior(BigDecimal expValorFobExterior) {
        this.expValorFobExterior = expValorFobExterior;
    }

    public BigDecimal getExpValorFobLocal() {
        return expValorFobLocal;
    }

    public void setExpValorFobLocal(BigDecimal expValorFobLocal) {
        this.expValorFobLocal = expValorFobLocal;
    }

    public String getExpTipoExportacion() {
        return expTipoExportacion;
    }

    public void setExpTipoExportacion(String expTipoExportacion) {
        this.expTipoExportacion = expTipoExportacion;
    }

    public String getExpRefrendoDocumentoTransporte() {
        return expRefrendoDocumentoTransporte;
    }

    public void setExpRefrendoDocumentoTransporte(String expRefrendoDocumentoTransporte) {
        this.expRefrendoDocumentoTransporte = expRefrendoDocumentoTransporte;
    }

    public String getExpRefrendoDistrito() {
        return expRefrendoDistrito;
    }

    public void setExpRefrendoDistrito(String expRefrendoDistrito) {
        this.expRefrendoDistrito = expRefrendoDistrito;
    }

    public String getExpRefrendoAnio() {
        return expRefrendoAnio;
    }

    public void setExpRefrendoAnio(String expRefrendoAnio) {
        this.expRefrendoAnio = expRefrendoAnio;
    }

    public String getExpRefrendoRegimen() {
        return expRefrendoRegimen;
    }

    public void setExpRefrendoRegimen(String expRefrendoRegimen) {
        this.expRefrendoRegimen = expRefrendoRegimen;
    }

    public String getExpRefrendoCorrelativo() {
        return expRefrendoCorrelativo;
    }

    public void setExpRefrendoCorrelativo(String expRefrendoCorrelativo) {
        this.expRefrendoCorrelativo = expRefrendoCorrelativo;
    }

    public String getExpTipoIngresoExterior() {
        return expTipoIngresoExterior;
    }

    public void setExpTipoIngresoExterior(String expTipoIngresoExterior) {
        this.expTipoIngresoExterior = expTipoIngresoExterior;
    }

    public BigDecimal getExpImpuestoPagadoExterior() {
        return expImpuestoPagadoExterior;
    }

    public void setExpImpuestoPagadoExterior(BigDecimal expImpuestoPagadoExterior) {
        this.expImpuestoPagadoExterior = expImpuestoPagadoExterior;
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

    public String getExpObservaciones() {
        return expObservaciones;
    }

    public void setExpObservaciones(String expObservaciones) {
        this.expObservaciones = expObservaciones;
    }

}
