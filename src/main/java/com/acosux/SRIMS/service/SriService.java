/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import java.util.Map;

/**
 *
 * @author mario
 */
public interface SriService {

    public Map<String, Object> consultaDatosRucCedula(String numero, char tipo);

}
