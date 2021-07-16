/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author mario
 */
@Service
public class SriServiceImpl implements SriService {

    private final String conexionSri = "https://srienlinea.sri.gob.ec/facturacion-internet";
    private final String conexionWSVerificar = "http://certificados.ministeriodegobierno.gob.ec/gestorcertificados/antecedentes/data.php";

    public String consultaRucSri(String ruc) {
        String url = conexionSri + "/consultas/publico/ruc-datos2.jspa?ruc=" + ruc;
        String body = new RestTemplate().getForObject(url, String.class);
        return body;
    }

    public boolean existePorNumeroRucSRI(String ruc) {
        String urlPrincipal = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=";
        String url = urlPrincipal + ruc;
        boolean body = new RestTemplate().getForObject(url, boolean.class);
        return body;
    }

    public String obtenerAntecedentePorCedula(String ruc) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tipo", "getDataWs");
        body.add("ci", ruc);
        body.add("tp", "C");
        body.add("ise", "SI");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.postForEntity(conexionWSVerificar, requestEntity, String.class);
        String respuesta = resp.getBody();
        return respuesta;
    }

    @Override
    public Map<String, Object> consultaDatosRucCedula(String numero, char tipo) {
        Map<String, Object> respuesta = new HashMap<>();
        String identificacion = "";
        String datosRuc = "";
        String datosAntecedentesCedula = "";

        switch (tipo) {
            case 'C':
                identificacion = numero + "001";//convertimos a ruc
                if (existePorNumeroRucSRI(identificacion)) {
                    //consultamos datos de RUC
                    datosRuc = consultaRucSri(identificacion);
                } else {
                    datosAntecedentesCedula = obtenerAntecedentePorCedula(numero);
                }
                break;
            case 'R':
                if (existePorNumeroRucSRI(numero)) {
                    //consultamos datos de RUC
                    datosRuc = consultaRucSri(numero);
                }
                break;
        }

        respuesta.put("tipo", tipo);
        respuesta.put("identificacion", numero);
        respuesta.put("datosRuc", datosRuc);
        respuesta.put("datosAntecedentesCedula", datosAntecedentesCedula);

        return respuesta;
    }

}
