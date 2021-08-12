/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.util.sri.modelo.ClaveContingencia;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author mario
 */
public class FormGenerales {

    public static Date eliminaHora(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static String insertarCaracteres(String cadenaLarga, String aInsertar, int longitud) {
        StringBuilder sb = new StringBuilder(cadenaLarga);

        int i = 0;
        while ((i = sb.indexOf(" ", i + longitud)) != -1) {
            sb.replace(i, i + 1, aInsertar);
        }

        return sb.toString();
    }

    public ClaveContingencia obtieneClaveDeAcceso(String secuencialComprobante, Emisor emisor, String claveDeAcceso, Date fechaEmision, String tipoComprobante) {
        ClaveContingencia clave = new ClaveContingencia();
        if (emisor != null) {
            String serie = emisor.getCodigoEstablecimiento().concat(emisor.getCodPuntoEmision());
            if (emisor.getTipoEmision().equals("1")) {
                claveDeAcceso = new ClaveDeAcceso().generaClave(fechaEmision, tipoComprobante, emisor.getRuc(), emisor.getTipoAmbiente(), serie, secuencialComprobante, emisor.getClaveInterna(), "1");
                clave.setCodigoComprobante(claveDeAcceso);
            } else if ((emisor.getTipoEmision().equals("2")) || (emisor.getTipoEmision().equals("3"))) {
            }
        }
        return clave;
    }

    public static String ingresaPassword() {
        String password = null;

        JPasswordField pwd = new JPasswordField(20);
        int action = JOptionPane.showConfirmDialog(null, pwd, "Ingrese la Contraseña del token", 2);
        if (action < 0) {
            JOptionPane.showMessageDialog(new JFrame(), "Usted canceló la operación");
        } else {
            password = new String(pwd.getPassword());
        }

        return password;
    }
}
