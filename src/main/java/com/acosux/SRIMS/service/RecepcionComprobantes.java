package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "RecepcionComprobantes", targetNamespace = "http://ec.gob.sri.ws.recepcion")
public interface RecepcionComprobantes {

    @WebMethod
    @WebResult(name = "RespuestaRecepcionComprobante", targetNamespace = "")
    @RequestWrapper(localName = "validarComprobante", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "com.acosux.SRIMS.service.ValidarComprobante")
    @ResponseWrapper(localName = "validarComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "com.acosux.SRIMS.service.ValidarComprobanteResponse")
    public RespuestaSolicitud validarComprobante(
            @WebParam(name = "xml", targetNamespace = "") byte[] xml);

}
