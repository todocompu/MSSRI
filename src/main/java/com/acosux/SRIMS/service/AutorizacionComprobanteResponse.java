
package com.acosux.SRIMS.service;


import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autorizacionComprobanteResponse", propOrder = { "respuestaAutorizacionComprobante" })
public class AutorizacionComprobanteResponse {

	@XmlElement(name = "RespuestaAutorizacionComprobante")
	protected RespuestaComprobante respuestaAutorizacionComprobante;

	public RespuestaComprobante getRespuestaAutorizacionComprobante() {
		return respuestaAutorizacionComprobante;
	}

	public void setRespuestaAutorizacionComprobante(RespuestaComprobante value) {
		this.respuestaAutorizacionComprobante = value;
	}

}