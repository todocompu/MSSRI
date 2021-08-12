package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.naming.spi.ObjectFactory;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "AutorizacionComprobantes", targetNamespace = "http://ec.gob.sri.ws.autorizacion")
public interface AutorizacionComprobantes {

    @WebMethod
    @WebResult(name = "RespuestaAutorizacionComprobante", targetNamespace = "")
    @RequestWrapper(localName = "autorizacionComprobante", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "ec.com.todocompu.ShrimpSoftUtils.sri.ws.autorizacion.AutorizacionComprobante") // ec.com.todocompu.ShrimpSoftServer.util.sri.ws.autorizacion
    @ResponseWrapper(localName = "autorizacionComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "ec.com.todocompu.ShrimpSoftUtils.sri.ws.autorizacion.AutorizacionComprobanteResponse")
    public RespuestaComprobante autorizacionComprobante(@WebParam(name = "claveAccesoComprobante", targetNamespace = "") String claveAccesoComprobante);

}
