/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 *
 * @author Mario
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnxCompraTO implements java.io.Serializable {

    private String empCodigo;
    private String perCodigo;
    private String motCodigo;
    private String compNumero;
    private String compRetencionNumero;
    private String compRetencionFechaEmision;
    private java.math.BigDecimal compBaseivabienes;
    private java.math.BigDecimal compPorcentajebienes;
    private java.math.BigDecimal compValorbienes;
    private java.math.BigDecimal compBaseivaservicios;
    private java.math.BigDecimal compPorcentajeservicios;
    private java.math.BigDecimal compValorservicios;
    private java.math.BigDecimal compBaseivaserviciosprofesionales;
    private java.math.BigDecimal compPorcentajeserviciosprofesionales;
    private java.math.BigDecimal compValorserviciosprofesionales;

    public AnxCompraTO() {
    }

    public BigDecimal getCompBaseivabienes() {
        return compBaseivabienes;
    }

    public void setCompBaseivabienes(BigDecimal compBaseivabienes) {
        this.compBaseivabienes = compBaseivabienes;
    }

    public BigDecimal getCompBaseivaservicios() {
        return compBaseivaservicios;
    }

    public void setCompBaseivaservicios(BigDecimal compBaseivaservicios) {
        this.compBaseivaservicios = compBaseivaservicios;
    }

    public BigDecimal getCompBaseivaserviciosprofesionales() {
        return compBaseivaserviciosprofesionales;
    }

    public void setCompBaseivaserviciosprofesionales(BigDecimal compBaseivaserviciosprofesionales) {
        this.compBaseivaserviciosprofesionales = compBaseivaserviciosprofesionales;
    }

    public BigDecimal getCompPorcentajebienes() {
        return compPorcentajebienes;
    }

    public void setCompPorcentajebienes(BigDecimal compPorcentajebienes) {
        this.compPorcentajebienes = compPorcentajebienes;
    }

    public BigDecimal getCompPorcentajeservicios() {
        return compPorcentajeservicios;
    }

    public void setCompPorcentajeservicios(BigDecimal compPorcentajeservicios) {
        this.compPorcentajeservicios = compPorcentajeservicios;
    }

    public BigDecimal getCompPorcentajeserviciosprofesionales() {
        return compPorcentajeserviciosprofesionales;
    }

    public void setCompPorcentajeserviciosprofesionales(BigDecimal compPorcentajeserviciosprofesionales) {
        this.compPorcentajeserviciosprofesionales = compPorcentajeserviciosprofesionales;
    }

    public String getCompRetencionFechaEmision() {
        return compRetencionFechaEmision;
    }

    public void setCompRetencionFechaEmision(String compRetencionFechaEmision) {
        this.compRetencionFechaEmision = compRetencionFechaEmision;
    }

    public String getCompRetencionNumero() {
        return compRetencionNumero;
    }

    public void setCompRetencionNumero(String compRetencionNumero) {
        this.compRetencionNumero = compRetencionNumero;
    }

    public BigDecimal getCompValorbienes() {
        return compValorbienes;
    }

    public void setCompValorbienes(BigDecimal compValorbienes) {
        this.compValorbienes = compValorbienes;
    }

    public BigDecimal getCompValorservicios() {
        return compValorservicios;
    }

    public void setCompValorservicios(BigDecimal compValorservicios) {
        this.compValorservicios = compValorservicios;
    }

    public BigDecimal getCompValorserviciosprofesionales() {
        return compValorserviciosprofesionales;
    }

    public void setCompValorserviciosprofesionales(BigDecimal compValorserviciosprofesionales) {
        this.compValorserviciosprofesionales = compValorserviciosprofesionales;
    }

    public String getEmpCodigo() {
        return empCodigo;
    }

    public void setEmpCodigo(String empCodigo) {
        this.empCodigo = empCodigo;
    }

    public String getPerCodigo() {
        return perCodigo;
    }

    public void setPerCodigo(String perCodigo) {
        this.perCodigo = perCodigo;
    }

    public String getMotCodigo() {
        return motCodigo;
    }

    public void setMotCodigo(String motCodigo) {
        this.motCodigo = motCodigo;
    }

    public String getCompNumero() {
        return compNumero;
    }

    public void setCompNumero(String compNumero) {
        this.compNumero = compNumero;
    }

}
