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
 * @author Usuario1
 */
@Entity
@Table(name = "anx_catastro_microempresa", schema = "anexo")
public class AnxCatastroMicroempresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "cat_secuencial")
    private Integer catSecuencial;
    @Column(name = "cat_ruc")
    private String catRuc;
    @Column(name = "cat_razon_social")
    private String catRazonSocial;
    @Column(name = "cat_zona")
    private String catZona;
    @Column(name = "cat_provincia")
    private String catProvincia;
    @Column(name = "cat_year")
    private BigDecimal catYear;
    @Column(name = "usr_empresa")
    private String usrEmpresa;
    @Column(name = "usr_codigo")
    private String usrCodigo;
    @Column(name = "usr_fecha_inserta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usrFechaInserta;

    public AnxCatastroMicroempresa() {
    }

    public Integer getCatSecuencial() {
        return catSecuencial;
    }

    public void setCatSecuencial(Integer catSecuencial) {
        this.catSecuencial = catSecuencial;
    }

    public String getCatRuc() {
        return catRuc;
    }

    public void setCatRuc(String catRuc) {
        this.catRuc = catRuc;
    }

    public String getCatRazonSocial() {
        return catRazonSocial;
    }

    public void setCatRazonSocial(String catRazonSocial) {
        this.catRazonSocial = catRazonSocial;
    }

    public String getCatZona() {
        return catZona;
    }

    public void setCatZona(String catZona) {
        this.catZona = catZona;
    }

    public String getCatProvincia() {
        return catProvincia;
    }

    public void setCatProvincia(String catProvincia) {
        this.catProvincia = catProvincia;
    }

    public BigDecimal getCatYear() {
        return catYear;
    }

    public void setCatYear(BigDecimal catYear) {
        this.catYear = catYear;
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

    public Date getUsrFechaInserta() {
        return usrFechaInserta;
    }

    public void setUsrFechaInserta(Date usrFechaInserta) {
        this.usrFechaInserta = usrFechaInserta;
    }

}
