package com.acosux.SRIMS.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

public class UtilsArchivos {

    private final Properties p = System.getProperties();
    public final String sep = p.getProperty("file.separator");
    private static final Locale locale = new Locale("es", "EC");
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
    
    public static Date fecha(String fecha, String mascara) {
        try {
            return (Date) new SimpleDateFormat(mascara).parse(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    public static String fecha(String fecha, String mascara1, String mascara2) {
        String fechaDevolver = "";
        SimpleDateFormat formatoFecha1 = new SimpleDateFormat(mascara1);
        SimpleDateFormat formatoFecha2 = new SimpleDateFormat(mascara2);
        try {
            formatoFecha1.parse(fecha);
            fechaDevolver = formatoFecha2.format(formatoFecha1.getCalendar().getTime());
        } catch (ParseException ex) {
            System.out.println("ERRORROR: " + ex.getMessage());
        }
        return fechaDevolver;
    }
    
    public static String fechaSistema() {
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        return formato.format(fecha);
    }
    
    public static BigDecimal redondeoDecimalBigDecimal(BigDecimal d, int precision, RoundingMode metodoRedondeo) {
        return d.setScale(precision, metodoRedondeo);
    }
    
    public static BigDecimal redondeoDecimalBigDecimal(BigDecimal d) {
        DecimalFormat formato = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = formato.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        formato.setDecimalFormatSymbols(dfs);
        return new BigDecimal(formato.format(d));
    }
    
     public static List<String> separar(String listaAux, String separador) {
        List<String> lista = new ArrayList<>();
        int indice = 0;
        int token = new StringTokenizer(listaAux, separador).countTokens();
        for (int i = 0; i < token; i++) {
            if (i == 0) {
                lista.add(listaAux.substring(indice, listaAux.indexOf(separador, indice + 1)).trim());
            } else if (i == token - 1) {
                lista.add(listaAux.substring(indice + 1, listaAux.length()).trim());
            } else {
                lista.add(listaAux.substring(indice + 1, listaAux.indexOf(separador, indice + 1)).trim());
            }
            indice = listaAux.indexOf(separador, indice + 1);
        }
        return lista;
    }

}
