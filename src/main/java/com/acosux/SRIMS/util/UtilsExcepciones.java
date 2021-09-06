package com.acosux.SRIMS.util;


import org.postgresql.util.PSQLException;


public class UtilsExcepciones {

    public static Throwable obtenerErrorPostgreSQL(Exception e) {
        Throwable t = e.getCause();
        while ((t != null) && !(t instanceof PSQLException)) {
            t = t.getCause();
        }
        return t instanceof PSQLException ? t : e;
    }

}
