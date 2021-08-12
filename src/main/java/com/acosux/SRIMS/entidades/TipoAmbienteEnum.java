/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

/**
 *
 * @author mario
 */
public enum TipoAmbienteEnum {
    PRODUCCION("2"), PRUEBAS("1");
    private final String code;

    private TipoAmbienteEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
