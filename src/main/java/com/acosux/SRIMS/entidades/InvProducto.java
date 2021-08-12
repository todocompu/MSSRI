package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvProducto implements Serializable {

    protected InvProductoPK invProductoPK;
    private String proIva;
    private String proNombre;
    private InvProductoMedida invProductoMedida;

    public InvProducto() {
    }

    public InvProducto(InvProductoPK invProductoPK) {
        this.invProductoPK = invProductoPK;
    }

    public InvProductoPK getInvProductoPK() {
        return invProductoPK;
    }

    public void setInvProductoPK(InvProductoPK invProductoPK) {
        this.invProductoPK = invProductoPK;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProIva() {
        return proIva;
    }

    public void setProIva(String proIva) {
        this.proIva = proIva;
    }

    public InvProductoMedida getInvProductoMedida() {
        return invProductoMedida;
    }

    public void setInvProductoMedida(InvProductoMedida invProductoMedida) {
        this.invProductoMedida = invProductoMedida;
    }

}
