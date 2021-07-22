/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.RespuestaComprobante;

/**
 *
 * @author mario
 */
public interface ComprobantesSRIService {

    public RespuestaComprobante getAutorizadocionComprobantes(String claveAcceso, String tipoAmbiente) throws Exception;

}
