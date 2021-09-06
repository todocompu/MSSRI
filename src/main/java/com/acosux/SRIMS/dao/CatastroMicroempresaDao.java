/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.dao;

import com.acosux.SRIMS.entidades.AnxCatastroMicroempresa;
import com.acosux.SRIMS.util.dao.GenericDao;
import java.util.List;

public interface CatastroMicroempresaDao extends GenericDao<AnxCatastroMicroempresa, Integer> {

    public boolean existeCatastroMicroEmpresa(String codigo) throws Exception;

    public boolean insertarListadoCatastroMicroempresa(List<AnxCatastroMicroempresa> listadoCatastroMicroempresa, boolean permitirBorrar) throws Exception;

}
