package com.acosux.SRIMS.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaComprobante", propOrder = {"claveAccesoConsultada",
    "numeroComprobantes", "autorizaciones"})
public class RespuestaComprobante implements java.io.Serializable {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"autorizacion"})
    public static class Autorizaciones implements java.io.Serializable {

        protected List<Autorizacion> autorizacion;

        public List<Autorizacion> getAutorizacion() {
            if (autorizacion == null) {
                autorizacion = new ArrayList<>();
            }
            return this.autorizacion;
        }
    }

    protected String claveAccesoConsultada;
    protected String numeroComprobantes;

    protected RespuestaComprobante.Autorizaciones autorizaciones;

    public RespuestaComprobante.Autorizaciones getAutorizaciones() {
        return autorizaciones;
    }

    public String getClaveAccesoConsultada() {
        return claveAccesoConsultada;
    }

    public String getNumeroComprobantes() {
        return numeroComprobantes;
    }

    public void setAutorizaciones(RespuestaComprobante.Autorizaciones value) {
        this.autorizaciones = value;
    }

    public void setClaveAccesoConsultada(String value) {
        this.claveAccesoConsultada = value;
    }

    public void setNumeroComprobantes(String value) {
        this.numeroComprobantes = value;
    }

}
