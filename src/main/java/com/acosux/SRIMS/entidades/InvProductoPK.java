package com.acosux.SRIMS.entidades;

import java.io.Serializable;


public class InvProductoPK implements Serializable {

    private String proEmpresa;
    private String proCodigoPrincipal;

    public InvProductoPK() {
    }

    public String getProEmpresa() {
        return proEmpresa;
    }

    public void setProEmpresa(String proEmpresa) {
        this.proEmpresa = proEmpresa;
    }

    public String getProCodigoPrincipal() {
        return proCodigoPrincipal;
    }

    public void setProCodigoPrincipal(String proCodigoPrincipal) {
        this.proCodigoPrincipal = proCodigoPrincipal;
    }

}
