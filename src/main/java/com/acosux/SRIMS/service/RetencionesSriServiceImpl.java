/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.util.ComprobantesUtil;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.AnxComprobanteElectronicoUtilTO;
import com.acosux.SRIMS.util.sri.AutorizacionComprobantesWs;
import com.acosux.SRIMS.util.sri.EnvioComprobantesWs;
import com.acosux.SRIMS.util.sri.FormGenerales;
import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class RetencionesSriServiceImpl implements RetencionesSriService {

    @Autowired
    ComprobantesSRIService comprobantesSRIService;

    @Override
    public String envioComprobanteWebServiceSRI(Emisor emisor, String claveDeAcceso) throws Exception {
        RespuestaSolicitud respuestaRecepcion;
        String mensajeAutorizacion = "";
        UtilsArchivos utils = new UtilsArchivos();
        AnxComprobanteElectronicoUtilTO anxComprobanteAutorizadoUtilTO;
        String respuestaAutoriz;
        String xmlPathFirmados = utils.getRutaComprobnateFirmados() + claveDeAcceso + ".xml";
        File xmlFile = new File(xmlPathFirmados);
        File xmlFileCopia = new File(xmlPathFirmados);
        String claveAccesoComprobante = ComprobantesUtil.obtenerValorXML(xmlFile, "/*/infoTributaria/claveAcceso");
        String tipoComprobante = ComprobantesUtil.obtenerValorXML(xmlFile, "/*/infoTributaria/codDoc").substring(1);

        if (tipoComprobante != null && claveAccesoComprobante != null) {
            byte[] xmlByte = ComprobantesUtil.archivoToByte(xmlFileCopia);
            anxComprobanteAutorizadoUtilTO = comprobantesSRIService.getEnvioComprobanteElectronicosWS(xmlByte, claveDeAcceso, emisor.getTipoAmbiente());
            String auxMensaje = anxComprobanteAutorizadoUtilTO == null ? "FERROR DESCONOCIDO" : anxComprobanteAutorizadoUtilTO.getMensaje();
            if (auxMensaje.charAt(0) == 'T') {
                respuestaRecepcion = anxComprobanteAutorizadoUtilTO != null ? anxComprobanteAutorizadoUtilTO.getRespuestaSolicitud() : null;
                if (respuestaRecepcion != null && respuestaRecepcion.getEstado().equals("RECIBIDA")) {
                    respuestaAutoriz = AutorizacionComprobantesWs.generarXmlComprobantes(anxComprobanteAutorizadoUtilTO.getRespuestaComprobante(), xmlFile.getName());

                    if (respuestaAutoriz != null && respuestaAutoriz.equalsIgnoreCase("AUTORIZADO")) {
                        mensajeAutorizacion = respuestaAutoriz;
                    } else if (respuestaAutoriz != null && respuestaAutoriz.equalsIgnoreCase("EN PROCESO")) {
                        mensajeAutorizacion = respuestaAutoriz; //"<ERROR>" + "El comprobante esta en proceso de espera";
                    } else {
                        System.out.println("respuestaAutoriz --> " + respuestaAutoriz);
                        if (respuestaAutoriz != null) {
                            mensajeAutorizacion = "Enviado, pero sin respuesta del SRI <" + respuestaAutoriz + ">";
                        }
                    }
                } else if (respuestaRecepcion != null && respuestaRecepcion.getEstado().equals("DEVUELTA")) {
                    String resultado = FormGenerales.insertarCaracteres(EnvioComprobantesWs.obtenerMensajeRespuesta(respuestaRecepcion), "\n", 160);
                    if (ComprobantesUtil.anadirMotivosRechazo(xmlFile, respuestaRecepcion)) {
                        if (!ComprobantesUtil.copiarArchivo(xmlFile, utils.getRutaComprobnateRechazados() + xmlFile.getName())) {
                            mensajeAutorizacion = "Error al mover archivo a carpeta rechazados";
                        }
                        mensajeAutorizacion = "<" + respuestaRecepcion.getEstado() + ">"
                                + "Error al tratar de enviar el comprobante hacia el SRI:\n" + resultado
                                + "\n Contacte al administrador";
                    } else {
                        mensajeAutorizacion = "<" + respuestaRecepcion.getEstado() + "> " + resultado.trim();
                    }
                    System.out.println("mensajeAutorizacion: --> " + mensajeAutorizacion);
                } else if (respuestaRecepcion == null || respuestaRecepcion.getComprobantes() == null) {
                    mensajeAutorizacion = "<ERROR>"
                            + "Error al tratar de enviar el comprobante hacia el SRI intentelo mas tarde...\n"
                            + (respuestaRecepcion != null ? respuestaRecepcion.getEstado() : "");
                }
            } else {
                mensajeAutorizacion = auxMensaje.substring(1);
            }
        } else {
            String m = "\nEn: " + xmlFile.getName() + " la información <codDoc> y <claveAcceso> son obligatorias para el envio del archivo";
            String mensaje = "<ERROR>Error al tratar de enviar el comprobante hacia el SRI:\n" + m
                    + "\n Contacte al administrador";
            mensajeAutorizacion = mensaje;
        }
        return mensajeAutorizacion;
    }
    
    private void mostrarArchivoXML(String rutaArchivo){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
           // Apertura del fichero y creacion de BufferedReader para poder
           // hacer una lectura comoda (disponer del metodo readLine()).
           archivo = new File (rutaArchivo);
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);

           // Lectura del fichero
           String linea;
           while((linea=br.readLine())!=null)
              System.out.println(linea);
        } catch(Exception e){
           e.printStackTrace();
        } finally{
           // En el finally cerramos el fichero, para asegurarnos
           // que se cierra tanto si todo va bien como si salta 
           // una excepcion.
           try{                    
              if( null != fr ){   
                 fr.close();     
              }                  
           }catch (Exception e2){ 
              e2.printStackTrace();
           }
        }
    }
}
