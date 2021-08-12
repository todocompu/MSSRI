/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util;

import com.thoughtworks.xstream.XStream;
import com.acosux.SRIMS.util.sri.Autorizacion;
import com.acosux.SRIMS.util.sri.DocumentoReporteRIDE;
import com.acosux.SRIMS.util.sri.Java2XML;
import com.acosux.SRIMS.util.sri.LectorXPath;
import com.acosux.SRIMS.util.sri.RespuestaSolicitud;
import com.acosux.SRIMS.util.sri.XAdESBESSignature;
import com.acosux.SRIMS.util.sri.XStreamUtil;
import com.acosux.SRIMS.util.sri.modelo.factura.Factura;
import com.acosux.SRIMS.util.sri.modelo.guiaremision.GuiaRemision;
import com.acosux.SRIMS.util.sri.modelo.liquidacioncompra.LiquidacionCompra;
import com.acosux.SRIMS.util.sri.modelo.notadebito.NotaDebito;
import com.acosux.SRIMS.util.sri.modelo.notadecredito.NotaCredito;
import com.acosux.SRIMS.util.sri.modelo.retencion.ComprobanteRetencion;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

/**
 *
 * @author mario
 */
public class ComprobantesUtil {

    public static Object unmarshal(Class<?> clase, Node contenidoXml) throws Exception {
        return JAXBContext.newInstance(clase).createUnmarshaller()
                .unmarshal(new InputSource(new StringReader(contenidoXml.getTextContent())));
    }

    public static String obtenerDocumentoModificado(String codDoc) {
        if ("01".equals(codDoc)) {
            return "FACTURA";
        }
        if ("03".equals(codDoc)) {
            return "LIQUIDACIÓN DE COMPRA";
        }
        if ("04".equals(codDoc)) {
            return "NOTA DE CRÉDITO";
        }
        if ("05".equals(codDoc)) {
            return "NOTA DE DÉBITO";
        }
        if ("06".equals(codDoc)) {
            return "GUÍA REMISIÓN";
        }
        if ("07".equals(codDoc)) {
            return "COMPROBANTE DE RETENCIÓN";
        }
        return null;
    }

    public static DocumentoReporteRIDE documentoRIDE(Document doc) {
        NodeList list = doc.getElementsByTagName("*");
        String documento = "";
        String numeroAutorizacion = "";
        String fechaAutorizacion = "";
        String estado = "";
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            switch (element.getNodeName()) {
                case "comprobante":
                    documento = element.getChildNodes().item(0).getNodeValue();
                    break;
                case "estado":
                    estado = element.getChildNodes().item(0).getNodeValue();
                    break;
                case "numeroAutorizacion":
                    numeroAutorizacion = element.getChildNodes().item(0).getNodeValue();
                    break;
                case "fechaAutorizacion":
                    fechaAutorizacion = element.getChildNodes().item(0).getNodeValue();
                    break;
                case "claveAcceso":
                    break;
                default:
                    break;
            }
        }
        return new DocumentoReporteRIDE(numeroAutorizacion, fechaAutorizacion, estado, documento);
    }

    public static void crearXml(String nombreArchivo, String contenido) {
        String ruta = System.setProperty("java.io.tmpdir", "/comprobantes") + "/" + nombreArchivo.trim() + ".xml";
        File xmlFile = new File(ruta);
        BufferedWriter contenidoString;
        try {
            contenidoString = new BufferedWriter(new FileWriter(xmlFile));
            contenidoString.write(contenido);
            contenidoString.close();
        } catch (IOException e) {
        }
    }

    public static String eliminaCaracteres(String s_cadena, String s_caracteres) {
        String nueva_cadena = "";
        Character caracter = null;
        boolean valido = true;
        /*
		 * Va recorriendo la cadena s_cadena y copia a la cadena que va a
		 * regresar, sólo los caracteres que no estén en la cadena s_caracteres
         */
        for (int i = 0; i < s_cadena.length(); i++) {
            valido = true;
            for (int j = 0; j < s_caracteres.length(); j++) {
                caracter = s_caracteres.charAt(j);
                if (s_cadena.charAt(i) == caracter) {
                    valido = false;
                    break;
                }
            }
            if (valido) {
                nueva_cadena += s_cadena.charAt(i);
            }
        }
        return nueva_cadena;
    }

    public static String obtieneClaveAccesoAutorizacion(Autorizacion item) {
        String claveAcceso = null;

        String xmlAutorizacion = XStreamUtil.getRespuestaLoteXStream().toXML(item);
        File archivoTemporal = new File("temp.xml");

        stringToArchivo(archivoTemporal.getPath(), xmlAutorizacion);
        String contenidoXML = decodeArchivoBase64(archivoTemporal.getPath());

        if (contenidoXML != null) {
            stringToArchivo(archivoTemporal.getPath(), contenidoXML);
            claveAcceso = obtenerValorXML(archivoTemporal, "/*/infoTributaria/claveAcceso");
        }

        return claveAcceso;
    }

    public static String decodeArchivoBase64(String pathArchivo) {
        String xmlDecodificado = null;
        try {
            File file = new File(pathArchivo);
            if (file.exists()) {
                String encd = obtenerValorXML(file, "/*/comprobante");
                xmlDecodificado = encd;
            } else {
            }
        } catch (Exception e) {
        }
        return xmlDecodificado;
    }

    public static File stringToArchivo(String rutaArchivo, String contenidoArchivo) {
        FileOutputStream fos = null;
        File archivoCreado = null;
        int i;
        try {
            fos = new FileOutputStream(rutaArchivo);
            try (OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8")) {
                for (i = 0; i < contenidoArchivo.length(); i++) {
                    out.write(contenidoArchivo.charAt(i));
                }
            }
            archivoCreado = File.createTempFile(rutaArchivo, ".xml");
        } catch (IOException ex) {
            Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            i = 0;
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return archivoCreado;
    }

    public static File stringToArchivoEmail(String rutaArchivo, String contenidoArchivo) {
        FileOutputStream fos = null;
        File archivoCreado = null;
        int i;
        try {
            archivoCreado = File.createTempFile(rutaArchivo, ".xml");
            fos = new FileOutputStream(archivoCreado.getPath());
            try (OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8")) {
                for (i = 0; i < contenidoArchivo.length(); i++) {
                    out.write(contenidoArchivo.charAt(i));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            i = 0;
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return archivoCreado;
    }

    public static String obtenerValorXML(File xmlDocument, String expression) {
        String valor = null;
        try {
            LectorXPath reader = new LectorXPath(xmlDocument.getPath());
            valor = (String) reader.leerArchivo(expression, XPathConstants.STRING);
        } catch (Exception e) {
            Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, e);
        }

        return valor;
    }

    public static byte[] archivoToByte(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return buffer;
    }

    public static String crearArchivoXml2(String pathArchivo, Object objetoModelo) {
        String respuestaCreacion = null;
        if (objetoModelo != null) {
            try {
                respuestaCreacion = realizaMarshal(objetoModelo, pathArchivo);
            } catch (Exception ex) {
                Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            respuestaCreacion = "Ingrese los campos obligatorios del comprobante";
        }
        return respuestaCreacion;
    }

    public static String realizaMarshal(Object comprobante, String pathArchivo) {
        String respuesta = null;
        Class c = null;
        if (comprobante instanceof Factura) {
            c = Factura.class;
        } else if (comprobante instanceof NotaDebito) {
            c = NotaDebito.class;
        } else if (comprobante instanceof NotaCredito) {
            c = NotaCredito.class;
        } else if (comprobante instanceof ComprobanteRetencion) {
            c = ComprobanteRetencion.class;
        } else if (comprobante instanceof LiquidacionCompra) {
            c = LiquidacionCompra.class;
        } else if (comprobante instanceof GuiaRemision) {
            c = GuiaRemision.class;
        }
        Java2XML.objetoTOxml(c, pathArchivo, comprobante);
        return respuesta;
    }

    public static boolean copiarArchivo(File archivoOrigen, String pathDestino) {
        FileReader in = null;
        boolean resultado = false;
        try {
            File outputFile = new File(pathDestino);
            in = new FileReader(archivoOrigen);
            try (FileWriter out = new FileWriter(outputFile)) {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                in.close();
            }
            resultado = true;
        } catch (IOException ex) {
            Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ComprobantesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultado;
    }

    private static Document merge(String exp, File[] files) throws Exception {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expression = xpath.compile(exp);

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document base = docBuilder.parse(files[0]);

        Node results = (Node) expression.evaluate(base, XPathConstants.NODE);
        if (results == null) {
            throw new IOException(files[0] + ": expression does not evaluate to node");
        }

        for (int i = 1; i < files.length; i++) {
            try {
                Document merge = docBuilder.parse(files[i]);
                Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
                results.appendChild(base.importNode(nextResults, true));
            } catch (IOException | XPathExpressionException | DOMException | SAXException e) {
                System.out.println(e.getMessage());
            }
        }
        return base;
    }

    public static boolean adjuntarArchivo(File respuesta, File comprobante) throws Exception {
        boolean exito = false;
        Document document = merge("*", new File[]{comprobante, respuesta});
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new OutputStreamWriter(new FileOutputStream(comprobante), "UTF-8"));
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.transform(source, result);
        return exito;
    }

    public static String firmarArchivo(String xmlPath, byte[] pathSignature, String passSignature, String pathOut,
            String nameFileOut) throws Exception {
        String respuestaFirma = null;
        if (xmlPath != null && pathSignature != null && passSignature != null && pathOut != null
                && nameFileOut != null) {
            File temp = File.createTempFile("temp", ".p12");
            FileUtils.writeByteArrayToFile(temp, pathSignature);
            respuestaFirma = XAdESBESSignature.firmar(xmlPath, temp.getAbsolutePath(), passSignature, pathOut, nameFileOut);

            File directorioComprobante = new File(xmlPath);
            if (directorioComprobante.exists()) {
                directorioComprobante.delete();
            }
            FileUtils.deleteQuietly(temp);
        }
        return respuestaFirma;
    }

    public static boolean anadirMotivosRechazo(File archivo, RespuestaSolicitud respuestaRecepcion) throws Exception {
        boolean exito = false;
        UtilsArchivos u = new UtilsArchivos();
        File respuesta = new File(u.getRutaComprobnate() + "respuesta.xml");
        Java2XML.objetoTOxml(RespuestaSolicitud.class, respuesta.getPath(), respuestaRecepcion);
        if (adjuntarArchivo(respuesta, archivo) == true) {
            exito = true;
            respuesta.delete();
        }
        return exito;
    }

    public static byte[] convertirXMLAutorizacionABytes(Autorizacion autorizacion) throws IOException {
        XStream xstream = XStreamUtil.getRespuestaXStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xstream.toXML(autorizacion, writer);
        String xmlAutorizacion = outputStream.toString("UTF-8");
        return xmlAutorizacion.getBytes("UTF-8");
    }
}
