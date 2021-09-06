/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.dao;


import com.acosux.SRIMS.entidades.AnxCatastroMicroempresa;
import com.acosux.SRIMS.util.UtilsConversiones;
import com.acosux.SRIMS.util.dao.GenericDaoImpl;
import com.acosux.SRIMS.util.dao.GenericSQLDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Trabajo
 */
@Repository
public class CatastroMicroempresaDaoImpl extends GenericDaoImpl<AnxCatastroMicroempresa, Integer> implements CatastroMicroempresaDao {

    @Autowired
    private GenericSQLDao genericSQLDao;

    @Override
    public boolean existeCatastroMicroEmpresa(String identificacion) throws Exception {
        String sql = "SELECT COUNT(*)!=0 FROM anexo.anx_catastro_microempresa WHERE cat_ruc='" + identificacion + "'";
        return (boolean) UtilsConversiones.convertir(genericSQLDao.obtenerObjetoPorSql(sql));
    }

    @Override
    public boolean insertarListadoCatastroMicroempresa(List<AnxCatastroMicroempresa> listadoCatastroMicroempresa, boolean permitirBorrar) throws Exception {
        if (permitirBorrar) {
            String sql = " DELETE FROM anexo.anx_catastro_microempresa";
            int mensaje = genericSQLDao.ejecutarSQL(sql);
        }

        insertar(listadoCatastroMicroempresa);
        return true;
    }

}
