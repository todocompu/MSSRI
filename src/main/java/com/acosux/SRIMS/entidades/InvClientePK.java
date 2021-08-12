package com.acosux.SRIMS.entidades;

import java.io.Serializable;

public class InvClientePK implements Serializable {

    private String cliEmpresa;
    private String cliCodigo;

    public InvClientePK() {
    }

    public InvClientePK(String cliEmpresa, String cliCodigo) {
        this.cliEmpresa = cliEmpresa;
        this.cliCodigo = cliCodigo;
    }

    public String getCliEmpresa() {
        return cliEmpresa;
    }

    public void setCliEmpresa(String cliEmpresa) {
        this.cliEmpresa = cliEmpresa;
    }

    public String getCliCodigo() {
        return cliCodigo;
    }

    public void setCliCodigo(String cliCodigo) {
        this.cliCodigo = cliCodigo;
    }

}
