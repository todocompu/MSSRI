/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.dao.CatastroMicroempresaDao;
import com.acosux.SRIMS.dao.CatastroRimpeDao;
import com.acosux.SRIMS.entidades.AnxCatastroMicroempresa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Trabajo
 */
@Service
public class CatastroMicroempresaServiceImpl implements CatastroMicroempresaService {

    @Autowired
    private CatastroMicroempresaDao catastroMicroempresaDao;
    @Autowired
    private CatastroRimpeDao catastroRimpeDao;

    @Override
    public boolean existeCatastroMicroEmpresa(String identificacion) throws Exception {
        return catastroMicroempresaDao.existeCatastroMicroEmpresa(identificacion);
    }

    @Override
    public boolean existeCatastroRimpe(String identificacion) throws Exception {
        return catastroRimpeDao.existeCatastroRimpe(identificacion);
    }

    @Override
    public String insertarListadoCatastroMicroempresa(List<AnxCatastroMicroempresa> listado, boolean permitirBorrar) throws Exception {
        String retorno = "";
        if (catastroMicroempresaDao.insertarListadoCatastroMicroempresa(listado, permitirBorrar)) {
            retorno = "Listado Ingresado Correctamente";
        } else {
            retorno = "Error al guardar los registros, Intente de nuevo";
        }
        return retorno;
    }

}
