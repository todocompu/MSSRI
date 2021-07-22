/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.entidades;

import com.acosux.SRIMS.util.Autorizacion;
import com.acosux.SRIMS.util.RespuestaComprobante;
import com.acosux.SRIMS.util.UtilsArchivos;

public class AnxValidezComprobanteElectronico {

    String clave;
    String tipoComprobante;
    String ambiente;
    String razonSocialEmisor;
    String rucEmisor;
    String estado;
    String numeroDocumento;
    String fechaEmision;
    String fechaAutorizacion;
    String totalComprobante;
    String numeroAutorizacion;
    String codigoDocumento;
    RespuestaComprobante respuestaComprobante;

    public AnxValidezComprobanteElectronico() {
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getRazonSocialEmisor() {
        return razonSocialEmisor;
    }

    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    public String getRucEmisor() {
        return rucEmisor;
    }

    public void setRucEmisor(String rucEmisor) {
        this.rucEmisor = rucEmisor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getTotalComprobante() {
        return totalComprobante;
    }

    public void setTotalComprobante(String totalComprobante) {
        this.totalComprobante = totalComprobante;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public RespuestaComprobante getRespuestaComprobante() {
        return respuestaComprobante;
    }

    public void setRespuestaComprobante(RespuestaComprobante respuestaComprobante) {
        this.respuestaComprobante = respuestaComprobante;
    }

    public static AnxValidezComprobanteElectronico convertirXMLEnITEM(Autorizacion autorizacion) {

        AnxValidezComprobanteElectronico itemResultadoFinal = new AnxValidezComprobanteElectronico();

        /*TIPO COMPROBANTE*/
        String comprobante = "";
        String total = "";
        String tipoComprobante = autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<codDoc>") + 8, autorizacion.getComprobante().lastIndexOf("</codDoc>")).trim();

        if (tipoComprobante.equals(TipoComprobanteEnum.FACTURA.getCode())) {
            comprobante = TipoComprobanteEnum.FACTURA.getDescripcion();
            total = autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<importeTotal>") + 14, autorizacion.getComprobante().lastIndexOf("</importeTotal>")).trim();
        } else if (tipoComprobante.equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCode())) {
            comprobante = TipoComprobanteEnum.NOTA_DE_CREDITO.getDescripcion();
            total = autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<valorModificacion>") + 19, autorizacion.getComprobante().lastIndexOf("</valorModificacion>")).trim();
        } else if (tipoComprobante.equals(TipoComprobanteEnum.NOTA_DE_DEBITO.getCode())) {
            comprobante = TipoComprobanteEnum.NOTA_DE_DEBITO.getDescripcion();
            total = autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<valorTotal>") + 12, autorizacion.getComprobante().lastIndexOf("</valorTotal>")).trim();
        } else if (tipoComprobante.equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCode())) {
            comprobante = TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getDescripcion();
        }
        itemResultadoFinal.setTipoComprobante(comprobante);
        itemResultadoFinal.setTotalComprobante(total);

        /*AMBIENTE*/
        itemResultadoFinal.setAmbiente(
                autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<ambiente>") + 10, autorizacion.getComprobante().lastIndexOf("</ambiente>")).trim().equals("2")
                ? "2" : "1");

        /*RAZÓN SOCIAL EMISOR*/
        itemResultadoFinal.setRazonSocialEmisor(autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<razonSocial>") + 13, autorizacion.getComprobante().lastIndexOf("</razonSocial>")).trim());

        /*RUC DEL EMISOR*/
        itemResultadoFinal.setRucEmisor(autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<ruc>") + 5, autorizacion.getComprobante().lastIndexOf("</ruc>")).trim());

        /*ESTADO*/
        itemResultadoFinal.setEstado(autorizacion.getEstado());

        /*NÚMERO DOCUMENTO*/
        itemResultadoFinal.setNumeroDocumento(
                autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<estab>") + 7, autorizacion.getComprobante().lastIndexOf("</estab>")).trim()
                + "-"
                + autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<ptoEmi>") + 8, autorizacion.getComprobante().lastIndexOf("</ptoEmi>")).trim()
                + "-"
                + autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<secuencial>") + 12, autorizacion.getComprobante().lastIndexOf("</secuencial>")).trim());

        /*FECHA EMISIÓN*/
        itemResultadoFinal.setFechaEmision(autorizacion.getComprobante().substring(autorizacion.getComprobante().lastIndexOf("<fechaEmision>") + 14, autorizacion.getComprobante().lastIndexOf("</fechaEmision>")).trim());

        /*NUMERO AUTORIZACIÓN*/
        itemResultadoFinal.setNumeroAutorizacion(autorizacion.getNumeroAutorizacion());

        /*FECHA AUTORIZACIÓN*/
        itemResultadoFinal.setFechaAutorizacion(UtilsArchivos.fecha(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime(), "yyyy-MM-dd hh:mm:ss"));

        return itemResultadoFinal;
    }
}
