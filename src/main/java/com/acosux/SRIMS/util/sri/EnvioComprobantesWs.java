/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

/**
 *
 * @author mario
 */
public class EnvioComprobantesWs {

    public static final String ESTADO_RECIBIDA = "RECIBIDA";
    public static final String ESTADO_DEVUELTA = "DEVUELTA";

    public static String obtenerMensajeRespuesta(RespuestaSolicitud respuesta) {
        StringBuilder mensajeDesplegable = new StringBuilder();
        if (respuesta.getEstado().equals("DEVUELTA") == true) {
            RespuestaSolicitud.Comprobantes comprobantes = respuesta.getComprobantes();
            for (Comprobante comp : comprobantes.getComprobante()) {
                mensajeDesplegable.append("Clave de Acceso: ").append(comp.getClaveAcceso()).append(" ");
                for (Mensaje m : comp.getMensajes().getMensaje()) {
                    mensajeDesplegable.append("-(CE(").append(m.getIdentificador()).append("): ").append(m.getMensaje()).append(")- ");
                    mensajeDesplegable.append(m.getInformacionAdicional() != null ? "Información Adicional: " + m.getInformacionAdicional() : "");
                }
            }
        }
        return mensajeDesplegable.toString();
    }
}
