/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.AnxCompraDetalleTO;
import com.acosux.SRIMS.entidades.AnxCompraTO;
import com.acosux.SRIMS.entidades.InvComprasTO;
import com.acosux.SRIMS.entidades.InvProveedor;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.ComprobantesUtil;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.retencion.ComprobanteRetencion;
import com.acosux.SRIMS.util.sri.modelo.retencion.ImpuestoRetencion;
import com.acosux.SRIMS.util.sri.modelo.retencion.ObjectFactoryRetencion;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author mario
 */
public class GenerarXMLRetencion {

    private InfoTributaria infoTributaria = null;
    private ObjectFactoryRetencion factoryRetencion = null;
    private ComprobanteRetencion.InfoCompRetencion infoCompRetencion = null;
    private Emisor emisor = null;
    private String claveDeAcceso;
    private AnxCompraTO anxCompraTO = null;
    private InvComprasTO invComprasTO = null;
    private InvProveedor invProveedor;
    private List<AnxCompraDetalleTO> listAnxCompraDetalleTO = null;
    private SisEmpresaParametros sisEmpresaParametros;

    public ComprobanteRetencion generarComprobanteDeRetencion(InvComprasTO invComprasTO, AnxCompraTO anxCompraTO,
            List<AnxCompraDetalleTO> listAnxCompraDetalleTO, InvProveedor invProveedor, String claveDeAcceso,
            Emisor emisor, String agenteRetencion, SisEmpresaParametros sisEmpresaParametros) {
        this.factoryRetencion = new ObjectFactoryRetencion();
        ComprobanteRetencion compRetencion = null;
        this.emisor = emisor;
        this.anxCompraTO = anxCompraTO;
        this.invComprasTO = invComprasTO;
        this.listAnxCompraDetalleTO = listAnxCompraDetalleTO;
        this.invProveedor = invProveedor;
        this.claveDeAcceso = claveDeAcceso;
        this.sisEmpresaParametros = sisEmpresaParametros;
        if (!llenarObjetoComprobante(agenteRetencion)) {
            ComprobanteRetencion.Impuestos impuestos = obtenerImpuestosRetencio();
            ComprobanteRetencion.InfoAdicional informacion = generarInformacionAdicionalComprobanteRetencion();
            if (impuestos != null) {
                compRetencion = this.factoryRetencion.createComprobanteRetencion();
                compRetencion.setVersion("1.0.0");
                compRetencion.setId("comprobante");
                compRetencion.setInfoTributaria(this.infoTributaria);
                compRetencion.setInfoCompRetencion(this.infoCompRetencion);
                compRetencion.setImpuestos(impuestos);
                if (informacion.getCampoAdicional().size() > 0) {
                    compRetencion.setInfoAdicional(informacion);
                }
            }
        }
        return compRetencion;
    }

    private boolean llenarObjetoComprobante(String agenteRetencion) {
        boolean error = false;
        this.infoTributaria = this.factoryRetencion.createInfoTributaria();
        this.infoTributaria.setAmbiente(this.emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(this.emisor.getRazonSocial());// this.emisor.getNombreComercial()
        this.infoTributaria.setNombreComercial(this.emisor.getNombreComercial());

        this.infoTributaria.setRuc(emisor.getRuc());
        if (this.claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(this.claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCode());
        this.infoTributaria.setEstab(anxCompraTO.getCompRetencionNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(anxCompraTO.getCompRetencionNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(anxCompraTO.getCompRetencionNumero().substring(8));
        this.infoTributaria.setDirMatriz(emisor.getDireccionMatriz());
        if (agenteRetencion != null && !agenteRetencion.equals("")) {
            this.infoTributaria.setAgenteRetencion(agenteRetencion);
        }

        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
            this.infoTributaria.setContribuyenteRimpe("CONTRIBUYENTE RÉGIMEN RIMPE");
        }

        this.infoCompRetencion = this.factoryRetencion.createComprobanteRetencionInfoCompRetencion();
        this.infoCompRetencion.setFechaEmision(UtilsArchivos.fecha(anxCompraTO.getCompRetencionFechaEmision(), "yyyy-MM-dd", "dd/MM/yyyy"));
        if (this.emisor.getDirEstablecimiento() != null && !this.emisor.getDirEstablecimiento().equals("")) {
            this.infoCompRetencion.setDirEstablecimiento(this.emisor.getDirEstablecimiento());
        }
        if (this.emisor.getContribuyenteEspecial() != null && !this.emisor.getContribuyenteEspecial().equals("")) {
            this.infoCompRetencion.setContribuyenteEspecial(this.emisor.getContribuyenteEspecial());// sisUsuarioEmpresaTO.getEmpParResolucionContribuyenteEspecia()
        }
        this.infoCompRetencion.setObligadoContabilidad(this.emisor.getLlevaContabilidad());

        String tipoProveedor = this.invProveedor.getProvIdTipo();
        switch (tipoProveedor) {
            case "R":
                this.infoCompRetencion.setTipoIdentificacionSujetoRetenido("04");
                break;
            case "C":
                this.infoCompRetencion.setTipoIdentificacionSujetoRetenido("05");
                break;
            case "P":
                this.infoCompRetencion.setTipoIdentificacionSujetoRetenido("06");
                break;
        }
        this.infoCompRetencion.setRazonSocialSujetoRetenido(this.invProveedor.getProvRazonSocial());
        this.infoCompRetencion.setIdentificacionSujetoRetenido(this.invProveedor.getProvIdNumero());
        this.infoCompRetencion.setPeriodoFiscal(UtilsArchivos.fecha(anxCompraTO.getCompRetencionFechaEmision(), "yyyy-MM-dd", "dd/MM/yyyy").substring(3, 10));
        return error;
    }

    /*
	 * ///////////////////////////impuesto.setCodigo////////////////////////////
	 * /// IMPUESTO A RETENER CODIGO RENTA 1 IVA 2 ISD 6
	 * //////////////////////////impuesto.setCodigoRetencion////////////////////
	 * /// ----Retención IVA--- PORCENTAJE IVA CÓDIGO 0% 7 20% 10 30% 1 70% 2
	 * 100% 3 ----Retención ISD--- PORCENTAJE ISD CODIGO 5% 4580
     */
    // OJO HACER DE MANERA DINAMICA
    ComprobanteRetencion.Impuestos obtenerImpuestosRetencio() {
        ComprobanteRetencion.Impuestos resultado = this.factoryRetencion.createComprobanteRetencionImpuestos();
        ImpuestoRetencion impuesto;
        if (anxCompraTO.getCompBaseivabienes().compareTo(BigDecimal.ZERO) > 0) {
            String codigoRetencion;
            if (anxCompraTO.getCompPorcentajebienes().compareTo(new BigDecimal(10)) == 0) {
                codigoRetencion = "9";
            } else if (anxCompraTO.getCompPorcentajebienes().compareTo(new BigDecimal(30)) == 0) {
                codigoRetencion = "1";
            } else if (anxCompraTO.getCompPorcentajebienes().compareTo(new BigDecimal(100)) == 0) {
                codigoRetencion = "3";
            } else {
                codigoRetencion = "7";
            }
            impuesto = this.factoryRetencion.createImpuesto();
            impuesto.setCodigo("2");// impuesto Renta
            impuesto.setCodigoRetencion(codigoRetencion);
            impuesto.setPorcentajeRetener(anxCompraTO.getCompPorcentajebienes());
            impuesto.setBaseImponible(anxCompraTO.getCompBaseivabienes());
            impuesto.setValorRetenido(anxCompraTO.getCompValorbienes());
            impuesto.setCodDocSustento(invComprasTO.getCompDocumentoTipo());
            impuesto.setNumDocSustento(ComprobantesUtil.eliminaCaracteres(invComprasTO.getCompDocumentoNumero(), "-"));
            impuesto.setFechaEmisionDocSustento(UtilsArchivos.fecha(invComprasTO.getCompFecha(), "dd-MM-yyyy", "dd/MM/yyyy"));// anxCompraTO.getCompEmision()
            resultado.getImpuesto().add(impuesto);
        }
        if (anxCompraTO.getCompBaseivaservicios().compareTo(BigDecimal.ZERO) > 0) {
            String codigoRetencion;
            if (anxCompraTO.getCompPorcentajeservicios().compareTo(new BigDecimal(20)) == 0) {
                codigoRetencion = "10";
            } else if (anxCompraTO.getCompPorcentajeservicios().compareTo(new BigDecimal(50)) == 0) {
                codigoRetencion = "11";
            } else if (anxCompraTO.getCompPorcentajeservicios().compareTo(new BigDecimal(70)) == 0) {
                codigoRetencion = "2";
            } else {
                codigoRetencion = "7";// 0%
            }
            impuesto = this.factoryRetencion.createImpuesto();
            impuesto.setCodigo("2");// impuesto Renta
            impuesto.setCodigoRetencion(codigoRetencion);// DE LA RETENCION
            // 20%-70% codigo
            // (10-2)
            impuesto.setPorcentajeRetener(anxCompraTO.getCompPorcentajeservicios());
            impuesto.setBaseImponible(anxCompraTO.getCompBaseivaservicios());
            impuesto.setValorRetenido(anxCompraTO.getCompValorservicios());
            impuesto.setCodDocSustento(invComprasTO.getCompDocumentoTipo());
            impuesto.setNumDocSustento(ComprobantesUtil.eliminaCaracteres(invComprasTO.getCompDocumentoNumero(), "-"));
            impuesto.setFechaEmisionDocSustento(UtilsArchivos.fecha(invComprasTO.getCompFecha(), "dd-MM-yyyy", "dd/MM/yyyy"));
            resultado.getImpuesto().add(impuesto);
        }
        if (anxCompraTO.getCompBaseivaserviciosprofesionales().compareTo(BigDecimal.ZERO) > 0) {
            String codigoRetencion;
            if (anxCompraTO.getCompPorcentajeserviciosprofesionales().compareTo(new BigDecimal(100)) == 0) {
                codigoRetencion = "3";
            } else {
                codigoRetencion = "7";
            }
            impuesto = this.factoryRetencion.createImpuesto();
            impuesto.setCodigo("2");// impuesto Renta
            impuesto.setCodigoRetencion(codigoRetencion);// DE LA RETENCION 100
            impuesto.setPorcentajeRetener(anxCompraTO.getCompPorcentajeserviciosprofesionales());
            impuesto.setBaseImponible(anxCompraTO.getCompBaseivaserviciosprofesionales());
            impuesto.setValorRetenido(anxCompraTO.getCompValorserviciosprofesionales());
            impuesto.setCodDocSustento(invComprasTO.getCompDocumentoTipo());
            impuesto.setNumDocSustento(ComprobantesUtil.eliminaCaracteres(invComprasTO.getCompDocumentoNumero(), "-"));
            impuesto.setFechaEmisionDocSustento(UtilsArchivos.fecha(invComprasTO.getCompFecha(), "dd-MM-yyyy", "dd/MM/yyyy"));
            resultado.getImpuesto().add(impuesto);
        }
        // RETENCIONES RENTA
        for (int i = 0; i < listAnxCompraDetalleTO.size(); i++) {
            impuesto = this.factoryRetencion.createImpuesto();
            impuesto.setCodigo("1");// impuesto Renta
            impuesto.setCodigoRetencion(listAnxCompraDetalleTO.get(i).getDetConcepto());// DE
            // LA RETENCION
            impuesto.setPorcentajeRetener(listAnxCompraDetalleTO.get(i).getDetPorcentaje());
            impuesto.setBaseImponible(listAnxCompraDetalleTO.get(i).getDetBaseImponible().add(listAnxCompraDetalleTO
                    .get(i).getDetBase0().add(listAnxCompraDetalleTO.get(i).getDetBaseNoObjetoIva())));
            impuesto.setValorRetenido(listAnxCompraDetalleTO.get(i).getDetValorRetenido());
            impuesto.setCodDocSustento(invComprasTO.getCompDocumentoTipo());
            impuesto.setNumDocSustento(ComprobantesUtil.eliminaCaracteres(invComprasTO.getCompDocumentoNumero() == null ? "999-999-999999999" : invComprasTO.getCompDocumentoNumero(), "-"));
            impuesto.setFechaEmisionDocSustento(UtilsArchivos.fecha(invComprasTO.getCompFecha(), "dd-MM-yyyy", "dd/MM/yyyy"));
            resultado.getImpuesto().add(impuesto);
        }
        return resultado;
    }

    private ComprobanteRetencion.InfoAdicional generarInformacionAdicionalComprobanteRetencion() {

        ComprobanteRetencion.InfoAdicional info = this.factoryRetencion.createComprobanteRetencionInfoAdicional();
        if (this.invProveedor.getProvDireccion() != null && !this.invProveedor.getProvDireccion().equals("")) {
            ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
            detalle.setNombre("Dirección");
            detalle.setValue((String) this.invProveedor.getProvDireccion());
            info.getCampoAdicional().add(detalle);
        }
        if (this.invProveedor.getProvTelefono() != null && !this.invProveedor.getProvTelefono().equals("")) {
            ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
            detalle.setNombre("Teléfono");
            detalle.setValue((String) this.invProveedor.getProvTelefono());
            info.getCampoAdicional().add(detalle);
        }
        if (this.invProveedor.getProvEmail() != null && !this.invProveedor.getProvEmail().equals("")) {
            ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
            detalle.setNombre("eMail de Notificacion");
            detalle.setValue((String) this.invProveedor.getProvEmail());
            info.getCampoAdicional().add(detalle);
        }
        if ((this.invComprasTO.getEmpCodigo() != null && !this.invComprasTO.getEmpCodigo().equals(""))
                && (this.invComprasTO.getCompPeriodo() != null && !this.invComprasTO.getCompPeriodo().equals(""))
                && (this.invComprasTO.getCompMotivo() != null && !this.invComprasTO.getCompMotivo().equals(""))
                && (this.invComprasTO.getCompNumero() != null && !this.invComprasTO.getCompNumero().equals(""))) {
            ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
            detalle.setNombre("Clave Interna");
            detalle.setValue(this.invComprasTO.getEmpCodigo() + " | " + this.invComprasTO.getCompPeriodo() + " | "
                    + this.invComprasTO.getCompMotivo() + " | " + this.invComprasTO.getCompNumero() + " | " + this.invComprasTO.getUsrInsertaCompra());
            info.getCampoAdicional().add(detalle);
        }
        if (this.emisor.getParWebDocumentosElectronicos() != null && !this.emisor.getParWebDocumentosElectronicos().equals("")) {
            ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) this.emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }

        return info;
    }
}
