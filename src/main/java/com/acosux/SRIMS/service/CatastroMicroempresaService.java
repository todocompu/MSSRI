/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.service;

import com.acosux.SRIMS.entidades.AnxCatastroMicroempresa;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Trabajo
 */
public interface CatastroMicroempresaService {

    public boolean existeCatastroMicroEmpresa(String identificacion) throws Exception;

    public boolean existeCatastroRimpe(String identificacion) throws Exception;

    @Transactional
    public String insertarListadoCatastroMicroempresa(List<AnxCatastroMicroempresa> listado, boolean permitirBorrar) throws Exception;

}
