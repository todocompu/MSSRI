package com.acosux.SRIMS.util;

import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import com.acosux.SRIMS.util.sri.RespuestaComprobante;
import com.acosux.SRIMS.MisPropiedades;
import com.acosux.SRIMS.entidades.TipoAmbienteEnum;
import com.acosux.SRIMS.service.AutorizacionComprobantes;
import com.acosux.SRIMS.service.AutorizacionComprobantesService;
import com.acosux.SRIMS.service.RecepcionComprobantes;
import com.acosux.SRIMS.service.RecepcionComprobantesService;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class UtilsWebService {

    private static String serviceUrlWS = "";
    private static String rutaKeystore = "";
    private static String passwordKeystore = "";
    public UtilsArchivos urilsArchivos = new UtilsArchivos();
    @Autowired
    MisPropiedades propiedades;

    public void devuelveUrlWs(String tipoAmbiente, String nombreServicio) throws Exception {
        rutaKeystore = urilsArchivos.getRutaCertificadosWebServiceSRI() + "owsKeyStore.jks";
        passwordKeystore = "t0d0c0mPU";
        certificadoSeguridadSRI();
        StringBuilder url = new StringBuilder();
        String direccionIPServicio = tipoAmbiente.equals(TipoAmbienteEnum.PRODUCCION.getCode()) ? "https://cel.sri.gob.ec" : "https://celcer.sri.gob.ec";
        url.append(direccionIPServicio);
        url.append("/comprobantes-electronicos-ws/");
        url.append(nombreServicio);
        url.append("?wsdl");
        serviceUrlWS = url.toString();
    }

    public static RespuestaComprobante autorizacionComprobante(String claveAccesoComprobante) throws Exception {
        certificadoSeguridadSRI();
        AutorizacionComprobantesService service = new AutorizacionComprobantesService(new URL(serviceUrlWS), new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService"));
        AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.request.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.connect.timeout", 9999);
        return port.autorizacionComprobante(claveAccesoComprobante);
    }

    public static RespuestaSolicitud validarComprobante(byte[] xml) throws Exception {
        certificadoSeguridadSRI();
        RecepcionComprobantesService service = new RecepcionComprobantesService(new URL(serviceUrlWS), new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService"));
        RecepcionComprobantes port = service.getRecepcionComprobantesPort();
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.request.timeout", 9999);
        ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.connect.timeout", 9999);
        RespuestaSolicitud respuestaSolicitud = port.validarComprobante(xml);
        return respuestaSolicitud;
    }

    private static void certificadoSeguridadSRI() {
        System.clearProperty("javax.net.ssl.keyStore");
        System.clearProperty("javax.net.ssl.keyStorePassword");
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");

        System.setProperty("javax.net.ssl.keyStore", rutaKeystore);
        System.setProperty("javax.net.ssl.keyStorePassword", passwordKeystore);
        System.setProperty("javax.net.ssl.trustStore", rutaKeystore);
        System.setProperty("javax.net.ssl.trustStorePassword", passwordKeystore);
    }

    public void actualizarCertificadoSeguridadSRI() {
        rutaKeystore = urilsArchivos.getRutaCertificadosWebServiceSRI() + "owsKeyStore.jks";
        passwordKeystore = "t0d0c0mPU";
        System.out.println("rutaKeystore:" + rutaKeystore);
        System.out.println("passwordKeystore: " + passwordKeystore);
        certificadoSeguridadSRI();
    }

}
