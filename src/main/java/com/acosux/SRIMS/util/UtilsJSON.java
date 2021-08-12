/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mario
 */
public class UtilsJSON {

    private static final ObjectMapper om = new ObjectMapper();

    public static String objetoToJson(Object obj) throws JsonProcessingException {
        return om.writeValueAsString(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(Class<T> clase, Object json) throws JsonProcessingException {
        return (List<T>) om.readValue(objetoToJson(json), TypeFactory.defaultInstance().constructCollectionType(List.class, clase));
    }

    public static Map<String, Object> jsonToMap(String json) throws JsonProcessingException {
        return om.readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> T jsonToObjeto(Class<T> clase, String json) throws JsonProcessingException {
        return (T) om.readValue(json, clase);
    }

    public static <T> T jsonToObjeto(Class<T> clase, Object json) throws JsonProcessingException {
        String objetcToJason = objetoToJson(json);
        return (T) om.readValue(objetcToJason, clase);
    }
}
