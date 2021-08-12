/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import java.io.Serializable;

/**
 *
 * @author CarolValdiviezo
 */
public class InvGuiaRemisionPK implements Serializable {

    private String guiaEmpresa;
    private String guiaPeriodo;
    private String guiaNumero;

    public InvGuiaRemisionPK() {
    }

    public String getGuiaEmpresa() {
        return guiaEmpresa;
    }

    public void setGuiaEmpresa(String guiaEmpresa) {
        this.guiaEmpresa = guiaEmpresa;
    }

    public String getGuiaPeriodo() {
        return guiaPeriodo;
    }

    public void setGuiaPeriodo(String guiaPeriodo) {
        this.guiaPeriodo = guiaPeriodo;
    }

    public String getGuiaNumero() {
        return guiaNumero;
    }

    public void setGuiaNumero(String guiaNumero) {
        this.guiaNumero = guiaNumero;
    }

}
