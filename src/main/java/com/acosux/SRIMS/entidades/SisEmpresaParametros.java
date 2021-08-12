package com.acosux.SRIMS.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SisEmpresaParametros implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean parContribuyenteRegimenMicroempresa;
    private String parAgenteRetencion;

    public SisEmpresaParametros() {
    }

    public boolean isParContribuyenteRegimenMicroempresa() {
        return parContribuyenteRegimenMicroempresa;
    }

    public void setParContribuyenteRegimenMicroempresa(boolean parContribuyenteRegimenMicroempresa) {
        this.parContribuyenteRegimenMicroempresa = parContribuyenteRegimenMicroempresa;
    }

    public String getParAgenteRetencion() {
        return parAgenteRetencion;
    }

    public void setParAgenteRetencion(String parAgenteRetencion) {
        this.parAgenteRetencion = parAgenteRetencion;
    }

}
