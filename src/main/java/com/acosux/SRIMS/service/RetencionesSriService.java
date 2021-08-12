/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.sri.modelo.Emisor;

/**
 *
 * @author mario
 */
public interface RetencionesSriService {

    public String envioComprobanteWebServiceSRI(Emisor emisor, String claveDeAcceso) throws Exception;
}
