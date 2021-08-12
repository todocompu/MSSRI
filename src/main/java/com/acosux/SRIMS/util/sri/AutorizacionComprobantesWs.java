/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.util.ComprobantesUtil;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author mario
 */
public class AutorizacionComprobantesWs {

    static File comprobanteFactura = null;

    // nuevo metodo para Generar XML
    public static String generarXmlComprobantes(RespuestaComprobante respuestaComprobante, String nombreArchivo) throws Exception {
        StringBuilder mensaje = new StringBuilder();
        UtilsArchivos utils = new UtilsArchivos();
        int i;
        if (respuestaComprobante != null) {
            i = 0;
            for (Autorizacion item : respuestaComprobante.getAutorizaciones().getAutorizacion()) {
                mensaje.append(item.getEstado());
                item.setComprobante("<![CDATA[" + item.getComprobante() + "]]>");
                XStream xstream = XStreamUtil.getRespuestaXStream();
                Writer writer = null;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                writer = new OutputStreamWriter(outputStream, "UTF-8");
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

                xstream.toXML(item, writer);
                String xmlAutorizacion = outputStream.toString("UTF-8");
                if ((i == 0) && (item.getEstado().equals("AUTORIZADO"))) {
                    comprobanteFactura = ComprobantesUtil.stringToArchivo(utils.getRutaComprobnateAutorizados() + nombreArchivo, xmlAutorizacion);
                    break;
                }
                if (item.getEstado().equals("NO AUTORIZADO")) {
                    mensaje.append("|").append(obtieneMensajesAutorizacion(item));
                    verificarOCSP(item);
                    break;
                }
                i++;
            }
        }

        return mensaje.toString();
    }

    public static String obtieneMensajesAutorizacion(Autorizacion autorizacion) {
        StringBuilder mensaje = new StringBuilder();
        int cont = 0;
        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
            if (m.getInformacionAdicional() != null) {
                mensaje.append("-(CE(").append(m.getIdentificador()).append("): ").append(m.getMensaje()).append(")-");
                mensaje.append("\n").append(m.getInformacionAdicional());
                cont++;
            } else {
                if (cont == 0) {
                    mensaje.append("\n-(CE(").append(m.getIdentificador()).append("): ").append(m.getMensaje()).append(")-");
                } else {
                    mensaje.append("\n").append(m.getMensaje());
                }
                cont++;
            }
        }

        return mensaje.toString();
    }

    public static boolean verificarOCSP(Autorizacion autorizacion) throws SQLException, ClassNotFoundException {
        boolean respuesta = true;

        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
            if (m.getIdentificador().equals("61")) {
                int i = JOptionPane.showConfirmDialog(null,
                        "No se puede validar el certificado digital.\n Desea emitir en contingencia?", "Advertencia",
                        0);
                if (i == 0) {
                }
                respuesta = false;
            }
        }
        return respuesta;
    }
}
