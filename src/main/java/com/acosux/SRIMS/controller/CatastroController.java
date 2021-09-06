/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.controller;

import com.acosux.SRIMS.entidades.AnxCatastroMicroempresa;
import com.acosux.SRIMS.service.CatastroMicroempresaService;
import com.acosux.SRIMS.util.RespuestaWebTO;
import com.acosux.SRIMS.util.UtilsExcepciones;
import com.acosux.SRIMS.util.UtilsJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Carol
 */
@RestController
@RequestMapping("/catastro/")
public class CatastroController {

    @Autowired
    CatastroMicroempresaService catastroMicroempresaService;

    @RequestMapping(value = "/existeCatastroMicroEmpresa/{identificacion}", method = {RequestMethod.GET})
    public RespuestaWebTO existeCatastroMicroEmpresa(@PathVariable("identificacion") String identificacion) {
        RespuestaWebTO resp = new RespuestaWebTO();
        resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
        try {
            boolean respues = catastroMicroempresaService.existeCatastroMicroEmpresa(identificacion);
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } catch (Exception e) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/insertarListadoMicroEmpresa")
    public RespuestaWebTO insertarListadoMicroEmpresa(@RequestBody String json) throws JsonProcessingException {
        RespuestaWebTO resp = new RespuestaWebTO();
        Map<String, Object> map = UtilsJSON.jsonToMap(json);
        List<AnxCatastroMicroempresa> listaEnviar = UtilsJSON.jsonToList(AnxCatastroMicroempresa.class, map.get("listado"));
        boolean permitirBorrar = UtilsJSON.jsonToObjeto(boolean.class, map.get("permitirBorrar"));
        try {
            String respuesta = catastroMicroempresaService.insertarListadoCatastroMicroempresa(listaEnviar, permitirBorrar);
            if (respuesta != null) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respuesta);
            } else {
                resp.setEstadoOperacion("Ocurrio un problema al guardar los regsitros. Contacte con el administrador");
            }
        } catch (Exception e) {
            e = (Exception) UtilsExcepciones.obtenerErrorPostgreSQL(e);
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
}
