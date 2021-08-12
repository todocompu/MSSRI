/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mario
 */
public class LectorXPath {

    private final String xmlFile;
    private Document xmlDocument;
    private XPath xPath;

    public LectorXPath(String xmlFile) {
        this.xmlFile = xmlFile;
        inicializar();
    }

    private void inicializar() {
        try {
            this.xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.xmlFile);
            this.xPath = XPathFactory.newInstance().newXPath();
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            Logger.getLogger(LectorXPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object leerArchivo(String expression, QName returnType) {
        try {
            XPathExpression xPathExpression = this.xPath.compile(expression);
            return xPathExpression.evaluate(this.xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(LectorXPath.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
