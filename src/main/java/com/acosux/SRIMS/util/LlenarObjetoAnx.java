/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util;

import com.acosux.SRIMS.entidades.AnxCompraElectronicaTO;
import com.acosux.SRIMS.entidades.AnxCompraTO;
import com.acosux.SRIMS.entidades.AnxGuiaRemisionElectronicaTO;
import com.acosux.SRIMS.entidades.AnxLiquidacionComprasElectronicaTO;
import com.acosux.SRIMS.entidades.AnxVentaElectronicaTO;
import com.acosux.SRIMS.entidades.TipoAmbienteEnum;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import java.io.File;

/**
 *
 * @author mario
 */
public class LlenarObjetoAnx {

    public static AnxCompraElectronicaTO llenarObjetoAnxCompraElectronica(String mensajeEnvioComprobanteAutorizado,
            AnxCompraTO anxCompraTO, String tipoAmbiente, String claveAcceso) throws Exception {

        AnxCompraElectronicaTO anxCompraElectronicaTO;
        String estado;
        String autorizacionNumero;
        String autorizacionFecha;
        File directorio;
        UtilsArchivos util = new UtilsArchivos();
        if (mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO")) {
            String xmlPathFirmadosAutorizados = util.getRutaComprobnateAutorizados() + claveAcceso + ".xml";
            directorio = new File(xmlPathFirmadosAutorizados);
            estado = ComprobantesUtil.obtenerValorXML(directorio, "/*/estado");
            autorizacionNumero = ComprobantesUtil.obtenerValorXML(directorio, "/*/numeroAutorizacion");
            autorizacionFecha = UtilsArchivos.fecha(ComprobantesUtil.obtenerValorXML(directorio, "/*/fechaAutorizacion"), "dd/MM/yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss");
        } else {
            estado = mensajeEnvioComprobanteAutorizado.substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("<") + 1, mensajeEnvioComprobanteAutorizado.lastIndexOf(">")).trim();
            autorizacionFecha = UtilsArchivos.fecha(anxCompraTO.getCompRetencionFechaEmision(), "yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss");
            autorizacionNumero = mensajeEnvioComprobanteAutorizado
                    .substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("-(") + 2, mensajeEnvioComprobanteAutorizado.lastIndexOf(")-"))
                    .trim();
            directorio = new File("auxxml.xml");
        }
        anxCompraElectronicaTO = new AnxCompraElectronicaTO();
        anxCompraElectronicaTO.seteSecuencial(0);
        anxCompraElectronicaTO.seteTipoAmbiente(tipoAmbiente.equals(TipoAmbienteEnum.PRUEBAS.getCode()) ? "PRUEBAS" : "PRODUCCION");
        anxCompraElectronicaTO.seteClaveAcceso(claveAcceso.trim());
        anxCompraElectronicaTO.seteEstado(estado);
        anxCompraElectronicaTO.seteAutorizacionNumero(autorizacionNumero.trim());
        anxCompraElectronicaTO.seteAutorizacionFecha(autorizacionFecha.trim());
        anxCompraElectronicaTO.seteXml((mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO") ? ComprobantesUtil.archivoToByte(directorio) : new byte[(int) directorio.length()]));
        anxCompraElectronicaTO.seteEnviadoPorCorreo(false);
        anxCompraElectronicaTO.setCompEmpresa(anxCompraTO.getEmpCodigo().trim());
        anxCompraElectronicaTO.setCompPeriodo(anxCompraTO.getPerCodigo().trim());
        anxCompraElectronicaTO.setCompMotivo(anxCompraTO.getMotCodigo().trim());
        anxCompraElectronicaTO.setCompNumero(anxCompraTO.getCompNumero().trim());
        anxCompraElectronicaTO.setUsrEmpresa(anxCompraTO.getEmpCodigo().trim());
        anxCompraElectronicaTO.setUsrFechaInserta(anxCompraTO.getCompRetencionFechaEmision().trim());
        return anxCompraElectronicaTO;
    }

    public static AnxVentaElectronicaTO llenarObjetoAnxVentaElectronicaTO(String vtaEmp, String vtaPerCodigo,
            String vtaMotCodigo, String vtaNumero, String claveAcceso, String fechaEmision, String mensajeEnvioComprobanteAutorizado, Emisor emisor) throws Exception {

        String estado;
        String autorizacionNumero;
        String autorizacionFecha;
        File directorio;
        UtilsArchivos util = new UtilsArchivos();
        if (mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO")) {
            String xmlPathFirmadosAutorizados = util.getRutaComprobnateAutorizados() + claveAcceso + ".xml";
            directorio = new File(xmlPathFirmadosAutorizados);
            estado = ComprobantesUtil.obtenerValorXML(directorio, "/*/estado");
            autorizacionNumero = ComprobantesUtil.obtenerValorXML(directorio, "/*/numeroAutorizacion");
            autorizacionFecha = UtilsArchivos.fecha(ComprobantesUtil.obtenerValorXML(directorio, "/*/fechaAutorizacion"), "dd/MM/yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss");
        } else {
            estado = mensajeEnvioComprobanteAutorizado.substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("<") + 1, mensajeEnvioComprobanteAutorizado.lastIndexOf(">")).trim();
            autorizacionFecha = fechaEmision;// "2014-11-19
            autorizacionNumero = mensajeEnvioComprobanteAutorizado
                    .substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("-(") + 2, mensajeEnvioComprobanteAutorizado.lastIndexOf(")-"))
                    .trim();
            directorio = new File("auxxml.xml");
        }
        AnxVentaElectronicaTO anxVentaElectronicaTO = new AnxVentaElectronicaTO();
        anxVentaElectronicaTO.seteSecuencial(0);
        anxVentaElectronicaTO.seteTipoAmbiente(emisor.getTipoAmbiente().equals(TipoAmbienteEnum.PRUEBAS.getCode()) ? TipoAmbienteEnum.PRUEBAS.toString() : TipoAmbienteEnum.PRODUCCION.toString());
        anxVentaElectronicaTO.seteClaveAcceso(claveAcceso);
        anxVentaElectronicaTO.seteEstado(estado);
        anxVentaElectronicaTO.seteAutorizacionNumero(autorizacionNumero);
        anxVentaElectronicaTO.seteAutorizacionFecha(autorizacionFecha);
        anxVentaElectronicaTO.seteXml((mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO") ? ComprobantesUtil.archivoToByte(directorio) : new byte[(int) directorio.length()]));
        anxVentaElectronicaTO.seteEnviadoPorCorreo(false);
        anxVentaElectronicaTO.setVtaEmpresa(vtaEmp);
        anxVentaElectronicaTO.setVtaPeriodo(vtaPerCodigo);
        anxVentaElectronicaTO.setVtaMotivo(vtaMotCodigo);
        anxVentaElectronicaTO.setVtaNumero(vtaNumero);///
        anxVentaElectronicaTO.setUsrEmpresa(vtaEmp);
        anxVentaElectronicaTO.setUsrFechaInserta(UtilsArchivos.fechaSistema());
        anxVentaElectronicaTO.setVtaFecha(fechaEmision.equals("  -  -    ") ? null : fechaEmision);
        return anxVentaElectronicaTO;
    }

    public static AnxLiquidacionComprasElectronicaTO llenarObjetoAnxLiquidacionComprasElectronicaTO(String compEmp, String compPerCodigo,
            String compMotCodigo, String compNumero, String claveAcceso, String fechaEmision, String mensajeEnvioComprobanteAutorizado, Emisor emisor) throws Exception {

        String estado;
        String autorizacionNumero;
        String autorizacionFecha;
        File directorio;
        UtilsArchivos util = new UtilsArchivos();
        if (mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO")) {
            String xmlPathFirmadosAutorizados = util.getRutaComprobnateAutorizados() + claveAcceso + ".xml";
            directorio = new File(xmlPathFirmadosAutorizados);
            estado = ComprobantesUtil.obtenerValorXML(directorio, "/*/estado");
            autorizacionNumero = ComprobantesUtil.obtenerValorXML(directorio, "/*/numeroAutorizacion");
            autorizacionFecha = UtilsArchivos.fecha(ComprobantesUtil.obtenerValorXML(directorio, "/*/fechaAutorizacion"), "dd/MM/yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss");
        } else {
            estado = mensajeEnvioComprobanteAutorizado.substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("<") + 1, mensajeEnvioComprobanteAutorizado.lastIndexOf(">")).trim();
            autorizacionFecha = UtilsArchivos.fecha(fechaEmision, "dd-MM-yyyy", "yyyy-MM-dd hh:mm:ss");// "2014-11-19
            autorizacionNumero = mensajeEnvioComprobanteAutorizado
                    .substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("-(") + 2, mensajeEnvioComprobanteAutorizado.lastIndexOf(")-"))
                    .trim();
            directorio = new File("auxxml.xml");
        }
        AnxLiquidacionComprasElectronicaTO anxLiquidacionComprasElectronicaTO = new AnxLiquidacionComprasElectronicaTO();
        anxLiquidacionComprasElectronicaTO.seteSecuencial(0);
        anxLiquidacionComprasElectronicaTO.seteTipoAmbiente(emisor.getTipoAmbiente().equals(TipoAmbienteEnum.PRUEBAS.getCode()) ? TipoAmbienteEnum.PRUEBAS.toString() : TipoAmbienteEnum.PRODUCCION.toString());
        anxLiquidacionComprasElectronicaTO.seteClaveAcceso(claveAcceso);
        anxLiquidacionComprasElectronicaTO.seteEstado(estado);
        anxLiquidacionComprasElectronicaTO.seteAutorizacionNumero(autorizacionNumero);
        anxLiquidacionComprasElectronicaTO.seteAutorizacionFecha(autorizacionFecha);
        anxLiquidacionComprasElectronicaTO.seteXml((mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO") ? ComprobantesUtil.archivoToByte(directorio) : new byte[(int) directorio.length()]));
        anxLiquidacionComprasElectronicaTO.seteEnviadoPorCorreo(false);
        anxLiquidacionComprasElectronicaTO.setCompEmpresa(compEmp);
        anxLiquidacionComprasElectronicaTO.setCompPeriodo(compPerCodigo);
        anxLiquidacionComprasElectronicaTO.setCompMotivo(compMotCodigo);
        anxLiquidacionComprasElectronicaTO.setCompNumero(compNumero);///
        anxLiquidacionComprasElectronicaTO.setUsrEmpresa(compEmp);
        anxLiquidacionComprasElectronicaTO.setUsrFechaInserta(UtilsArchivos.fechaSistema());
        anxLiquidacionComprasElectronicaTO.setCompFecha(fechaEmision.equals("  -  -    ") ? null : UtilsArchivos.fecha(fechaEmision, "dd-MM-yyyy", "yyyy-MM-dd"));
        return anxLiquidacionComprasElectronicaTO;
    }

    public static AnxGuiaRemisionElectronicaTO llenarObjetoAnxGuiaRemisionElectronicaTO(String compEmp, String compPerCodigo,
            String compNumero, String claveAcceso, String fechaEmision, String mensajeEnvioComprobanteAutorizado, Emisor emisor) throws Exception {

        String estado;
        String autorizacionNumero;
        String autorizacionFecha;
        File directorio;
        UtilsArchivos util = new UtilsArchivos();
        if (mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO")) {
            String xmlPathFirmadosAutorizados = util.getRutaComprobnateAutorizados() + claveAcceso + ".xml";
            directorio = new File(xmlPathFirmadosAutorizados);
            estado = ComprobantesUtil.obtenerValorXML(directorio, "/*/estado");
            autorizacionNumero = ComprobantesUtil.obtenerValorXML(directorio, "/*/numeroAutorizacion");
            autorizacionFecha = UtilsArchivos.fecha(ComprobantesUtil.obtenerValorXML(directorio, "/*/fechaAutorizacion"), "dd/MM/yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss");
        } else {
            estado = mensajeEnvioComprobanteAutorizado.substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("<") + 1, mensajeEnvioComprobanteAutorizado.lastIndexOf(">")).trim();
            autorizacionFecha = UtilsArchivos.fecha(fechaEmision, "dd-MM-yyyy", "yyyy-MM-dd hh:mm:ss");// "2014-11-19
            autorizacionNumero = mensajeEnvioComprobanteAutorizado
                    .substring(mensajeEnvioComprobanteAutorizado.lastIndexOf("-(") + 2, mensajeEnvioComprobanteAutorizado.lastIndexOf(")-"))
                    .trim();
            directorio = new File("auxxml.xml");
        }
        AnxGuiaRemisionElectronicaTO anxGuiaRemisionElectronicaTO = new AnxGuiaRemisionElectronicaTO();
        anxGuiaRemisionElectronicaTO.seteSecuencial(0);
        anxGuiaRemisionElectronicaTO.seteTipoAmbiente(emisor.getTipoAmbiente().equals(TipoAmbienteEnum.PRUEBAS.getCode()) ? TipoAmbienteEnum.PRUEBAS.toString() : TipoAmbienteEnum.PRODUCCION.toString());
        anxGuiaRemisionElectronicaTO.seteClaveAcceso(claveAcceso);
        anxGuiaRemisionElectronicaTO.seteEstado(estado);
        anxGuiaRemisionElectronicaTO.seteAutorizacionNumero(autorizacionNumero);
        anxGuiaRemisionElectronicaTO.seteAutorizacionFecha(autorizacionFecha);
        anxGuiaRemisionElectronicaTO.seteXml((mensajeEnvioComprobanteAutorizado.equals("AUTORIZADO") ? ComprobantesUtil.archivoToByte(directorio) : new byte[(int) directorio.length()]));
        anxGuiaRemisionElectronicaTO.seteEnviadoPorCorreo(false);
        anxGuiaRemisionElectronicaTO.setGuiaEmpresa(compEmp);
        anxGuiaRemisionElectronicaTO.setGuiaPeriodo(compPerCodigo);
        anxGuiaRemisionElectronicaTO.setGuiaNumero(compNumero);
        anxGuiaRemisionElectronicaTO.setUsrEmpresa(compEmp);
        anxGuiaRemisionElectronicaTO.setUsrFechaInserta(UtilsArchivos.fechaSistema());
        anxGuiaRemisionElectronicaTO.setGuiaFecha(fechaEmision.equals("  -  -    ") ? null : UtilsArchivos.fecha(fechaEmision, "dd-MM-yyyy", "yyyy-MM-dd"));
        return anxGuiaRemisionElectronicaTO;
    }
}
