package com.acosux.SRIMS.util.sri;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaSolicitud", propOrder = {"estado", "comprobantes"})
@XmlRootElement(name = "RespuestaSolicitud")
public class RespuestaSolicitud {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"comprobante"})
    public static class Comprobantes implements java.io.Serializable {

        protected List<Comprobante> comprobante;

        public List<Comprobante> getComprobante() {
            if (comprobante == null) {
                comprobante = new ArrayList<>();
            }
            return this.comprobante;
        }

    }

    protected String estado;

    protected RespuestaSolicitud.Comprobantes comprobantes;

    public RespuestaSolicitud.Comprobantes getComprobantes() {
        return comprobantes;
    }

    public String getEstado() {
        return estado;
    }

    public void setComprobantes(RespuestaSolicitud.Comprobantes value) {
        this.comprobantes = value;
    }

    public void setEstado(String value) {
        this.estado = value;
    }

}
