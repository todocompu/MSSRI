/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.RespuestaComprobante;
import com.acosux.SRIMS.util.UtilsWebService;
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

}
