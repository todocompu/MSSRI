/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.dao;

import com.acosux.SRIMS.entidades.AnxCatastroRimpe;
import com.acosux.SRIMS.util.dao.GenericDaoImpl;
import com.acosux.SRIMS.util.dao.GenericSQLDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Trabajo
 */
@Repository
public class CatastroRimpeDaoImpl extends GenericDaoImpl<AnxCatastroRimpe, Integer> implements CatastroRimpeDao {

    @Autowired
    private GenericSQLDao genericSQLDao;

    @Override
    public AnxCatastroRimpe existeCatastroRimpe(String identificacion) throws Exception {
        String sql = "SELECT * FROM anexo.anx_catastro_rimpe WHERE catr_ruc='" + identificacion + "' LIMIT 1";
        return (AnxCatastroRimpe) genericSQLDao.obtenerObjetoPorSql(sql, AnxCatastroRimpe.class);
    }

}
