/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.controller;

import com.acosux.SRIMS.entidades.AnxCompraDetalleTO;
import com.acosux.SRIMS.entidades.AnxCompraElectronicaTO;
import com.acosux.SRIMS.entidades.AnxCompraTO;
import com.acosux.SRIMS.entidades.AnxGuiaRemisionElectronicaTO;
import com.acosux.SRIMS.entidades.AnxLiquidacionComprasElectronicaTO;
import com.acosux.SRIMS.entidades.AnxValidezComprobanteElectronico;
import com.acosux.SRIMS.entidades.AnxVentaElectronicaTO;
import com.acosux.SRIMS.entidades.AnxVentaExportacion;
import com.acosux.SRIMS.entidades.AnxVentaReembolsoTO;
import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvComprasDetalle;
import com.acosux.SRIMS.entidades.InvComprasTO;
import com.acosux.SRIMS.entidades.InvGuiaRemision;
import com.acosux.SRIMS.entidades.InvGuiaRemisionDetalleTO;
import com.acosux.SRIMS.entidades.InvListaDetalleVentasTO;
import com.acosux.SRIMS.entidades.InvProveedor;
import com.acosux.SRIMS.entidades.InvTransportista;
import com.acosux.SRIMS.entidades.InvVentaGuiaRemision;
import com.acosux.SRIMS.entidades.InvVentas;
import com.acosux.SRIMS.entidades.InvVentasPK;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.service.ComprobantesSRIService;
import com.acosux.SRIMS.service.RetencionesSriService;
import com.acosux.SRIMS.service.SriService;
import com.acosux.SRIMS.util.ComprobantesUtil;
import com.acosux.SRIMS.util.LlenarObjetoAnx;
import com.acosux.SRIMS.util.sri.Autorizacion;
import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import com.acosux.SRIMS.util.RespuestaWebTO;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.UtilsJSON;
import com.acosux.SRIMS.util.sri.AutorizacionComprobantesWs;
import com.acosux.SRIMS.util.sri.GenerarXMLFactura;
import com.acosux.SRIMS.util.sri.GenerarXMLFacturaReembolso;
import com.acosux.SRIMS.util.sri.GenerarXMLGuiaRemision;
import com.acosux.SRIMS.util.sri.GenerarXMLNotaCredito;
import com.acosux.SRIMS.util.sri.GenerarXMLNotaDebito;
import com.acosux.SRIMS.util.sri.GenerarXMLRetencion;
import com.acosux.SRIMS.util.sri.GenerarXMLiquidacionCompras;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.guiaremision.GuiaRemision;
import com.acosux.SRIMS.util.sri.modelo.liquidacioncompra.LiquidacionCompra;
import com.acosux.SRIMS.util.sri.modelo.retencion.ComprobanteRetencion;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mario
 */
@RestController
@RequestMapping("/consultasSRI/")
public class ConsultasSRIController {

    @Autowired
    SriService sriService;
    @Autowired
    ComprobantesSRIService comprobantesSRIService;
    @Autowired
    RetencionesSriService retencionesSriService;

    @RequestMapping(value = "/consultaDatosRucCedula", method = {RequestMethod.POST})
    public RespuestaWebTO consultaDatosRucCedula(@RequestBody Map<String, Object> map) {
        RespuestaWebTO resp = new RespuestaWebTO();
        resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
        String identificacion = (String) map.get("identificacion");
        String tipo = (String) map.get("tipo");//C o R
        try {
            Map<String, Object> respues = sriService.consultaDatosRucCedula(identificacion, tipo.charAt(0));
            if (respues != null) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    /*consultar validez de comprobantes*/
    @RequestMapping("/getAutorizadocionComprobantes")
    public RespuestaWebTO getAutorizadocionComprobantes(@RequestBody Map<String, Object> map) {
        RespuestaWebTO resp = new RespuestaWebTO();
        resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            if (claveAcceso.length() != 49) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                resp.setOperacionMensaje("La longitud de la clave de acceso no es la correcta.");
            } else {
                RespuestaComprobante respuestaComprobante = comprobantesSRIService.getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
                if (respuestaComprobante == null || respuestaComprobante.getAutorizaciones() == null || respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
                    resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                    resp.setOperacionMensaje("Comprobante electrónico no encontrado.");
                } else {
                    for (Autorizacion autorizacion : respuestaComprobante.getAutorizaciones().getAutorizacion()) {
                        if (autorizacion.getEstado().equalsIgnoreCase("AUTORIZADO")) {
                            Autorizacion comprobanteAutorizado = autorizacion;
                            resp.setExtraInfo(respuestaComprobante);
                            AnxValidezComprobanteElectronico itemValidezComprobanteElectronico = AnxValidezComprobanteElectronico.convertirXMLEnITEM(comprobanteAutorizado);
                            itemValidezComprobanteElectronico.setClave(claveAcceso);
                            itemValidezComprobanteElectronico.setRespuestaComprobante(respuestaComprobante);
                            resp.setExtraInfo(itemValidezComprobanteElectronico);
                            return resp;
                        }
                    }
                }
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getRespuestaComprobante")
    public RespuestaComprobante getRespuestaComprobante(@RequestBody Map<String, Object> map) {
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            if (claveAcceso.length() != 49) {
                return null;
            } else {
                RespuestaComprobante respuestaComprobante = comprobantesSRIService.getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
                return respuestaComprobante;
            }
        } catch (Exception e) {
            //cuando se Cae el SRI LLEGA ACÁ
            return null;
        }
    }

    @RequestMapping("/getRecepcionComprobantes")
    public RespuestaSolicitud getRecepcionComprobantes(@RequestBody Map<String, Object> map) {
        byte[] eXmlFirmado = (byte[]) map.get("eXmlFirmado");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            RespuestaSolicitud respuestaComprobante = comprobantesSRIService.getRecepcionComprobantes(eXmlFirmado, tipoAmbiente);
            return respuestaComprobante;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/generarXmlComprobantes")
    public String generarXmlComprobantes(@RequestBody Map<String, Object> map) {
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            RespuestaComprobante respuestaComprobante = comprobantesSRIService.getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
            String respuestaAutoriz = AutorizacionComprobantesWs.generarXmlComprobantes(respuestaComprobante, claveAcceso + ".xml");
            return respuestaAutoriz;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/exportarXmlAutorizacionComprobantes")
    public RespuestaWebTO exportarXmlAutorizacionComprobantes(@RequestBody Map<String, Object> map) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoAmbiente = (String) map.get("tipoAmbiente");
        try {
            RespuestaComprobante respuestaComprobante = comprobantesSRIService.getAutorizadocionComprobantes(claveAcceso, tipoAmbiente);
            for (Autorizacion autorizacion : respuestaComprobante.getAutorizaciones().getAutorizacion()) {
                if (autorizacion.getEstado().equalsIgnoreCase("AUTORIZADO")) {
                    resp.setExtraInfo(autorizacion.getComprobante());
                } else {
                    resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                    resp.setOperacionMensaje("No se ha podido generar el XML.");
                }
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/enviarAutorizarRetencion")
    public RespuestaWebTO enviarAutorizarRetencion(@RequestBody Map<String, Object> map) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        String claveAcceso = (String) map.get("claveAcceso");
        String agenteRetencion = (String) map.get("agenteRetencion");
        String passSignature = (String) map.get("passSignature");
        Emisor emisor = UtilsJSON.jsonToObjeto(Emisor.class, map.get("emisor"));
        InvComprasTO invComprasTO = UtilsJSON.jsonToObjeto(InvComprasTO.class, map.get("invComprasTO"));
        AnxCompraTO anxCompraTO = UtilsJSON.jsonToObjeto(AnxCompraTO.class, map.get("anxCompraTO"));
        List<AnxCompraDetalleTO> listAnxCompraDetalleTO = UtilsJSON.jsonToList(AnxCompraDetalleTO.class, map.get("listAnxCompraDetalleTO"));
        InvProveedor invProveedor = UtilsJSON.jsonToObjeto(InvProveedor.class, map.get("invProveedor"));
        byte[] archivoFirma = UtilsJSON.jsonToObjeto(byte[].class, map.get("archivoFirma"));
        SisEmpresaParametros sisEmpresaParametros = UtilsJSON.jsonToObjeto(SisEmpresaParametros.class, map.get("sisEmpresaParametros"));
        UtilsArchivos util = new UtilsArchivos();
        GenerarXMLRetencion generarXMLRetencion = new GenerarXMLRetencion();
        String mensaje = "F";
        try {
            ComprobanteRetencion comprobanteRetencion = generarXMLRetencion.generarComprobanteDeRetencion(invComprasTO, anxCompraTO, listAnxCompraDetalleTO, invProveedor, claveAcceso, emisor, agenteRetencion, sisEmpresaParametros);
            if (comprobanteRetencion == null) {
                mensaje = "FIngrese los campos obligatorios del comprobante ...";
            } else {
                String respuestaAutorizacion = ComprobantesUtil.realizaMarshal(comprobanteRetencion, util.getRutaComprobnateGenerados() + claveAcceso + ".xml");
                System.out.println("respuestaAutorizacion: " + respuestaAutorizacion);
                System.out.println("Clave de Acceso: " + claveAcceso);
                if (respuestaAutorizacion != null) {
                    mensaje = "FError al tratar de crear el archivo correspondiente al comprobante:\n" + respuestaAutorizacion + "Se ha producido un error ";
                } else {
                    String xmlPath = util.getRutaComprobnateGenerados() + claveAcceso + ".xml";
                    String respuestaFirma = ComprobantesUtil.firmarArchivo(xmlPath, archivoFirma, passSignature, util.getRutaComprobnateFirmados(), claveAcceso + ".xml");
                    if (respuestaFirma == null || respuestaFirma.charAt(0) != 'T') {
                        mensaje = respuestaFirma;
                    } else if (respuestaFirma.charAt(0) == 'T') {
                        mensaje = retencionesSriService.envioComprobanteWebServiceSRI(emisor, claveAcceso);
                        if (mensaje != null && mensaje.equals("AUTORIZADO")) {
                            AnxCompraElectronicaTO anxCompraElectronicaTO = LlenarObjetoAnx.llenarObjetoAnxCompraElectronica(mensaje, anxCompraTO, emisor.getTipoAmbiente(), claveAcceso);
                            resp.setExtraInfo(anxCompraElectronicaTO);
                        }
                    }
                }
            }
            resp.setOperacionMensaje(mensaje);
            eliminarDirectorios(claveAcceso);
        } catch (Exception e) {
            eliminarDirectorios(claveAcceso);
            mensaje = "F" + e.getMessage();
            resp.setOperacionMensaje(mensaje);
        }
        return resp;
    }

    @RequestMapping("/enviarAutorizarFacturaElectronica")
    public RespuestaWebTO enviarAutorizarFacturaElectronica(@RequestBody Map<String, Object> map) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        String direccion = (String) map.get("direccion");
        String complementoDocumentoNumero = (String) map.get("complementoDocumentoNumero");
        String tipoIdentificacion = (String) map.get("tipoIdentificacion");
        String claveAcceso = (String) map.get("claveAcceso");
        String tipoComprobante = (String) map.get("tipoComprobante");
        String passSignature = (String) map.get("passSignature");
        Emisor emisor = UtilsJSON.jsonToObjeto(Emisor.class, map.get("emisor"));
        InvVentas invVentas = UtilsJSON.jsonToObjeto(InvVentas.class, map.get("invVentas"));
        InvVentaGuiaRemision guia = UtilsJSON.jsonToObjeto(InvVentaGuiaRemision.class, map.get("guia"));
        List<InvListaDetalleVentasTO> listaInvVentasDetalleTO = UtilsJSON.jsonToList(InvListaDetalleVentasTO.class, map.get("listaInvVentasDetalleTO"));
        List<AnxVentaReembolsoTO> listaAnxVentaReembolsoTO = UtilsJSON.jsonToList(AnxVentaReembolsoTO.class, map.get("listaAnxVentaReembolsoTO"));
        InvCliente invCliente = UtilsJSON.jsonToObjeto(InvCliente.class, map.get("invCliente"));
        SisEmpresaParametros sisEmpresaParametros = UtilsJSON.jsonToObjeto(SisEmpresaParametros.class, map.get("sisEmpresaParametros"));
        Date fechaComplemento = UtilsJSON.jsonToObjeto(Date.class, map.get("fechaComplemento"));
        byte[] archivoFirma = UtilsJSON.jsonToObjeto(byte[].class, map.get("archivoFirma"));
        AnxVentaExportacion exportacion = UtilsJSON.jsonToObjeto(AnxVentaExportacion.class, map.get("exportacion"));

        UtilsArchivos util = new UtilsArchivos();
        String mensaje = "F";
        try {
            Object objComprobante = null;
            if (tipoComprobante.compareTo(TipoComprobanteEnum.FACTURA.getCode()) == 0) {
                GenerarXMLFactura generarXMLFactura = new GenerarXMLFactura();
                if (invVentas.isVtaReembolso() && listaAnxVentaReembolsoTO != null && listaAnxVentaReembolsoTO.size() > 0) {
                    GenerarXMLFacturaReembolso xmlReembolso = new GenerarXMLFacturaReembolso();
                    objComprobante = xmlReembolso.generarComprobanteFacturaReembolso(invVentas, invCliente, listaInvVentasDetalleTO, claveAcceso, emisor, tipoIdentificacion, direccion, listaAnxVentaReembolsoTO, sisEmpresaParametros);
                } else {
                    objComprobante = generarXMLFactura.generarComprobanteFactura(invVentas, invCliente, listaInvVentasDetalleTO, claveAcceso, emisor, tipoIdentificacion, guia, direccion, exportacion, sisEmpresaParametros);
                }
            } else if (tipoComprobante.compareTo(TipoComprobanteEnum.NOTA_DE_CREDITO.getCode()) == 0) {
                GenerarXMLNotaCredito generarXMLNotaCredito = new GenerarXMLNotaCredito();
                objComprobante = generarXMLNotaCredito.generarComprobanteNotaCredito(invVentas, invCliente, listaInvVentasDetalleTO, claveAcceso, emisor, tipoIdentificacion, complementoDocumentoNumero, fechaComplemento, sisEmpresaParametros);
            } else if (tipoComprobante.compareTo(TipoComprobanteEnum.NOTA_DE_DEBITO.getCode()) == 0) {
                GenerarXMLNotaDebito generarXMLNotaDebito = new GenerarXMLNotaDebito();
                objComprobante = generarXMLNotaDebito.generarComprobanteNotaDebito(invVentas, invCliente, listaInvVentasDetalleTO, claveAcceso, emisor, tipoIdentificacion, complementoDocumentoNumero, fechaComplemento, sisEmpresaParametros);
            }
            if (objComprobante == null) {
                mensaje = "FError al crear el XML  ...";
            } else {
                String respuestaAutorizacion = ComprobantesUtil.realizaMarshal(objComprobante, util.getRutaComprobnateGenerados() + claveAcceso + ".xml");
                System.out.println("respuestaAutorizacion: " + respuestaAutorizacion);
                if (respuestaAutorizacion != null) {
                    mensaje = "FError al tratar de crear el archivo correspondiente de la factura:\n" + respuestaAutorizacion + "Se ha producido un error ";
                } else {
                    String xmlPath = util.getRutaComprobnateGenerados() + claveAcceso + ".xml";
                    String pathOut = util.getRutaComprobnateFirmados();
                    String respuestaFirma = ComprobantesUtil.firmarArchivo(xmlPath, archivoFirma, passSignature, pathOut, claveAcceso + ".xml");
                    if (respuestaFirma == null || respuestaFirma.charAt(0) != 'T') {
                        mensaje = respuestaFirma;
                    } else if (respuestaFirma.charAt(0) == 'T') {
                        mensaje = retencionesSriService.envioComprobanteWebServiceSRI(emisor, claveAcceso);
                        if (mensaje != null && mensaje.equals("AUTORIZADO")) {
                            InvVentasPK pk = invVentas.getInvVentasPK();
                            AnxVentaElectronicaTO anxVentaElectronicaTO = LlenarObjetoAnx.llenarObjetoAnxVentaElectronicaTO(pk.getVtaEmpresa(), pk.getVtaPeriodo(), pk.getVtaMotivo(), pk.getVtaNumero(), claveAcceso, UtilsArchivos.fecha(invVentas.getVtaFecha(), "yyyy-MM-dd"), mensaje, emisor);
                            resp.setExtraInfo(anxVentaElectronicaTO);
                        }
                    }
                }
            }
            resp.setOperacionMensaje(mensaje);
            eliminarDirectorios(claveAcceso);
        } catch (Exception e) {
            eliminarDirectorios(claveAcceso);
            mensaje = "F" + e.getMessage();
            resp.setOperacionMensaje(mensaje);
        }
        return resp;
    }

    @RequestMapping("/enviarAutorizarLiquidacionCompras")
    public RespuestaWebTO enviarAutorizarLiquidacionCompras(@RequestBody Map<String, Object> map) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        String claveAcceso = (String) map.get("claveAcceso");
        String agenteRetencion = (String) map.get("agenteRetencion");
        String passSignature = (String) map.get("passSignature");
        Emisor emisor = UtilsJSON.jsonToObjeto(Emisor.class, map.get("emisor"));
        InvComprasTO invComprasTO = UtilsJSON.jsonToObjeto(InvComprasTO.class, map.get("invComprasTO"));
        List<InvComprasDetalle> listaInvComprasDetalle = UtilsJSON.jsonToList(InvComprasDetalle.class, map.get("listaInvComprasDetalle"));
        InvProveedor invProveedor = UtilsJSON.jsonToObjeto(InvProveedor.class, map.get("invProveedor"));
        byte[] archivoFirma = UtilsJSON.jsonToObjeto(byte[].class, map.get("archivoFirma"));
        SisEmpresaParametros sisEmpresaParametros = UtilsJSON.jsonToObjeto(SisEmpresaParametros.class, map.get("sisEmpresaParametros"));
        UtilsArchivos util = new UtilsArchivos();
        String mensaje = "F";
        try {
            GenerarXMLiquidacionCompras generarXMLiquidacionCompras = new GenerarXMLiquidacionCompras();
            LiquidacionCompra liquidacionCompra = generarXMLiquidacionCompras.generarXMLiquidacionCompras(invComprasTO, invProveedor, listaInvComprasDetalle, claveAcceso, emisor, agenteRetencion, sisEmpresaParametros);
            if (liquidacionCompra == null) {
                mensaje = "FIngrese los campos obligatorios del comprobante ...";
            } else {
                String respuestaAutorizacion = ComprobantesUtil.realizaMarshal(liquidacionCompra, util.getRutaComprobnateGenerados() + claveAcceso + ".xml");
                System.out.println("respuestaAutorizacion: " + respuestaAutorizacion);
                System.out.println("Clave de Acceso: " + claveAcceso);
                if (respuestaAutorizacion != null) {
                    mensaje = "FError al tratar de crear el archivo correspondiente al comprobante:\n" + respuestaAutorizacion + "Se ha producido un error ";
                } else {
                    String xmlPath = util.getRutaComprobnateGenerados() + claveAcceso + ".xml";
                    String respuestaFirma = ComprobantesUtil.firmarArchivo(xmlPath, archivoFirma, passSignature, util.getRutaComprobnateFirmados(), claveAcceso + ".xml");
                    if (respuestaFirma == null || respuestaFirma.charAt(0) != 'T') {
                        mensaje = respuestaFirma;
                    } else if (respuestaFirma.charAt(0) == 'T') {
                        mensaje = retencionesSriService.envioComprobanteWebServiceSRI(emisor, claveAcceso);
                        if (mensaje != null && mensaje.equals("AUTORIZADO")) {
                            AnxLiquidacionComprasElectronicaTO anxLiquidacionComprasElectronicaTO = LlenarObjetoAnx.llenarObjetoAnxLiquidacionComprasElectronicaTO(invComprasTO.getEmpCodigo(),
                                    invComprasTO.getCompPeriodo(), invComprasTO.getCompMotivo(), invComprasTO.getCompNumero(), claveAcceso,
                                    invComprasTO.getCompFecha(), mensaje, emisor);
                            resp.setExtraInfo(anxLiquidacionComprasElectronicaTO);
                        }
                    }
                }
            }
            resp.setOperacionMensaje(mensaje);
            eliminarDirectorios(claveAcceso);
        } catch (Exception e) {
            eliminarDirectorios(claveAcceso);
            mensaje = "F" + e.getMessage();
            resp.setOperacionMensaje(mensaje);
        }
        return resp;
    }

    @RequestMapping("/enviarAutorizarGuiaRemision")
    public RespuestaWebTO enviarAutorizarGuiaRemision(@RequestBody Map<String, Object> map) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        String claveAcceso = (String) map.get("claveAcceso");
        String agenteRetencion = (String) map.get("agenteRetencion");
        String passSignature = (String) map.get("passSignature");
        String fechaVenta = (String) map.get("fechaVenta");
        String emision = UtilsJSON.jsonToObjeto(String.class, map.get("emision"));
        String inicio = UtilsJSON.jsonToObjeto(String.class, map.get("inicio"));
        String fin = UtilsJSON.jsonToObjeto(String.class, map.get("fin"));
        Emisor emisor = UtilsJSON.jsonToObjeto(Emisor.class, map.get("emisor"));
        InvGuiaRemision invGuiaRemision = UtilsJSON.jsonToObjeto(InvGuiaRemision.class, map.get("invGuiaRemision"));
        InvTransportista invTransportista = UtilsJSON.jsonToObjeto(InvTransportista.class, map.get("invTransportista"));
        List<InvGuiaRemisionDetalleTO> listaInvGuiaRemisionDetalleTO = UtilsJSON.jsonToList(InvGuiaRemisionDetalleTO.class, map.get("listaInvGuiaRemisionDetalleTO"));
        InvCliente invCliente = UtilsJSON.jsonToObjeto(InvCliente.class, map.get("invCliente"));
        byte[] archivoFirma = UtilsJSON.jsonToObjeto(byte[].class, map.get("archivoFirma"));
        SisEmpresaParametros sisEmpresaParametros = UtilsJSON.jsonToObjeto(SisEmpresaParametros.class, map.get("sisEmpresaParametros"));
        UtilsArchivos util = new UtilsArchivos();
        String mensaje = "F";
        try {
            //formatoFechas
            invGuiaRemision.setGuiaFechaEmision(UtilsArchivos.fecha(emision, "yyyy-MM-dd"));
            invGuiaRemision.setGuiaFechaInicioTransporte(UtilsArchivos.fecha(inicio, "yyyy-MM-dd"));
            invGuiaRemision.setGuiaFechaFinTransporte(UtilsArchivos.fecha(fin, "yyyy-MM-dd"));
            GenerarXMLGuiaRemision generarXMLGuiaRemision = new GenerarXMLGuiaRemision();
            GuiaRemision guiaRemision = generarXMLGuiaRemision.generarXMGuiaRemision(invGuiaRemision, invTransportista, invCliente, listaInvGuiaRemisionDetalleTO, claveAcceso, emisor, fechaVenta, agenteRetencion, sisEmpresaParametros);
            if (guiaRemision == null) {
                mensaje = "FIngrese los campos obligatorios del comprobante ...";
            } else {
                String respuestaAutorizacion = ComprobantesUtil.realizaMarshal(guiaRemision, util.getRutaComprobnateGenerados() + claveAcceso + ".xml");
                if (respuestaAutorizacion != null) {
                    mensaje = "FError al tratar de crear el archivo correspondiente al comprobante:\n" + respuestaAutorizacion + "Se ha producido un error ";
                } else {
                    String xmlPath = util.getRutaComprobnateGenerados() + claveAcceso + ".xml";
                    String respuestaFirma = ComprobantesUtil.firmarArchivo(xmlPath, archivoFirma, passSignature, util.getRutaComprobnateFirmados(), claveAcceso + ".xml");
                    if (respuestaFirma == null || respuestaFirma.charAt(0) != 'T') {
                        mensaje = respuestaFirma;
                    } else if (respuestaFirma.charAt(0) == 'T') {
                        mensaje = retencionesSriService.envioComprobanteWebServiceSRI(emisor, claveAcceso);
                        if (mensaje != null && mensaje.equals("AUTORIZADO")) {
                            AnxGuiaRemisionElectronicaTO anxGuiaRemisionElectronicaTO = LlenarObjetoAnx.llenarObjetoAnxGuiaRemisionElectronicaTO(invGuiaRemision.getInvGuiaRemisionPK().getGuiaEmpresa(),
                                    invGuiaRemision.getInvGuiaRemisionPK().getGuiaPeriodo(), invGuiaRemision.getInvGuiaRemisionPK().getGuiaNumero(), claveAcceso, UtilsArchivos.fecha(invGuiaRemision.getGuiaFechaEmision(), "yyyy-MM-dd"), mensaje, emisor);
                            resp.setExtraInfo(anxGuiaRemisionElectronicaTO);
                        }
                    }
                }
            }
            resp.setOperacionMensaje(mensaje);
            eliminarDirectorios(claveAcceso);
        } catch (Exception e) {
            eliminarDirectorios(claveAcceso);
            mensaje = "F" + e.getMessage();
            resp.setOperacionMensaje(mensaje);
        }
        return resp;
    }

    public void eliminarDirectorios(String claveAcceso) {
        UtilsArchivos util = new UtilsArchivos();
        File directorioComprobante = new File(util.getRutaComprobnateGenerados() + claveAcceso + ".xml");
        if (directorioComprobante.exists()) {
            directorioComprobante.delete();
        }
        directorioComprobante = new File(util.getRutaComprobnateFirmados() + claveAcceso + ".xml");
        if (directorioComprobante.exists()) {
            directorioComprobante.delete();
        }
        directorioComprobante = new File(util.getRutaComprobnateAutorizados() + claveAcceso + ".xml");
        if (directorioComprobante.exists()) {
            directorioComprobante.delete();
        }
    }

}
