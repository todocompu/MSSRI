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
@Table(name = "anx_catastro_rimpe", schema = "anexo")
public class AnxCatastroRimpe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "catr_secuencial")
    private Integer catrSecuencial;
    @Column(name = "catr_ruc")
    private String catrRuc;
    @Column(name = "catr_razon_social")
    private String catrRazonSocial;
    @Column(name = "catr_zonal")
    private String catrZonal;
    @Column(name = "catr_regimen")
    private String catrRegimen;
    @Column(name = "catr_negocio_popular")
    private String catrNegocioPopular;
    @Column(name = "usr_empresa")
    private String usrEmpresa;
    @Column(name = "usr_codigo")
    private String usrCodigo;
    @Column(name = "usr_fecha_inserta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usrFechaInserta;

    public AnxCatastroRimpe() {
    }

    public Integer getCatrSecuencial() {
        return catrSecuencial;
    }

    public void setCatrSecuencial(Integer catrSecuencial) {
        this.catrSecuencial = catrSecuencial;
    }

    public String getCatrRuc() {
        return catrRuc;
    }

    public void setCatrRuc(String catrRuc) {
        this.catrRuc = catrRuc;
    }

    public String getCatrRazonSocial() {
        return catrRazonSocial;
    }

    public void setCatrRazonSocial(String catrRazonSocial) {
        this.catrRazonSocial = catrRazonSocial;
    }

    public String getCatrZonal() {
        return catrZonal;
    }

    public void setCatrZonal(String catrZonal) {
        this.catrZonal = catrZonal;
    }

    public String getCatrRegimen() {
        return catrRegimen;
    }

    public void setCatrRegimen(String catrRegimen) {
        this.catrRegimen = catrRegimen;
    }

    public String getCatrNegocioPopular() {
        return catrNegocioPopular;
    }

    public void setCatrNegocioPopular(String catrNegocioPopular) {
        this.catrNegocioPopular = catrNegocioPopular;
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
