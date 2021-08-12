package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvProductoMedida implements Serializable {

    private String medDetalle;

    public InvProductoMedida() {
    }

    public String getMedDetalle() {
        return medDetalle;
    }

    public void setMedDetalle(String medDetalle) {
        this.medDetalle = medDetalle;
    }

}
