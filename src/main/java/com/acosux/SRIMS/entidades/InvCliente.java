package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    protected InvClientePK invClientePK;
    private String cliIdNumero;
    private String cliRazonSocial; //antes era el nombre
    private String cliCodigoEstablecimiento;
    private String cliDireccion;
    private String cliTelefono;
    private String cliEmail;

    public InvCliente() {
    }

    public InvCliente(InvClientePK invClientePK) {
        this.invClientePK = invClientePK;
    }

    public InvCliente(String cliEmpresa, String cliCodigo) {
        this.invClientePK = new InvClientePK(cliEmpresa, cliCodigo);
    }

    public InvClientePK getInvClientePK() {
        return invClientePK;
    }

    public void setInvClientePK(InvClientePK invClientePK) {
        this.invClientePK = invClientePK;
    }

    public String getCliIdNumero() {
        return cliIdNumero;
    }

    public void setCliIdNumero(String cliIdNumero) {
        this.cliIdNumero = cliIdNumero;
    }

    public String getCliRazonSocial() {
        return cliRazonSocial;
    }

    public void setCliRazonSocial(String cliRazonSocial) {
        this.cliRazonSocial = cliRazonSocial;
    }

    public String getCliDireccion() {
        return cliDireccion;
    }

    public void setCliDireccion(String cliDireccion) {
        this.cliDireccion = cliDireccion;
    }

    public String getCliTelefono() {
        return cliTelefono;
    }

    public void setCliTelefono(String cliTelefono) {
        this.cliTelefono = cliTelefono;
    }

    public String getCliEmail() {
        return cliEmail;
    }

    public void setCliEmail(String cliEmail) {
        this.cliEmail = cliEmail;
    }

    public String getCliCodigoEstablecimiento() {
        return cliCodigoEstablecimiento;
    }

    public void setCliCodigoEstablecimiento(String cliCodigoEstablecimiento) {
        this.cliCodigoEstablecimiento = cliCodigoEstablecimiento;
    }

}
