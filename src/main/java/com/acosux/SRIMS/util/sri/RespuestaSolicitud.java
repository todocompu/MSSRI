package com.acosux.SRIMS.util.sri;

import java.util.ArrayList;
import java.util.List;

public class RespuestaSolicitud {

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
