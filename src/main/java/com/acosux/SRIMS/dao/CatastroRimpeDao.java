/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.dao;

import com.acosux.SRIMS.entidades.AnxCatastroRimpe;
import com.acosux.SRIMS.util.dao.GenericDao;

public interface CatastroRimpeDao extends GenericDao<AnxCatastroRimpe, Integer> {

    public boolean existeCatastroRimpe(String codigo) throws Exception;

}
