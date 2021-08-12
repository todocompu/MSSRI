package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvVentas implements Serializable {

    private static final long serialVersionUID = 1L;

    protected InvVentasPK invVentasPK;
    private String vtaDocumentoNumero;
    private Date vtaFecha;
    private Date vtaFechaVencimiento;
    private BigDecimal vtaIvaVigente;
    private String vtaObservaciones;
    private String vtaInformacionAdicional;
    private String vtaFormaPago;
    private BigDecimal vtaDescuentoBase0;
    private BigDecimal vtaDescuentoBaseImponible;
    private BigDecimal vtaDescuentoBaseNoObjeto;
    private BigDecimal vtaDescuentoBaseExenta;
    private BigDecimal vtaSubtotalBase0;
    private BigDecimal vtaSubtotalBaseImponible;
    private BigDecimal vtaMontoiva;
    private BigDecimal vtaTotal;
    private BigDecimal vtaPagadoEfectivo;
    private BigDecimal vtaPagadoDineroElectronico;
    private BigDecimal vtaPagadoTarjetaCredito;
    private BigDecimal vtaPagadoOtro;
    private BigDecimal vtaMontoIce;
    private String cliCodigoEstablecimiento;
    private String usrCodigo;

    public InvVentas() {
    }

    public InvVentasPK getInvVentasPK() {
        return invVentasPK;
    }

    public void setInvVentasPK(InvVentasPK invVentasPK) {
        this.invVentasPK = invVentasPK;
    }

    public String getVtaDocumentoNumero() {
        return vtaDocumentoNumero;
    }

    public void setVtaDocumentoNumero(String vtaDocumentoNumero) {
        this.vtaDocumentoNumero = vtaDocumentoNumero;
    }

    public Date getVtaFecha() {
        return vtaFecha;
    }

    public void setVtaFecha(Date vtaFecha) {
        this.vtaFecha = vtaFecha;
    }

    public Date getVtaFechaVencimiento() {
        return vtaFechaVencimiento;
    }

    public void setVtaFechaVencimiento(Date vtaFechaVencimiento) {
        this.vtaFechaVencimiento = vtaFechaVencimiento;
    }

    public BigDecimal getVtaIvaVigente() {
        return vtaIvaVigente;
    }

    public void setVtaIvaVigente(BigDecimal vtaIvaVigente) {
        this.vtaIvaVigente = vtaIvaVigente;
    }

    public String getVtaObservaciones() {
        return vtaObservaciones;
    }

    public void setVtaObservaciones(String vtaObservaciones) {
        this.vtaObservaciones = vtaObservaciones;
    }

    public String getVtaInformacionAdicional() {
        return vtaInformacionAdicional;
    }

    public void setVtaInformacionAdicional(String vtaInformacionAdicional) {
        this.vtaInformacionAdicional = vtaInformacionAdicional;
    }

    public String getVtaFormaPago() {
        return vtaFormaPago;
    }

    public void setVtaFormaPago(String vtaFormaPago) {
        this.vtaFormaPago = vtaFormaPago;
    }

    public BigDecimal getVtaDescuentoBase0() {
        return vtaDescuentoBase0;
    }

    public void setVtaDescuentoBase0(BigDecimal vtaDescuentoBase0) {
        this.vtaDescuentoBase0 = vtaDescuentoBase0;
    }

    public BigDecimal getVtaDescuentoBaseImponible() {
        return vtaDescuentoBaseImponible;
    }

    public void setVtaDescuentoBaseImponible(BigDecimal vtaDescuentoBaseImponible) {
        this.vtaDescuentoBaseImponible = vtaDescuentoBaseImponible;
    }

    public BigDecimal getVtaDescuentoBaseNoObjeto() {
        return vtaDescuentoBaseNoObjeto;
    }

    public void setVtaDescuentoBaseNoObjeto(BigDecimal vtaDescuentoBaseNoObjeto) {
        this.vtaDescuentoBaseNoObjeto = vtaDescuentoBaseNoObjeto;
    }

    public BigDecimal getVtaDescuentoBaseExenta() {
        return vtaDescuentoBaseExenta;
    }

    public void setVtaDescuentoBaseExenta(BigDecimal vtaDescuentoBaseExenta) {
        this.vtaDescuentoBaseExenta = vtaDescuentoBaseExenta;
    }

    public BigDecimal getVtaSubtotalBase0() {
        return vtaSubtotalBase0;
    }

    public void setVtaSubtotalBase0(BigDecimal vtaSubtotalBase0) {
        this.vtaSubtotalBase0 = vtaSubtotalBase0;
    }

    public BigDecimal getVtaSubtotalBaseImponible() {
        return vtaSubtotalBaseImponible;
    }

    public void setVtaSubtotalBaseImponible(BigDecimal vtaSubtotalBaseImponible) {
        this.vtaSubtotalBaseImponible = vtaSubtotalBaseImponible;
    }

    public BigDecimal getVtaMontoiva() {
        return vtaMontoiva;
    }

    public void setVtaMontoiva(BigDecimal vtaMontoiva) {
        this.vtaMontoiva = vtaMontoiva;
    }

    public BigDecimal getVtaTotal() {
        return vtaTotal;
    }

    public void setVtaTotal(BigDecimal vtaTotal) {
        this.vtaTotal = vtaTotal;
    }

    public BigDecimal getVtaPagadoEfectivo() {
        return vtaPagadoEfectivo;
    }

    public void setVtaPagadoEfectivo(BigDecimal vtaPagadoEfectivo) {
        this.vtaPagadoEfectivo = vtaPagadoEfectivo;
    }

    public BigDecimal getVtaPagadoDineroElectronico() {
        return vtaPagadoDineroElectronico;
    }

    public void setVtaPagadoDineroElectronico(BigDecimal vtaPagadoDineroElectronico) {
        this.vtaPagadoDineroElectronico = vtaPagadoDineroElectronico;
    }

    public BigDecimal getVtaPagadoTarjetaCredito() {
        return vtaPagadoTarjetaCredito;
    }

    public void setVtaPagadoTarjetaCredito(BigDecimal vtaPagadoTarjetaCredito) {
        this.vtaPagadoTarjetaCredito = vtaPagadoTarjetaCredito;
    }

    public BigDecimal getVtaPagadoOtro() {
        return vtaPagadoOtro;
    }

    public void setVtaPagadoOtro(BigDecimal vtaPagadoOtro) {
        this.vtaPagadoOtro = vtaPagadoOtro;
    }

    public String getCliCodigoEstablecimiento() {
        return cliCodigoEstablecimiento;
    }

    public void setCliCodigoEstablecimiento(String cliCodigoEstablecimiento) {
        this.cliCodigoEstablecimiento = cliCodigoEstablecimiento;
    }

    public BigDecimal getVtaMontoIce() {
        return vtaMontoIce;
    }

    public void setVtaMontoIce(BigDecimal vtaMontoIce) {
        this.vtaMontoIce = vtaMontoIce;
    }

    public String getUsrCodigo() {
        return usrCodigo;
    }

    public void setUsrCodigo(String usrCodigo) {
        this.usrCodigo = usrCodigo;
    }

}
