package com.acosux.SRIMS.util.sri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

//import com.sun.xml.internal.txw2.Document;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;

public abstract class GenericXMLSignature {

    // Path de la firma electronica
    private String pathSignature;
    // calve de la firma electronica
    private String passSignature;

    /**
     * se col * Ejecución del ejemplo.La ejecución consistirá en la firma de los
     * datos creados por el método abstracto createDataToSign mediante el
     * certificado declarado en la constante PKCS12_FILE. El resultado del
     * proceso de firma será almacenado en un fichero XML en el directorio
     * correspondiente a la constante OUTPUT_DIRECTORY del usuario bajo el
     * nombre devuelto por el método abstracto getSignFileName
     *
     *
     * @return
     */
    /*
	 * Metodos Getters y Setters (Propiedades)
     */
    public String getPathSignature() {
        return pathSignature;
    }

    public void setPathSignature(String pathSignature) {
        this.pathSignature = pathSignature;
    }

    public String getPassSignature() {
        return passSignature;
    }

    public void setPassSignature(String passSignature) {
        this.passSignature = passSignature;
    }

    protected String execute() {
        String mensaje;
        // Obtencion del gestor de claves
        KeyStore keyStore = getKeyStore();
        if (keyStore == null) {
            mensaje = "FNo se pudo obtener almacen de firma.";
            return mensaje;
        }
        String alias = getAlias(keyStore);

        // Obtencion del certificado para firmar. Utilizaremos el primer certificado del almacen.
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) keyStore.getCertificate(alias);
            if (certificate == null) {
                mensaje = "FNo existe ningún certificado para firmar.";
                return mensaje;
            }
        } catch (KeyStoreException e1) {
        }

        // Obtención de la clave privada asociada al certificado
        PrivateKey privateKey = null;
        KeyStore tmpKs = keyStore;
        try {
            privateKey = (PrivateKey) tmpKs.getKey(alias, this.passSignature.toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            mensaje = "FNo existe clave privada para firmar.";
        }

        // Obtención del provider encargado de las labores criptográficas
        Provider provider = keyStore.getProvider();

        /*
		 * Creación del objeto que contiene tanto los datos a firmar como la
		 * configuración del tipo de firma
         */
        DataToSign dataToSign = createDataToSign();

        /*
		 * Creación del objeto encargado de realizar la firma
         */
        FirmaXML firma = new FirmaXML();

        // Firmamos el documento
        Document docSigned;
        try {
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        } catch (Exception ex) {
            mensaje = "FError realizando la firma, Posibles Causa con el archivo .P12:\n - Password incorrecto \n - Ruc incorrecto";
            return mensaje;
        }

        // Guardamos la firma a un fichero en el home del usuario
        String filePath = getPathOut() + File.separatorChar + getSignatureFileName();
        mensaje = "TFirma salvada en en: " + filePath;
        saveDocumenteDisk(docSigned, filePath);
        return mensaje;
    }

    /**
     *
     * Crea el objeto DataToSign que contiene toda la información de la firma
     * que se desea realizar. Todas las implementaciones deberán proporcionar
     * una implementación de este método
     *
     *
     *
     * @return El objeto DataToSign que contiene toda la información de la firma
     * a realizar
     */
    protected abstract DataToSign createDataToSign();

    protected abstract String getSignatureFileName();

    protected abstract String getPathOut();

    protected Document getDocument(String resource) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        File file = new File(resource);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException ex) {
            System.err.println("Error al parsear el documento");
        }
        return doc;
    }

    private KeyStore getKeyStore() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(pathSignature), passSignature.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
        }
        return ks;
    }

    private static String getAlias(KeyStore keyStore) {
        String alias = null;
        Enumeration nombres;
        try {
            nombres = keyStore.aliases();

            while (nombres.hasMoreElements()) {
                String tmpAlias = (String) nombres.nextElement();
                if (keyStore.isKeyEntry(tmpAlias)) {
                    alias = tmpAlias;
                }
            }
        } catch (KeyStoreException e) {
        }
        return alias;
    }

    public static void saveDocumenteDisk(Document document, String pathXml) {
        try {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(pathXml));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
}
