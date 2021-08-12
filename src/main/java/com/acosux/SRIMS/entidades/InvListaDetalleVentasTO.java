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
public class InvListaDetalleVentasTO implements java.io.Serializable {

    private String codigoProducto;
    private String nombreProducto;
    private java.math.BigDecimal cantidadProducto;
    private String medidaDetalle;
    private java.math.BigDecimal precioProducto;
    private java.math.BigDecimal parcialProducto;
    private java.math.BigDecimal porcentajeRecargo;// 10
    private java.math.BigDecimal porcentajeDescuento;
    private java.math.BigDecimal ivaCobrado;
    private java.math.BigDecimal detalleTotal;
    private String gravaIva;
    private java.math.BigDecimal detMontoIce;
    private String iceCodigo;
    private java.math.BigDecimal iceTarifaFija;
    private Integer proComplementario;

    public InvListaDetalleVentasTO() {
    }

    public String getGravaIva() {
        return gravaIva;
    }

    public void setGravaIva(String gravaIva) {
        this.gravaIva = gravaIva;
    }

    public BigDecimal getIvaCobrado() {
        return ivaCobrado;
    }

    public void setIvaCobrado(BigDecimal ivaCobrado) {
        this.ivaCobrado = ivaCobrado;
    }

    public BigDecimal getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public BigDecimal getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(BigDecimal cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public BigDecimal getDetalleTotal() {
        return detalleTotal;
    }

    public void setDetalleTotal(BigDecimal detalleTotal) {
        this.detalleTotal = detalleTotal;
    }

    public String getMedidaDetalle() {
        return medidaDetalle;
    }

    public void setMedidaDetalle(String medidaDetalle) {
        this.medidaDetalle = medidaDetalle;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getParcialProducto() {
        return parcialProducto;
    }

    public void setParcialProducto(BigDecimal parcialProducto) {
        this.parcialProducto = parcialProducto;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }

    public BigDecimal getDetMontoIce() {
        return detMontoIce;
    }

    public void setDetMontoIce(BigDecimal detMontoIce) {
        this.detMontoIce = detMontoIce;
    }

    public String getIceCodigo() {
        return iceCodigo;
    }

    public void setIceCodigo(String iceCodigo) {
        this.iceCodigo = iceCodigo;
    }

    public BigDecimal getIceTarifaFija() {
        return iceTarifaFija;
    }

    public void setIceTarifaFija(BigDecimal iceTarifaFija) {
        this.iceTarifaFija = iceTarifaFija;
    }

    public Integer getProComplementario() {
        return proComplementario;
    }

    public void setProComplementario(Integer proComplementario) {
        this.proComplementario = proComplementario;
    }

}
