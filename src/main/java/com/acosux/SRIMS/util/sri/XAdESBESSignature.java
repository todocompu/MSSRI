/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import org.w3c.dom.Document;

/**
 *
 * @author mario
 */
public class XAdESBESSignature extends GenericXMLSignature {

    private static String nameFile;
    private static String pathFile;
    /**
     * Recurso a firmar
     */
    private final String fileToSign;

    public XAdESBESSignature(String fileToSign) {
        super();
        this.fileToSign = fileToSign;
    }

    public static String firmar(String xmlPath, String pathSignature, String passSignature, String pathOut, String nameFileOut) {
        XAdESBESSignature signature = new XAdESBESSignature(xmlPath);
        signature.setPassSignature(passSignature);
        signature.setPathSignature(pathSignature);
        pathFile = pathOut;
        nameFile = nameFileOut;
        return signature.execute();
    }

    @Override
    protected DataToSign createDataToSign() {

        DataToSign datosAFirmar = new DataToSign();
        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "contenido comprobante", null, "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");
        Document docToSign = getDocument(fileToSign);
        datosAFirmar.setDocument(docToSign);

        return datosAFirmar;
    }

    @Override
    protected String getSignatureFileName() {
        return XAdESBESSignature.nameFile;
    }

    @Override
    protected String getPathOut() {
        return XAdESBESSignature.pathFile;
    }
}
