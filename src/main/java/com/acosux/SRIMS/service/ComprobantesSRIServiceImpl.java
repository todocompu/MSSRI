/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import com.acosux.SRIMS.util.UtilsWebService;
import com.acosux.SRIMS.util.sri.AnxComprobanteElectronicoUtilTO;
import com.acosux.SRIMS.util.sri.Comprobante;
import com.acosux.SRIMS.util.sri.Mensaje;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class ComprobantesSRIServiceImpl implements ComprobantesSRIService {

    UtilsWebService service = new UtilsWebService();

    @Override
    public RespuestaComprobante getAutorizadocionComprobantes(String claveAcceso, String tipoAmbiente) throws Exception {
        service.devuelveUrlWs(tipoAmbiente, "AutorizacionComprobantesOffline");
        return UtilsWebService.autorizacionComprobante(claveAcceso);
    }

    @Override
    public RespuestaSolicitud getRecepcionComprobantes(byte[] eXmlFirmado, String tipoAmbiente) throws Exception {
        service.devuelveUrlWs(tipoAmbiente, "RecepcionComprobantesOffline");
        return UtilsWebService.validarComprobante(eXmlFirmado);
    }

    @Override
    public AnxComprobanteElectronicoUtilTO getEnvioComprobanteElectronicosWS(byte[] eXmlFirmado, String claveAcceso, String tipoAmbiente) throws Exception {
        AnxComprobanteElectronicoUtilTO anxComprobanteElectronicoUtilTO = new AnxComprobanteElectronicoUtilTO();
        RespuestaSolicitud respuestaSolicitud = getRecepcionComprobantes(eXmlFirmado, tipoAmbiente);
        if (respuestaSolicitud == null) {
            anxComprobanteElectronicoUtilTO
                    .setMensaje("F<ERROR>: Sin conexión con Web Services del SRI, "
                            + "Certificados incorrectos(Prueba o Producción), "
                            + "Firma electrónica Incorrecta");
        } else {
            if (respuestaSolicitud.getEstado() == null) {
                anxComprobanteElectronicoUtilTO
                        .setMensaje("F<ERROR>: Sin conexión con Web Services del SRI, "
                                + "Certificados incorrectos(Prueba o Producción), "
                                + "Firma electrónica Incorrecta");
            } else {
                anxComprobanteElectronicoUtilTO.setMensaje("TRespuesta de comprobante SRI");
                String mensajeRecepcion = "";
                if (respuestaSolicitud.getEstado().compareTo("DEVUELTA") == 0) {
                    for (Comprobante comp : respuestaSolicitud.getComprobantes().getComprobante()) {
                        for (Mensaje m : comp.getMensajes().getMensaje()) {
                            if (m.getMensaje().compareTo("CLAVE ACCESO REGISTRADA") == 0) {
                                mensajeRecepcion = m.getMensaje();
                                break;
                            }
                        }
                    }
                }

                if (respuestaSolicitud.getEstado().equals("RECIBIDA")
                        || (respuestaSolicitud.getEstado().compareTo("DEVUELTA") == 0
                        && mensajeRecepcion.compareTo("CLAVE ACCESO REGISTRADA") == 0)) {
                    respuestaSolicitud.setEstado("RECIBIDA");
                    RespuestaComprobante respuestaComprobante = getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
                    anxComprobanteElectronicoUtilTO.setRespuestaComprobante(respuestaComprobante);
                }
                anxComprobanteElectronicoUtilTO.setRespuestaSolicitud(respuestaSolicitud);
            }
        }
        return anxComprobanteElectronicoUtilTO;
    }

}
