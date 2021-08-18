package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "AutorizacionComprobantes", targetNamespace = "http://ec.gob.sri.ws.autorizacion")
public interface AutorizacionComprobantes {

    @WebMethod
    @WebResult(name = "RespuestaAutorizacionComprobante", targetNamespace = "")
    @RequestWrapper(localName = "autorizacionComprobante", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.acosux.SRIMS.service.AutorizacionComprobante")
    @ResponseWrapper(localName = "autorizacionComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.acosux.SRIMS.service.AutorizacionComprobanteResponse")
    public RespuestaComprobante autorizacionComprobante(@WebParam(name = "claveAccesoComprobante", targetNamespace = "") String claveAccesoComprobante);

}
