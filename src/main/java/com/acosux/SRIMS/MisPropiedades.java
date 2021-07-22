/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component
public class MisPropiedades {

    @Value("${pruebas}")
    public String urlPruebas;
    @Value("${produccion}")
    public String urlProduccion;

    public String getUrlPruebas() {
        return urlPruebas;
    }

    public void setUrlPruebas(String urlPruebas) {
        this.urlPruebas = urlPruebas;
    }

    public String getUrlProduccion() {
        return urlProduccion;
    }

    public void setUrlProduccion(String urlProduccion) {
        this.urlProduccion = urlProduccion;
    }

}
