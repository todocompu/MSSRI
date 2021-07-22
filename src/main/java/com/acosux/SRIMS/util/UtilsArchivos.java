package com.acosux.SRIMS.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UtilsArchivos {

    private final Properties p = System.getProperties();
    public final String sep = p.getProperty("file.separator");
    private final String rutaRaiz = (p.getProperty("os.name").compareToIgnoreCase("linux") == 0 ? "/opt" : p.getProperty("user.home")) + sep + "shrimp" + sep;

    public UtilsArchivos() {
    }

    // devuelve la ruta y si no existe la crea
    public String crearRuta(String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        return ruta;
    }

    // devuelve true si elimina el archivo
    public static boolean eliminarArchivo(String archivo) {
        File directorio = new File(archivo);
        return directorio.delete();
    }

    // devuelve una lista de archivos que contiene
    public static List<String> getlistArchivos(String directorio) {
        File f = new File(directorio);
        List<String> list = new ArrayList<>();
        for (String archivo : f.list()) {
            if (archivo.endsWith(".xml")) {
                list.add(archivo);
            }
        }
        return list;
    }

    public String getRutaRaiz() {
        return crearRuta(rutaRaiz);
    }

    public String getRutaReportes() {
        return crearRuta(getRutaRaiz() + "reportes" + sep);
    }

    public String getRutaReportesRide(String directorio) {
        return crearRuta(getRutaReportes() + "ride" + sep + directorio + sep);
    }

    public String getRutaDocumentosElectronicos() {
        return crearRuta(getRutaRaiz() + "documentosElectronicos" + sep);
    }

    public String getRutaCertificadosWebServiceSRI() {
        return crearRuta(getRutaDocumentosElectronicos() + "certificados" + sep);
    }

    public String getRutaComprobnate() {
        return crearRuta(getRutaDocumentosElectronicos() + "comprobante" + sep);
    }

    public String getRutaComprobnateGenerados() {
        return crearRuta(getRutaComprobnate() + "generados" + sep);
    }

    public String getRutaComprobnateFirmados() {
        return crearRuta(getRutaComprobnate() + "firmados" + sep);
    }

    public String getRutaComprobnateAutorizados() {
        return crearRuta(getRutaComprobnate() + "autorizados" + sep);
    }

    public String getRutaComprobnateRechazados() {
        return crearRuta(getRutaComprobnate() + "rechazados" + sep);
    }

    public String getRutaFirmaDigital() {
        return crearRuta(getRutaDocumentosElectronicos() + "firmaDigital" + sep);
    }
    
     public static String fecha(Date fecha, String mascara) {
        try {
            DateFormat formato = new SimpleDateFormat(mascara);
            return formato.format(fecha);
        } catch (Exception ex) {
            return null;
        }
    }

}
