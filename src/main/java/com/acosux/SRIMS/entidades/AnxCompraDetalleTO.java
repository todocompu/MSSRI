/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 *
 * @author Andres Guachisaca
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnxCompraDetalleTO implements java.io.Serializable {

    private String detConcepto;
    private java.math.BigDecimal detPorcentaje;
    private java.math.BigDecimal detBaseImponible;
    private java.math.BigDecimal detBase0;
    private java.math.BigDecimal detBaseNoObjetoIva;
    private java.math.BigDecimal detValorRetenido;

    public AnxCompraDetalleTO() {
    }

    public BigDecimal getDetBase0() {
        return detBase0;
    }

    public void setDetBase0(BigDecimal detBase0) {
        this.detBase0 = detBase0;
    }

    public BigDecimal getDetBaseImponible() {
        return detBaseImponible;
    }

    public void setDetBaseImponible(BigDecimal detBaseImponible) {
        this.detBaseImponible = detBaseImponible;
    }

    public BigDecimal getDetBaseNoObjetoIva() {
        return detBaseNoObjetoIva;
    }

    public void setDetBaseNoObjetoIva(BigDecimal detBaseNoObjetoIva) {
        this.detBaseNoObjetoIva = detBaseNoObjetoIva;
    }

    public String getDetConcepto() {
        return detConcepto;
    }

    public void setDetConcepto(String detConcepto) {
        this.detConcepto = detConcepto;
    }

    public BigDecimal getDetPorcentaje() {
        return detPorcentaje;
    }

    public void setDetPorcentaje(BigDecimal detPorcentaje) {
        this.detPorcentaje = detPorcentaje;
    }

    public BigDecimal getDetValorRetenido() {
        return detValorRetenido;
    }

    public void setDetValorRetenido(BigDecimal detValorRetenido) {
        this.detValorRetenido = detValorRetenido;
    }

}
