package com.acosux.SRIMS.util;

import com.acosux.SRIMS.MisPropiedades;
import com.acosux.SRIMS.service.AutorizacionComprobantes;
import com.acosux.SRIMS.service.AutorizacionComprobantesService;
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
        String direccionIPServicio = tipoAmbiente.equals("2") ? "https://cel.sri.gob.ec" : "https://celcer.sri.gob.ec";
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
        return port.autorizacionComprobante(claveAccesoComprobante);
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
