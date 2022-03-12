/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.AnxVentaReembolsoTO;
import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvListaDetalleVentasTO;
import com.acosux.SRIMS.entidades.InvVentas;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.factura.ImpuestoFactura;
import com.acosux.SRIMS.util.sri.modelo.facturareembolso.FacturaReembolso;
import com.acosux.SRIMS.util.sri.modelo.facturareembolso.FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos.DetalleImpuesto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Trabajo
 */
public class GenerarXMLFacturaReembolso {

    BigDecimal totalComprobantesReembolso = BigDecimal.ZERO;
    BigDecimal totalBaseImponibleReembolso = BigDecimal.ZERO;
    BigDecimal totalImpuestoReembolso = BigDecimal.ZERO;

    public FacturaReembolso generarComprobanteFacturaReembolso(
            InvVentas invVentas,
            InvCliente invCliente,
            List<InvListaDetalleVentasTO> listaInvVentasDetalleTO,
            String claveDeAcceso,
            Emisor emisor,
            String codigoTipoTransaccion,
            String direccion,
            List<AnxVentaReembolsoTO> listaAnxVentaReembolsoTO,
            SisEmpresaParametros sisEmpresaParametros) throws Exception {

        FacturaReembolso facturaReembolso = null;
        BigDecimal ivaVigente = invVentas.getVtaIvaVigente();

        // ************************<infoTributaria>**********************
        InfoTributaria infoTributaria = new InfoTributaria();
        infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        infoTributaria.setTipoEmision(emisor.getTipoEmision());
        infoTributaria.setRazonSocial(emisor.getRazonSocial());
        infoTributaria.setNombreComercial(emisor.getNombreComercial());
        infoTributaria.setRuc(emisor.getRuc());
        if (claveDeAcceso != null) {
            infoTributaria.setClaveAcceso(claveDeAcceso);
        } else {
            return null;
        }
        infoTributaria.setCodDoc(TipoComprobanteEnum.FACTURA.getCode());
        infoTributaria.setEstab(invVentas.getVtaDocumentoNumero().substring(0, 3));
        infoTributaria.setPtoEmi(invVentas.getVtaDocumentoNumero().substring(4, 7));
        infoTributaria.setSecuencial(invVentas.getVtaDocumentoNumero().substring(8));
        infoTributaria.setDirMatriz(emisor.getDirEstablecimiento());
        if (sisEmpresaParametros.getParAgenteRetencion() != null && !sisEmpresaParametros.getParAgenteRetencion().equals("")) {
            infoTributaria.setAgenteRetencion(sisEmpresaParametros.getParAgenteRetencion());
        }
        // ************************<infoFactura>**********************
        FacturaReembolso.InfoFactura infoFacturaReembolso = new FacturaReembolso.InfoFactura();
        infoFacturaReembolso.setFechaEmision(UtilsArchivos.fecha(invVentas.getVtaFecha(), "dd/MM/yyyy"));
        infoFacturaReembolso.setDirEstablecimiento(emisor.getDirEstablecimiento());
        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            infoFacturaReembolso.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        infoFacturaReembolso.setObligadoContabilidad(emisor.getLlevaContabilidad());
        infoFacturaReembolso.setTipoIdentificacionComprador(codigoTipoTransaccion);
        infoFacturaReembolso.setRazonSocialComprador(invCliente.getCliRazonSocial());
        infoFacturaReembolso.setIdentificacionComprador(invCliente.getCliIdNumero() == null ? "9999999999999" : invCliente.getCliIdNumero());
        if (invCliente.getCliDireccion() != null && !invCliente.getCliDireccion().equals("")) {
            infoFacturaReembolso.setDireccionComprador(invCliente.getCliDireccion());//campo nuevo  
        }
        infoFacturaReembolso.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0().add(invVentas.getVtaSubtotalBaseImponible()), 2, java.math.RoundingMode.HALF_UP));
        infoFacturaReembolso.setTotalDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaDescuentoBase0()
                .add(invVentas.getVtaDescuentoBaseImponible()).add(invVentas.getVtaDescuentoBaseExenta()).// getVtaDescuentoGeneralBase0
                add(invVentas.getVtaDescuentoBaseNoObjeto())).add(BigDecimal.ZERO));

        //*************************impuestos*****************************
        infoFacturaReembolso.setTotalConImpuesto(generaTotalesImpuestoFacturaReembolso(
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP),
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBaseImponible(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP),
                ivaVigente, invVentas, listaInvVentasDetalleTO)
        );
        infoFacturaReembolso.setPropina(BigDecimal.ZERO);
        infoFacturaReembolso.setImporteTotal(invVentas.getVtaTotal());
        infoFacturaReembolso.setMoneda("DOLAR");
        //***************************PAGOS*********************************
        if (invVentas.getVtaPagadoEfectivo().compareTo(BigDecimal.ZERO) != 0
                || invVentas.getVtaPagadoDineroElectronico().compareTo(BigDecimal.ZERO) != 0
                || invVentas.getVtaPagadoTarjetaCredito().compareTo(BigDecimal.ZERO) != 0
                || invVentas.getVtaPagadoOtro().compareTo(BigDecimal.ZERO) != 0) {
            FacturaReembolso.InfoFactura.Pagos respuesta = new FacturaReembolso.InfoFactura.Pagos();
            FacturaReembolso.InfoFactura.Pagos.Pago pago;
            BigDecimal totalPagado = invVentas.getVtaPagadoEfectivo().add(invVentas.getVtaPagadoDineroElectronico().add(invVentas.getVtaPagadoTarjetaCredito().add(invVentas.getVtaPagadoOtro())));
            if (invVentas.getVtaPagadoDineroElectronico().compareTo(BigDecimal.ZERO) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("17");
                pago.setTotal(invVentas.getVtaPagadoDineroElectronico());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoDineroElectronico());
            }
            if (invVentas.getVtaPagadoTarjetaCredito().compareTo(BigDecimal.ZERO) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("19");
                pago.setTotal(invVentas.getVtaPagadoTarjetaCredito());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoTarjetaCredito());
            }
            if (invVentas.getVtaPagadoOtro().compareTo(BigDecimal.ZERO) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("20");
                pago.setTotal(invVentas.getVtaPagadoOtro());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoOtro());
            }
            if (invVentas.getVtaPagadoEfectivo().compareTo(BigDecimal.ZERO) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("01");
                pago.setTotal(invVentas.getVtaPagadoEfectivo().subtract(totalPagado));
                respuesta.getPago().add(pago);
            }
            infoFacturaReembolso.setPagos(respuesta);
        }
        ///////****************************DETALLES**********************************
        FacturaReembolso.Detalles detalles = new FacturaReembolso.Detalles();
        for (InvListaDetalleVentasTO invVentasDetalleTO : listaInvVentasDetalleTO) {
            if (invVentasDetalleTO.getProComplementario() == null || invVentasDetalleTO.getProComplementario().equals("")) {
                FacturaReembolso.Detalles.Detalle detalle = new FacturaReembolso.Detalles.Detalle();
                detalle.setCodigoPrincipal(invVentasDetalleTO.getCodigoProducto());
                detalle.setDescripcion(invVentasDetalleTO.getNombreProducto());
                BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
                BigDecimal recargo = BigDecimal.ONE.add(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPorcentajeRecargo().divide(new BigDecimal(100)), 15, RoundingMode.HALF_UP));
                BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto().multiply(recargo), 6, RoundingMode.HALF_UP);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(UtilsArchivos.redondeoDecimalBigDecimal(precioUnitario, 6, RoundingMode.HALF_UP));
                detalle.setDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto()
                        .multiply(invVentasDetalleTO.getPrecioProducto())
                        .multiply(invVentasDetalleTO.getPorcentajeDescuento()).divide(new java.math.BigDecimal("100"))));
                detalle.setPrecioTotalSinImpuesto(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getDetalleTotal()
                        .subtract(invVentasDetalleTO.getIvaCobrado())
                        .subtract(invVentasDetalleTO.getDetMontoIce())
                        .add(BigDecimal.ZERO), 2, RoundingMode.HALF_UP));
                FacturaReembolso.Detalles.Detalle.DetallesAdicionales obj = new FacturaReembolso.Detalles.Detalle.DetallesAdicionales();
                if (!emisor.getRuc().contains("0704469998001")) {
                    FacturaReembolso.Detalles.Detalle.DetallesAdicionales.DetAdicional det = new FacturaReembolso.Detalles.Detalle.DetallesAdicionales.DetAdicional();
                    det.setNombre("Medida");
                    det.setValor(invVentasDetalleTO.getMedidaDetalle());
                    obj.getDetAdicional().add(det);
                    detalle.setDetallesAdicionales(obj);
                }
                detalle.setImpuestos(obtenerImpuestosProductoFacturaReembolso(invVentasDetalleTO, ivaVigente));
                detalles.getDetalle().add(detalle);
            }
        }

        if (detalles != null) {
            facturaReembolso.setDetalles(detalles);
        }
        ///***********Rembolso detalle***********************
        FacturaReembolso.Reembolsos rembolsos = new FacturaReembolso.Reembolsos();
        for (AnxVentaReembolsoTO reemb : listaAnxVentaReembolsoTO) {
            FacturaReembolso.Reembolsos.ReembolsoDetalle detalle = new FacturaReembolso.Reembolsos.ReembolsoDetalle();
            String tipoProveedor = reemb.getProvIdTipo();
            switch (tipoProveedor) {
                case "R":
                    detalle.setTipoIdentificacionProveedorReembolso("04");
                    break;
                case "C":
                    detalle.setTipoIdentificacionProveedorReembolso("05");
                    break;
                case "P":
                    detalle.setTipoIdentificacionProveedorReembolso("06");
                    break;
                case "F":
                    detalle.setTipoIdentificacionProveedorReembolso("07");
                    break;
            }
            detalle.setIdentificacionProveedorReembolso(reemb.getProvIdNumero());
            detalle.setCodPaisPagoProveedorReembolso("593");
            //Averiguar si va null
            if (reemb.getProvExtranjeroTipo() != null) {
                detalle.setTipoProveedorReembolso(reemb.getProvExtranjeroTipo());
            }
            detalle.setCodDocReembolso(reemb.getReembDocumentoTipo());
            detalle.setEstabDocReembolso(reemb.getReembDocumentoNumero().substring(0, 3));
            detalle.setPtoEmiDocReembolso(reemb.getReembDocumentoNumero().substring(4, 7));
            detalle.setSecuencialDocReembolso(reemb.getReembDocumentoNumero().substring(8));
            detalle.setFechaEmisionDocReembolso(UtilsArchivos.fecha(reemb.getReembFechaemision(), "yyyy-MM-dd", "dd/MM/yyyy"));
            detalle.setNumeroautorizacionDocReemb(reemb.getReembAutorizacion());
            detalle.setDetalleImpuestos(obtenerImpuestosDetalleReembolso(reemb));
            rembolsos.getReembolsoDetalle().add(detalle);
        }
        if (rembolsos != null) {
            facturaReembolso.setReembolsos(rembolsos);
        }
        //*********************campo nuevo REEMBOLSO*******************
        infoFacturaReembolso.setCodDocReemb("41");
        infoFacturaReembolso.setTotalComprobantesReembolso(totalComprobantesReembolso);
        infoFacturaReembolso.setTotalBaseImponibleReembolso(totalBaseImponibleReembolso);
        infoFacturaReembolso.setTotalImpuestoReembolso(totalImpuestoReembolso);
        ///////****************************Informacion adicional**********************************
        FacturaReembolso.InfoAdicional informacion = generarInformacionAdicional(invCliente, invVentas, emisor, direccion);
        if (informacion.getCampoAdicional().size() > 0) {
            facturaReembolso.setInfoAdicional(informacion);
        }
        facturaReembolso.setVersion("1.1.0");
        facturaReembolso.setId("comprobante");
        //
        return facturaReembolso;
    }

    private FacturaReembolso.Detalles.Detalle.Impuestos obtenerImpuestosProductoFacturaReembolso(InvListaDetalleVentasTO invVentasDetalleTO, BigDecimal ivaVigente) {
        FacturaReembolso.Detalles.Detalle.Impuestos result = new FacturaReembolso.Detalles.Detalle.Impuestos();
        ImpuestoFactura i = new ImpuestoFactura();
        BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
        BigDecimal recargo = BigDecimal.ONE.add(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPorcentajeRecargo().divide(new BigDecimal(100)), 6, RoundingMode.HALF_UP));
        BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto().multiply(recargo), 6, RoundingMode.HALF_UP);
        i.setCodigo("2");
        i.setCodigoPorcentaje(codigoPorcentajeIva(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0, ivaVigente));
        i.setTarifa(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0 ? ivaVigente : BigDecimal.ZERO);
        i.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                .add(BigDecimal.ZERO).add(invVentasDetalleTO.getDetMontoIce())));
        i.setValor(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0
                ? UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                        .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento()
                                .divide(new java.math.BigDecimal("100"))))
                        .add(BigDecimal.ZERO).multiply(ivaVigente.divide(new java.math.BigDecimal("100.00"), 2,
                        java.math.RoundingMode.HALF_UP)))
                : BigDecimal.ZERO);
        result.getImpuesto().add(i);
        if (invVentasDetalleTO.getDetMontoIce() != null && invVentasDetalleTO.getDetMontoIce().compareTo(BigDecimal.ZERO) > 0) {
            ImpuestoFactura ice = new ImpuestoFactura();
            ice.setCodigo("3");
            ice.setCodigoPorcentaje(invVentasDetalleTO.getIceCodigo());
            ice.setTarifa(invVentasDetalleTO.getIceTarifaFija() != null ? invVentasDetalleTO.getIceTarifaFija() : BigDecimal.ZERO);
            ice.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP));
            ice.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getDetMontoIce(), 2, RoundingMode.HALF_UP));
            result.getImpuesto().add(ice);
        }
        return result;
    }

    private String codigoPorcentajeIva(boolean llevaIva, BigDecimal ivaVigente) {
        /*
		 * TABLA 17 PORCENTAJE CODIGO 0% ------------ 0 12% ------------ 2 14%
		 * ------------ 3 No Objeto de Impuesto ------- 6 Exento de IVA -- 7
         */
        String codigoPorcentaje = "0";
        if (llevaIva && ivaVigente.compareTo(new BigDecimal("12.00")) == 0) {
            codigoPorcentaje = "2";
        } else if (llevaIva && ivaVigente.compareTo(new BigDecimal("14.00")) == 0) {
            codigoPorcentaje = "3";
        }
        return codigoPorcentaje;
    }

    private FacturaReembolso.InfoAdicional generarInformacionAdicional(InvCliente invCliente, InvVentas invVentas, Emisor emisor, String direccion) throws Exception {
        FacturaReembolso.InfoAdicional info = new FacturaReembolso.InfoAdicional();
        if (invCliente.getCliDireccion() != null && !invCliente.getCliDireccion().equals("")) {
            if (!invCliente.getCliDireccion().equals("-")) {
                if (invVentas != null && invVentas.getCliCodigoEstablecimiento() != null && !invVentas.getCliCodigoEstablecimiento().equals("")
                        && invCliente.getCliCodigoEstablecimiento() != null && !invCliente.getCliCodigoEstablecimiento().equals("")
                        && !invCliente.getCliCodigoEstablecimiento().equals(invVentas.getCliCodigoEstablecimiento())) {
                    if (direccion != null && !direccion.equals("")) {
                        FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                        detalle.setNombre("Dirección");
                        detalle.setValue((String) direccion);
                        info.getCampoAdicional().add(detalle);
                    } else {
                        FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                        detalle.setNombre("Dirección");
                        detalle.setValue((String) invCliente.getCliDireccion());
                        info.getCampoAdicional().add(detalle);
                    }
                } else {
                    FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                    detalle.setNombre("Dirección");
                    detalle.setValue((String) invCliente.getCliDireccion());
                    info.getCampoAdicional().add(detalle);
                }
            }
        }
        if (invCliente.getCliTelefono() != null) {
            if (!invCliente.getCliTelefono().equals("")) {
                FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                detalle.setNombre("Teléfono");
                detalle.setValue((String) invCliente.getCliTelefono());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (invCliente.getCliEmail() != null) {
            if (!invCliente.getCliEmail().equals("")) {
                FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                detalle.setNombre("eMail de Notificacion");
                detalle.setValue((String) invCliente.getCliEmail());
                info.getCampoAdicional().add(detalle);
            }
        }

        if ((invVentas.getInvVentasPK().getVtaEmpresa() != null
                && !invVentas.getInvVentasPK().getVtaEmpresa().equals(""))
                && (invVentas.getInvVentasPK().getVtaPeriodo() != null
                && !invVentas.getInvVentasPK().getVtaPeriodo().equals(""))
                && (invVentas.getInvVentasPK().getVtaMotivo() != null
                && !invVentas.getInvVentasPK().getVtaMotivo().equals(""))
                && (invVentas.getInvVentasPK().getVtaNumero() != null
                && !invVentas.getInvVentasPK().getVtaNumero().equals(""))) {
            FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
            detalle.setNombre("Clave Interna");
            detalle.setValue(invVentas.getInvVentasPK().getVtaEmpresa() + " | "
                    + invVentas.getInvVentasPK().getVtaPeriodo() + " | "
                    + invVentas.getInvVentasPK().getVtaMotivo() + " | "
                    + invVentas.getInvVentasPK().getVtaNumero() + " | "
                    + invVentas.getUsrCodigo());
            info.getCampoAdicional().add(detalle);
        }

        if (emisor.getParWebDocumentosElectronicos() != null
                && !emisor.getParWebDocumentosElectronicos().equals("")) {
            FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }

        if (invVentas.getVtaInformacionAdicional() != null) {
            java.util.List<String> informacionAdicional = UtilsArchivos.separar(invVentas.getVtaInformacionAdicional(), "|");
            for (int i = 0; i < informacionAdicional.size(); i++) {
                if (!informacionAdicional.get(i).equals("|")
                        && informacionAdicional.get(i).compareTo("") > 0
                        && informacionAdicional.get(i).lastIndexOf("=") >= 0) {
                    FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
                    detalle.setNombre(informacionAdicional.get(i).substring(0, informacionAdicional.get(i).lastIndexOf("=")));
                    detalle.setValue(informacionAdicional.get(i).substring(informacionAdicional.get(i).lastIndexOf("=") + 1));
                    info.getCampoAdicional().add(detalle);
                }
            }
        }

        if (invVentas.getVtaFormaPago().equalsIgnoreCase("POR PAGAR")
                && (emisor.getRuc().equalsIgnoreCase("0993013447001")
                || emisor.getRuc().equalsIgnoreCase("0993046590001")
                || emisor.getRuc().equalsIgnoreCase("0992879254001")
                || emisor.getRuc().equalsIgnoreCase("0791702070001") //NET2
                || emisor.getRuc().equalsIgnoreCase("0791702054001") //NET3
                || emisor.getRuc().equalsIgnoreCase("0791755093001") //NET1
                || emisor.getRuc().equalsIgnoreCase("0993176907001"))) {
            //SOLO SI ES "MARAQUATIK" o "BALANCEADOS DEL PACIFICO BAPACIF SA" o "COPACIGULF o "INSUMOS DEL MAR MARAQUATIK"
            FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
            detalle.setNombre("Fecha Vencimiento");
            detalle.setValue(UtilsArchivos.fecha(invVentas.getVtaFechaVencimiento(), "yyyy-MM-dd"));
            info.getCampoAdicional().add(detalle);
        }

        if (invVentas.getVtaFormaPago().equalsIgnoreCase("POR PAGAR") && (emisor.getRuc().equalsIgnoreCase("0992879254001"))) {
            //SOLO SI ES "COPACIGULF"
            FacturaReembolso.InfoAdicional.CampoAdicional detalle = new FacturaReembolso.InfoAdicional.CampoAdicional();
            detalle.setNombre("Observaciones");
            detalle.setValue(invVentas.getVtaObservaciones());
            info.getCampoAdicional().add(detalle);
        }
        return info;
    }

    private FacturaReembolso.InfoFactura.TotalConImpuesto generaTotalesImpuestoFacturaReembolso(BigDecimal parcialCero,
            BigDecimal parcialImponible, BigDecimal ivaVigente, InvVentas invVentas, List<InvListaDetalleVentasTO> listaInvVentasDetalleTO) {
        FacturaReembolso.InfoFactura.TotalConImpuesto respuesta = new FacturaReembolso.InfoFactura.TotalConImpuesto();
        FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto impuesto;
        // iva 0%
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false, ivaVigente));
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(BigDecimal.ZERO);
            respuesta.getTotalImpuesto().add(impuesto);
        }
        // iva 12%
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true, ivaVigente));
            impuesto.setBaseImponible(parcialImponible.add(invVentas.getVtaMontoIce()));
            impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaMontoiva(), 2, java.math.RoundingMode.HALF_UP));
            respuesta.getTotalImpuesto().add(impuesto);
        }
        //ice
        for (InvListaDetalleVentasTO detalle : listaInvVentasDetalleTO) {
            if (detalle.getDetMontoIce() != null && detalle.getDetMontoIce().compareTo(BigDecimal.ZERO) > 0 && (detalle.getProComplementario() == null || detalle.getProComplementario().equals(""))) {
                //encontrar impuesto existente
                Optional<FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto> impuestoExistente = respuesta.getTotalImpuesto().stream()
                        .filter(s -> s.getCodigo().equals("3") && s.getCodigoPorcentaje().equals(detalle.getIceCodigo()))
                        .findFirst();
                impuesto = impuestoExistente.isPresent() ? impuestoExistente.get() : null;
                if (impuesto != null) {
                    impuesto.setBaseImponible(impuesto.getBaseImponible().add(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP)));
                    impuesto.setValor(impuesto.getValor().add(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getDetMontoIce(), 2, java.math.RoundingMode.HALF_UP)));
                } else {
                    impuesto = new FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto();
                    impuesto.setCodigo("3");
                    impuesto.setCodigoPorcentaje(detalle.getIceCodigo());
                    impuesto.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP));
                    impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getDetMontoIce(), 2, java.math.RoundingMode.HALF_UP));
                    respuesta.getTotalImpuesto().add(impuesto);
                }
            }
        }
        return respuesta;
    }

    private FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos obtenerImpuestosDetalleReembolso(AnxVentaReembolsoTO anxVentaReembolsoTO) {
        FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos result = new FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos();
        DetalleImpuesto i = new DetalleImpuesto();
        //******************IVA*********************
        i.setCodigo("2");
        //codigo dependiendo porcentaje iva
        i.setCodigoPorcentaje(codigoPorcentajeIva(
                anxVentaReembolsoTO.getReembMontoiva() != null && anxVentaReembolsoTO.getReembMontoiva().compareTo(BigDecimal.ZERO) > 0,
                anxVentaReembolsoTO.getReembIvaVigente()));
        //monto iva
        i.setTarifa((anxVentaReembolsoTO.getReembMontoiva().compareTo(BigDecimal.ZERO) > 0) ? anxVentaReembolsoTO.getReembMontoiva() : BigDecimal.ZERO);
        //suma de base 0 + base ng + base imp
        i.setBaseImponibleReembolso(UtilsArchivos.redondeoDecimalBigDecimal(
                (anxVentaReembolsoTO.getReembBaseimponible() != null ? anxVentaReembolsoTO.getReembBaseimponible() : BigDecimal.ZERO).add(
                        (anxVentaReembolsoTO.getReembBaseimpgrav() != null ? anxVentaReembolsoTO.getReembBaseimpgrav() : BigDecimal.ZERO)).add(
                        (anxVentaReembolsoTO.getReembBasenograiva() != null ? anxVentaReembolsoTO.getReembBasenograiva() : BigDecimal.ZERO))
        ));
        totalBaseImponibleReembolso = UtilsArchivos.redondeoDecimalBigDecimal(totalBaseImponibleReembolso.add(i.getBaseImponibleReembolso()));
        //suma monto ice + monto iva
        i.setImpuestoReembolso(
                UtilsArchivos.redondeoDecimalBigDecimal(
                        (anxVentaReembolsoTO.getReembMontoice() != null ? anxVentaReembolsoTO.getReembMontoice() : BigDecimal.ZERO).add(
                                (anxVentaReembolsoTO.getReembMontoiva() != null ? anxVentaReembolsoTO.getReembMontoiva() : BigDecimal.ZERO))
                ));
        totalImpuestoReembolso = UtilsArchivos.redondeoDecimalBigDecimal(totalImpuestoReembolso.add(i.getImpuestoReembolso()));
        totalComprobantesReembolso = UtilsArchivos.redondeoDecimalBigDecimal(totalComprobantesReembolso.add(totalBaseImponibleReembolso).add(totalImpuestoReembolso));
        result.getDetalleImpuesto().add(i);
        //****************ICE********************
        if (anxVentaReembolsoTO.getReembMontoice() != null && anxVentaReembolsoTO.getReembMontoice().compareTo(BigDecimal.ZERO) > 0) {
            DetalleImpuesto ice = new DetalleImpuesto();
            ice.setCodigo("3");//ICE
            //ice.setCodigoPorcentaje(); AUN FALTA
            ice.setTarifa(anxVentaReembolsoTO.getReembMontoice());
            ice.setBaseImponibleReembolso(UtilsArchivos.redondeoDecimalBigDecimal(
                    (anxVentaReembolsoTO.getReembBaseimponible() != null ? anxVentaReembolsoTO.getReembBaseimponible() : BigDecimal.ZERO).add(
                            (anxVentaReembolsoTO.getReembBaseimpgrav() != null ? anxVentaReembolsoTO.getReembBaseimpgrav() : BigDecimal.ZERO)).add(
                            (anxVentaReembolsoTO.getReembBasenograiva() != null ? anxVentaReembolsoTO.getReembBasenograiva() : BigDecimal.ZERO))
            ));
            ice.setImpuestoReembolso(
                    UtilsArchivos.redondeoDecimalBigDecimal(
                            (anxVentaReembolsoTO.getReembMontoice() != null ? anxVentaReembolsoTO.getReembMontoice() : BigDecimal.ZERO).add(
                                    (anxVentaReembolsoTO.getReembMontoiva() != null ? anxVentaReembolsoTO.getReembMontoiva() : BigDecimal.ZERO))
                    ));
            result.getDetalleImpuesto().add(ice);
        }

        return result;
    }
}
