/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.controller;

import com.acosux.SRIMS.entidades.AnxValidezComprobanteElectronico;
import com.acosux.SRIMS.service.ComprobantesSRIService;
import com.acosux.SRIMS.service.SriService;
import com.acosux.SRIMS.util.Autorizacion;
import com.acosux.SRIMS.util.RespuestaComprobante;
import com.acosux.SRIMS.util.RespuestaWebTO;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mario
 */
@RestController
@RequestMapping("/consultasSRI/")
public class ConsultasSRIController {

    @Autowired
    SriService sriService;
    @Autowired
    ComprobantesSRIService comprobantesSRIService;

    @RequestMapping(value = "/consultaDatosRucCedula", method = {RequestMethod.POST})
    public RespuestaWebTO consultaDatosRucCedula(@RequestBody Map<String, Object> map) {
        RespuestaWebTO resp = new RespuestaWebTO();
        resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
        String identificacion = (String) map.get("identificacion");
        String tipo = (String) map.get("tipo");//C o R
        try {
            Map<String, Object> respues = sriService.consultaDatosRucCedula(identificacion, tipo.charAt(0));
            if (respues != null) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    /*consultar validez de comprobantes*/
    @RequestMapping("/getAutorizadocionComprobantes")
    public RespuestaWebTO getAutorizadocionComprobantes(@RequestBody Map<String, Object> map) {
        RespuestaWebTO resp = new RespuestaWebTO();
        resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            if (claveAcceso.length() != 49) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                resp.setOperacionMensaje("La longitud de la clave de acceso no es la correcta.");
            } else {
                RespuestaComprobante respuestaComprobante = comprobantesSRIService.getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
                if (respuestaComprobante == null || respuestaComprobante.getAutorizaciones() == null || respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
                    resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                    resp.setOperacionMensaje("Comprobante electrónico no encontrado.");
                } else {
                    for (Autorizacion autorizacion : respuestaComprobante.getAutorizaciones().getAutorizacion()) {
                        if (autorizacion.getEstado().equalsIgnoreCase("AUTORIZADO")) {
                            Autorizacion comprobanteAutorizado = autorizacion;
                            resp.setExtraInfo(respuestaComprobante);
                            AnxValidezComprobanteElectronico itemValidezComprobanteElectronico = AnxValidezComprobanteElectronico.convertirXMLEnITEM(comprobanteAutorizado);
                            itemValidezComprobanteElectronico.setClave(claveAcceso);
                            itemValidezComprobanteElectronico.setRespuestaComprobante(respuestaComprobante);
                            resp.setExtraInfo(itemValidezComprobanteElectronico);
                            return resp;
                        }
                    }
                }
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

}
