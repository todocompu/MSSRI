/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.InvComprasDetalle;
import com.acosux.SRIMS.entidades.InvComprasTO;
import com.acosux.SRIMS.entidades.InvProveedor;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.liquidacioncompra.ImpuestoLiquidacion;
import com.acosux.SRIMS.util.sri.modelo.liquidacioncompra.LiquidacionCompra;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author CarolValdiviezo
 */
public class GenerarXMLiquidacionCompras {

    private InfoTributaria infoTributaria = null;
    private InvProveedor invProveedor = null;
    private InvComprasTO invComprasTO = null;
    private LiquidacionCompra.InfoLiquidacionCompra infoLiquidacionCompra = null;
    private SisEmpresaParametros sisEmpresaParametros;

    public LiquidacionCompra generarXMLiquidacionCompras(InvComprasTO invComprasTO, InvProveedor invProveedor, List<InvComprasDetalle> listInvComprasDetalle,
            String claveDeAcceso, Emisor emisor, String agenteRetencion, SisEmpresaParametros sisEmpresaParametros) throws Exception {
        this.invProveedor = invProveedor;
        this.invComprasTO = invComprasTO;
        this.sisEmpresaParametros = sisEmpresaParametros;

        LiquidacionCompra liqCompra = null;

        if (!llenarObjetoComprobanteElectronicoLiquidacionCompras(emisor, claveDeAcceso, agenteRetencion)) {
            LiquidacionCompra.Detalles detalles = generarDetalleLiquidacionCompraYTotales(listInvComprasDetalle, invComprasTO.getCompIvaVigente(), emisor.getRuc());
            LiquidacionCompra.InfoAdicional informacion = generarInformacionAdicionalLiquidacionCompra(emisor);
            liqCompra = new LiquidacionCompra();
            liqCompra.setInfoTributaria(this.infoTributaria);
            liqCompra.setInfoLiquidacionCompra(this.infoLiquidacionCompra);
            if (detalles != null) {
                liqCompra.setDetalles(detalles);
            }
            if (informacion.getCampoAdicional().size() > 0) {
                liqCompra.setInfoAdicional(informacion);
            }
            liqCompra.setVersion("1.1.0");
            liqCompra.setId("comprobante");
        }

        return liqCompra;
    }

    private boolean llenarObjetoComprobanteElectronicoLiquidacionCompras(Emisor emisor, String claveDeAcceso, String agenteRetencion) {
        boolean error = false;
        this.infoTributaria = new InfoTributaria();
        // <infoTributaria>
        this.infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(emisor.getRazonSocial());
        this.infoTributaria.setNombreComercial(emisor.getNombreComercial());
        this.infoTributaria.setRuc(emisor.getRuc());
        /// clave de acceso falta
        if (claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCode());
        this.infoTributaria.setEstab(invComprasTO.getCompDocumentoNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(invComprasTO.getCompDocumentoNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(invComprasTO.getCompDocumentoNumero().substring(8));
        this.infoTributaria.setDirMatriz(emisor.getDirEstablecimiento());
        if (agenteRetencion != null && !agenteRetencion.equals("")) {
            this.infoTributaria.setAgenteRetencion(agenteRetencion);
        }
        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
            this.infoTributaria.setContribuyenteRimpe("CONTRIBUYENTE RÉGIMEN RIMPE");
        }

        // </infoTributaria>
        // infoLiquidacionCompra();
        this.infoLiquidacionCompra = new LiquidacionCompra.InfoLiquidacionCompra();
        // 2016-06-02
        this.infoLiquidacionCompra.setFechaEmision(UtilsArchivos.fecha(invComprasTO.getCompFecha(), "dd-MM-yyyy", "dd/MM/yyyy"));
        this.infoLiquidacionCompra.setDirEstablecimiento(emisor.getDirEstablecimiento());
        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            this.infoLiquidacionCompra.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        this.infoLiquidacionCompra.setObligadoContabilidad(emisor.getLlevaContabilidad());
        //Proveedor
        String tipoProveedor = invProveedor.getProvIdTipo();
        switch (tipoProveedor) {
            case "R":
                this.infoLiquidacionCompra.setTipoIdentificacionProveedor("04");
                break;
            case "C":
                this.infoLiquidacionCompra.setTipoIdentificacionProveedor("05");
                break;
            case "P":
                this.infoLiquidacionCompra.setTipoIdentificacionProveedor("06");
                break;
        }

        this.infoLiquidacionCompra.setRazonSocialProveedor(invProveedor.getProvRazonSocial());
        this.infoLiquidacionCompra.setIdentificacionProveedor(invProveedor.getProvIdNumero() == null ? "9999999999999" : invProveedor.getProvIdNumero());
        if (this.invProveedor.getProvDireccion() != null) {
            this.infoLiquidacionCompra.setDireccionProveedor(invProveedor.getProvDireccion());
        }

        return error;
    }

    private LiquidacionCompra.Detalles generarDetalleLiquidacionCompraYTotales(List<InvComprasDetalle> listInvComprasDetalle, BigDecimal ivaVigente, String rucEmisor) {
        LiquidacionCompra.Detalles resultado = new LiquidacionCompra.Detalles();
        BigDecimal totalDescuento = new BigDecimal("0.00");
        for (InvComprasDetalle invComprasDetalle : listInvComprasDetalle) {
            LiquidacionCompra.Detalles.Detalle detalle = new LiquidacionCompra.Detalles.Detalle();
            detalle.setCodigoPrincipal(invComprasDetalle.getInvProducto().getInvProductoPK().getProCodigoPrincipal());
            detalle.setDescripcion(invComprasDetalle.getInvProducto().getProNombre());
            BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetCantidad(), 6, RoundingMode.HALF_UP);
            BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetPrecio(), 15, RoundingMode.HALF_UP);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(UtilsArchivos.redondeoDecimalBigDecimal(precioUnitario, 6, RoundingMode.HALF_UP));
            detalle.setDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetCantidad()
                    .multiply(invComprasDetalle.getDetPrecio())
                    .multiply(invComprasDetalle.getDetPorcentajeDescuento()).divide(new java.math.BigDecimal("100"))));
            detalle.setPrecioTotalSinImpuesto(UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetCantidad()
                    .multiply(precioUnitario)
                    .subtract(detalle.getDescuento())
                    .add(new BigDecimal("0.00"))));

            //Para añadir detalles a cada registro
            if (!rucEmisor.contains("0704469998001")) {
                LiquidacionCompra.Detalles.Detalle.DetallesAdicionales obj = new LiquidacionCompra.Detalles.Detalle.DetallesAdicionales();
                LiquidacionCompra.Detalles.Detalle.DetallesAdicionales.DetAdicional det = new LiquidacionCompra.Detalles.Detalle.DetallesAdicionales.DetAdicional();
                det.setNombre("Medida");
                det.setValor(invComprasDetalle.getInvProducto().getInvProductoMedida().getMedDetalle());
                obj.getDetAdicional().add(det);
                detalle.setDetallesAdicionales(obj);
            }

            detalle.setImpuestos(obtenerImpuestosProductoLiquidacionCompra(invComprasDetalle, ivaVigente));
            resultado.getDetalle().add(detalle);
            totalDescuento.add(detalle.getDescuento());
        }

        //Totales
        this.infoLiquidacionCompra.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(invComprasTO.getCompBase0().add(invComprasTO.getCompBaseImponible()), 2, java.math.RoundingMode.HALF_UP));
        this.infoLiquidacionCompra.setTotalDescuento(totalDescuento);
        BigDecimal compBase0 = UtilsArchivos.redondeoDecimalBigDecimal(invComprasTO.getCompBase0(), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal compBaseImponible = UtilsArchivos.redondeoDecimalBigDecimal(invComprasTO.getCompBaseImponible(), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal compIvaVigente = UtilsArchivos.redondeoDecimalBigDecimal(invComprasTO.getCompIvaVigente(), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal compMontoIva = UtilsArchivos.redondeoDecimalBigDecimal(invComprasTO.getCompMontoIva(), 2, java.math.RoundingMode.HALF_UP);
        this.infoLiquidacionCompra.setTotalConImpuestos(generaTotalesImpuestoLiquidacionCompra(compBase0, compBaseImponible, compIvaVigente, compMontoIva));
        this.infoLiquidacionCompra.setImporteTotal(invComprasTO.getCompTotal());
        this.infoLiquidacionCompra.setMoneda("DOLAR");
        //pagos
        if (invComprasTO.getCompTotal().compareTo(new BigDecimal("0.00")) != 0) {
            this.infoLiquidacionCompra.setPagos(generarPagosLiquidacionCompra());
        }
        return resultado;
    }

    private LiquidacionCompra.Detalles.Detalle.Impuestos obtenerImpuestosProductoLiquidacionCompra(InvComprasDetalle invComprasDetalle, BigDecimal ivaVigente) {
        LiquidacionCompra.Detalles.Detalle.Impuestos result = new LiquidacionCompra.Detalles.Detalle.Impuestos();
        ImpuestoLiquidacion i = new ImpuestoLiquidacion();
        BigDecimal cero = new BigDecimal("0.00");
        BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetCantidad(), 6, RoundingMode.HALF_UP);
        BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invComprasDetalle.getDetPrecio(), 6, RoundingMode.HALF_UP);
        i.setCodigo("2");
        i.setCodigoPorcentaje(codigoPorcentajeIva(invComprasDetalle.getInvProducto().getProIva().compareTo("GRAVA") == 0, ivaVigente));
        i.setTarifa(invComprasDetalle.getInvProducto().getProIva().compareTo("GRAVA") == 0 ? ivaVigente : cero);
        i.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                .multiply(BigDecimal.ONE.subtract(invComprasDetalle.getDetPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                .add(cero)));
        i.setValor(invComprasDetalle.getInvProducto().getProIva().compareTo("GRAVA") == 0
                ? UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                        .multiply(BigDecimal.ONE.subtract(invComprasDetalle.getDetPorcentajeDescuento()
                                .divide(new java.math.BigDecimal("100"))))
                        .add(cero).multiply(ivaVigente.divide(new java.math.BigDecimal("100.00"), 2,
                        java.math.RoundingMode.HALF_UP)))
                : cero);
        result.getImpuesto().add(i);
        return result;
    }

    private LiquidacionCompra.InfoLiquidacionCompra.Pagos generarPagosLiquidacionCompra() {
        LiquidacionCompra.InfoLiquidacionCompra.Pagos respuesta = new LiquidacionCompra.InfoLiquidacionCompra.Pagos();
        LiquidacionCompra.InfoLiquidacionCompra.Pagos.Pago pago = new LiquidacionCompra.InfoLiquidacionCompra.Pagos.Pago();
        pago.setFormaPago("20");
        pago.setTotal(invComprasTO.getCompTotal());
        respuesta.getPago().add(pago);

        return respuesta;
    }

    private LiquidacionCompra.InfoAdicional generarInformacionAdicionalLiquidacionCompra(Emisor emisor) {
        LiquidacionCompra.InfoAdicional info = new LiquidacionCompra.InfoAdicional();
        if (this.invProveedor.getProvDireccion() != null && !this.invProveedor.getProvDireccion().equals("")) {
            if (!this.invProveedor.getProvDireccion().equals("-")) {
                LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
                detalle.setNombre("Dirección");
                detalle.setValue((String) invProveedor.getProvDireccion());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invProveedor.getProvTelefono() != null) {
            if (!this.invProveedor.getProvTelefono().equals("")) {
                LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
                detalle.setNombre("Teléfono");
                detalle.setValue((String) this.invProveedor.getProvTelefono());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invProveedor.getProvEmail() != null) {
            if (!this.invProveedor.getProvEmail().equals("")) {
                LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
                detalle.setNombre("eMail de Notificacion");
                detalle.setValue((String) this.invProveedor.getProvEmail());
                info.getCampoAdicional().add(detalle);
            }
        }

        if ((this.invComprasTO.getEmpCodigo() != null
                && !this.invComprasTO.getEmpCodigo().equals(""))
                && (this.invComprasTO.getCompPeriodo() != null
                && !this.invComprasTO.getCompPeriodo().equals(""))
                && (this.invComprasTO.getCompMotivo() != null
                && !this.invComprasTO.getCompMotivo().equals(""))
                && (this.invComprasTO.getCompNumero() != null
                && !this.invComprasTO.getCompNumero().equals(""))) {
            LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
            detalle.setNombre("Clave Interna");
            detalle.setValue(this.invComprasTO.getEmpCodigo() + " | "
                    + this.invComprasTO.getCompPeriodo() + " | "
                    + this.invComprasTO.getCompMotivo() + " | "
                    + this.invComprasTO.getCompNumero() + " | "
                    + this.invComprasTO.getUsrInsertaCompra());
            info.getCampoAdicional().add(detalle);
        }

        if (emisor.getParWebDocumentosElectronicos() != null && !emisor.getParWebDocumentosElectronicos().equals("")) {
            LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }

        if (this.invComprasTO.getCompFormaPago().equalsIgnoreCase("POR PAGAR")
                && (emisor.getRuc().equalsIgnoreCase("0993013447001")
                || emisor.getRuc().equalsIgnoreCase("0993046590001")
                || emisor.getRuc().equalsIgnoreCase("0791702070001") //NET2
                || emisor.getRuc().equalsIgnoreCase("0791702054001") //NET3
                || emisor.getRuc().equalsIgnoreCase("0791755093001") //NET1
                || emisor.getRuc().equalsIgnoreCase("0992879254001"))) {
            //SOLO SI ES "MARAQUATIK" o "BALANCEADOS DEL PACIFICO BAPACIF SA" o "COPACIGULF"
            LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
            detalle.setNombre("Fecha Vencimiento");
            detalle.setValue(this.invComprasTO.getCompFechaVencimiento());
            info.getCampoAdicional().add(detalle);
        }

        if (this.invComprasTO.getCompFormaPago().equalsIgnoreCase("POR PAGAR") && (emisor.getRuc().equalsIgnoreCase("0992879254001"))) {
            //SOLO SI ES "COPACIGULF"
            LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
            detalle.setNombre("Observaciones");
            detalle.setValue(this.invComprasTO.getCompObservaciones());
            info.getCampoAdicional().add(detalle);
        }

//        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
//            LiquidacionCompra.InfoAdicional.CampoAdicional detalle = new LiquidacionCompra.InfoAdicional.CampoAdicional();
//            detalle.setNombre("Régimen");
//            detalle.setValue("Contribuyente régimen RIMPE");
//            info.getCampoAdicional().add(detalle);
//        }
        return info;
    }

    private String codigoPorcentajeIva(boolean llevaIva, BigDecimal ivaVigente) {
        String codigoPorcentaje = "0";
        if (llevaIva && ivaVigente.compareTo(new BigDecimal("12.00")) == 0) {
            codigoPorcentaje = "2";
        } else if (llevaIva && ivaVigente.compareTo(new BigDecimal("14.00")) == 0) {
            codigoPorcentaje = "3";
        }
        return codigoPorcentaje;
    }

    private LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos generaTotalesImpuestoLiquidacionCompra(BigDecimal parcialCero, BigDecimal parcialImponible, BigDecimal ivaVigente, BigDecimal montoiva) {
        LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos respuesta = new LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos();
        LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto impuesto;
        // iva 0%
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false, ivaVigente));
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(new BigDecimal("0.00"));
            respuesta.getTotalImpuesto().add(impuesto);
        }
        // iva 12%
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true, ivaVigente));
            impuesto.setBaseImponible(parcialImponible);
            impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(montoiva, 2, java.math.RoundingMode.HALF_UP));
            respuesta.getTotalImpuesto().add(impuesto);
        }
        return respuesta;
    }
}
