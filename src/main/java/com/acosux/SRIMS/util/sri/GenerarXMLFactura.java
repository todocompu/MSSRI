package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.AnxVentaReembolsoTO;
import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvListaDetalleVentasTO;
import com.acosux.SRIMS.entidades.InvVentaGuiaRemision;
import com.acosux.SRIMS.entidades.InvVentas;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.factura.Factura;
import com.acosux.SRIMS.util.sri.modelo.factura.Factura.InfoFactura.Pagos.Pago;
import com.acosux.SRIMS.util.sri.modelo.factura.ImpuestoFactura;
import com.acosux.SRIMS.util.sri.modelo.factura.ObjectFactoryFactura;
import com.acosux.SRIMS.util.sri.modelo.facturareembolso.FacturaReembolso;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import java.util.Optional;

public class GenerarXMLFactura {

    private ObjectFactoryFactura facturaFactory = null;
    private InfoTributaria infoTributaria = null;
    private Factura.InfoFactura infoFactura = null;
    private FacturaReembolso.InfoFactura infoFacturaReembolso = null;
    private InvVentas invVentas = null;
    private InvCliente invCliente = null;
    private InvVentaGuiaRemision invVentaGuiaRemision = null;
    private List<InvListaDetalleVentasTO> listaInvVentasDetalleTO;
    private String claveDeAcceso;
    private String codigoTipoTransaccion;
    private Emisor emisor = null;
    private java.math.BigDecimal ivaVigente = new java.math.BigDecimal("0.00");
    private java.math.BigDecimal cero = new java.math.BigDecimal("0.00");
    private String direccion;
    private SisEmpresaParametros sisEmpresaParametros;

    public Factura generarComprobanteFactura(InvVentas invVentas, InvCliente invCliente,
            List<InvListaDetalleVentasTO> listaInvVentasDetalleTO, String claveDeAcceso, Emisor emisor,
            String codigoTipoTransaccion, InvVentaGuiaRemision guia, String direccion,
            SisEmpresaParametros sisEmpresaParametros) throws Exception {
        this.invVentas = invVentas;
        this.invCliente = invCliente;
        this.listaInvVentasDetalleTO = listaInvVentasDetalleTO;
        this.claveDeAcceso = claveDeAcceso;
        this.emisor = emisor;
        this.codigoTipoTransaccion = codigoTipoTransaccion;
        this.facturaFactory = new ObjectFactoryFactura();
        this.invVentaGuiaRemision = guia;
        this.sisEmpresaParametros = sisEmpresaParametros;
        Factura factura = null;
        this.direccion = direccion;

        this.ivaVigente = invVentas.getVtaIvaVigente();
        if (!llenarObjetoComprobanteElectronicoFactura()) {
            Factura.Detalles detalles = generarDetalleFactura();
            Factura.InfoAdicional informacion = generarInformacionAdicionalFactura();
            factura = new Factura();
            factura.setInfoTributaria(this.infoTributaria);
            factura.setInfoFactura(this.infoFactura);
            if (detalles != null) {
                factura.setDetalles(detalles);
            }
            if (this.invVentaGuiaRemision != null) {
                factura.setInfoSustitutivaGuiaRemision(generarSustitutivaGuia());
                factura.setVersion("2.1.0");
            } else {
                if (informacion.getCampoAdicional().size() > 0) {
                    factura.setInfoAdicional(informacion);
                }
                factura.setVersion("1.1.0");
            }

            factura.setId("comprobante");
        }

        return factura;
    }

    private boolean llenarObjetoComprobanteElectronicoFactura() {
        boolean error = false;
        this.infoTributaria = new InfoTributaria();
        // <infoTributaria>
        this.infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(emisor.getRazonSocial());
        this.infoTributaria.setNombreComercial(emisor.getNombreComercial());
        this.infoTributaria.setRuc(emisor.getRuc());
        /// clave de acceso falta
        if (this.claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(this.claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.FACTURA.getCode());
        this.infoTributaria.setEstab(invVentas.getVtaDocumentoNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(invVentas.getVtaDocumentoNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(invVentas.getVtaDocumentoNumero().substring(8));
        this.infoTributaria.setDirMatriz(emisor.getDirEstablecimiento());

        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
//            this.infoTributaria.setContribuyenteRimpe("CONTRIBUYENTE RÉGIMEN RIMPE");
        }
        if (this.sisEmpresaParametros.getParAgenteRetencion() != null && !this.sisEmpresaParametros.getParAgenteRetencion().equals("")) {
            this.infoTributaria.setAgenteRetencion(sisEmpresaParametros.getParAgenteRetencion());
        }

        this.infoFactura = this.facturaFactory.createFacturaInfoFactura();
        // 2016-06-02
        this.infoFactura
                .setFechaEmision(UtilsArchivos.fecha(invVentas.getVtaFecha(), "dd/MM/yyyy"));
        this.infoFactura.setDirEstablecimiento(emisor.getDirEstablecimiento());
        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            this.infoFactura.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        this.infoFactura.setObligadoContabilidad(emisor.getLlevaContabilidad());
        this.infoFactura.setTipoIdentificacionComprador(codigoTipoTransaccion);
        this.infoFactura.setRazonSocialComprador(invCliente.getCliRazonSocial());
        this.infoFactura.setIdentificacionComprador(invCliente.getCliIdNumero() == null ? "9999999999999" : invCliente.getCliIdNumero());
        this.infoFactura.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(
                invVentas.getVtaSubtotalBase0().add(invVentas.getVtaSubtotalBaseImponible()), 2,
                java.math.RoundingMode.HALF_UP));
        this.infoFactura.setTotalDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaDescuentoBase0()
                .add(invVentas.getVtaDescuentoBaseImponible()).add(invVentas.getVtaDescuentoBaseExenta()).// getVtaDescuentoGeneralBase0
                add(invVentas.getVtaDescuentoBaseNoObjeto())).add(cero));
        this.infoFactura.setTotalConImpuestos(generaTotalesImpuestoFactura(
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP),
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBaseImponible(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP)));
        this.infoFactura.setPropina(cero);
        this.infoFactura.setImporteTotal(invVentas.getVtaTotal());
        this.infoFactura.setMoneda("DOLAR");
        if (invVentas.getVtaPagadoEfectivo().compareTo(cero) != 0
                || invVentas.getVtaPagadoDineroElectronico().compareTo(cero) != 0
                || invVentas.getVtaPagadoTarjetaCredito().compareTo(cero) != 0
                || invVentas.getVtaPagadoOtro().compareTo(cero) != 0) {
            this.infoFactura.setPagos(generarPagosFactura());
        }
        return error;
    }

    private Factura.InfoFactura.Pagos generarPagosFactura() {
        Factura.InfoFactura.Pagos respuesta = this.facturaFactory.createFacturaPagos();
        Pago pago;
        BigDecimal totalPagado = invVentas.getVtaPagadoEfectivo()
                .add(invVentas.getVtaPagadoDineroElectronico()
                        .add(invVentas.getVtaPagadoTarjetaCredito()
                                .add(invVentas.getVtaPagadoOtro())));
        if (invVentas.getVtaPagadoDineroElectronico().compareTo(cero) != 0) {
            pago = new Factura.InfoFactura.Pagos.Pago();
            pago.setFormaPago("17");
            pago.setTotal(invVentas.getVtaPagadoDineroElectronico());
            respuesta.getPago().add(pago);
            totalPagado.subtract(invVentas.getVtaPagadoDineroElectronico());
        }
        if (invVentas.getVtaPagadoTarjetaCredito().compareTo(cero) != 0) {
            pago = new Factura.InfoFactura.Pagos.Pago();
            pago.setFormaPago("19");
            pago.setTotal(invVentas.getVtaPagadoTarjetaCredito());
            respuesta.getPago().add(pago);
            totalPagado.subtract(invVentas.getVtaPagadoTarjetaCredito());
        }
        if (invVentas.getVtaPagadoOtro().compareTo(cero) != 0) {
            pago = new Factura.InfoFactura.Pagos.Pago();
            pago.setFormaPago("20");
            pago.setTotal(invVentas.getVtaPagadoOtro());
            respuesta.getPago().add(pago);
            totalPagado.subtract(invVentas.getVtaPagadoOtro());
        }
        if (invVentas.getVtaPagadoEfectivo().compareTo(cero) != 0) {
            pago = new Factura.InfoFactura.Pagos.Pago();
            pago.setFormaPago("01");
            pago.setTotal(invVentas.getVtaPagadoEfectivo().subtract(totalPagado));
            respuesta.getPago().add(pago);
        }

        return respuesta;
    }

    private Factura.InfoFactura.TotalConImpuestos generaTotalesImpuestoFactura(java.math.BigDecimal parcialCero,
            java.math.BigDecimal parcialImponible) {
        Factura.InfoFactura.TotalConImpuestos respuesta = this.facturaFactory
                .createFacturaInfoFacturaTotalConImpuestos();
        Factura.InfoFactura.TotalConImpuestos.TotalImpuesto impuesto;
        // iva 0%
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false));
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(cero);
            respuesta.getTotalImpuesto().add(impuesto);
        }
        // iva 12%
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true));
            impuesto.setBaseImponible(parcialImponible.add(invVentas.getVtaMontoIce()));
            impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaMontoiva(), 2, java.math.RoundingMode.HALF_UP));
            respuesta.getTotalImpuesto().add(impuesto);
        }
        //ice
        for (InvListaDetalleVentasTO detalle : listaInvVentasDetalleTO) {
            if (detalle.getDetMontoIce() != null && detalle.getDetMontoIce().compareTo(BigDecimal.ZERO) > 0 && (detalle.getProComplementario() == null || detalle.getProComplementario().equals(""))) {
                //encontrar impuesto existente
                Optional<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto> impuestoExistente = respuesta.getTotalImpuesto().stream()
                        .filter(s -> s.getCodigo().equals("3") && s.getCodigoPorcentaje().equals(detalle.getIceCodigo()))
                        .findFirst();
                impuesto = impuestoExistente.isPresent() ? impuestoExistente.get() : null;
                if (impuesto != null) {
                    impuesto.setBaseImponible(impuesto.getBaseImponible().add(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP)));
                    impuesto.setValor(impuesto.getValor().add(UtilsArchivos.redondeoDecimalBigDecimal(detalle.getDetMontoIce(), 2, java.math.RoundingMode.HALF_UP)));
                } else {
                    impuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
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

    private Factura.Detalles generarDetalleFactura() {
        // List<InvListaDetalleVentasTO> listaInvVentasDetalleTO = null;
        Factura.Detalles resultado = this.facturaFactory.createFacturaDetalles();
        for (InvListaDetalleVentasTO invVentasDetalleTO : listaInvVentasDetalleTO) {
            if (invVentasDetalleTO.getProComplementario() == null || invVentasDetalleTO.getProComplementario().equals("")) {
                Factura.Detalles.Detalle detalle = this.facturaFactory.createFacturaDetallesDetalle();
                detalle.setCodigoPrincipal(invVentasDetalleTO.getCodigoProducto());
                detalle.setDescripcion(invVentasDetalleTO.getNombreProducto());
                BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
                BigDecimal recargo = BigDecimal.ONE.add(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPorcentajeRecargo().divide(new BigDecimal(100)), 15, RoundingMode.HALF_UP));

                //el xml admite max 6 decimales, mas decimales puede "dañar" el resultado por ej. 50 x 44.4841 = 2224.205 pero sin redondear o redondeando a 15 decimales el resultado es 2224.204999999999900
                BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto().multiply(recargo), 6, RoundingMode.HALF_UP);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(UtilsArchivos.redondeoDecimalBigDecimal(precioUnitario, 6, RoundingMode.HALF_UP));
                detalle.setDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto()
                        .multiply(invVentasDetalleTO.getPrecioProducto())
                        .multiply(invVentasDetalleTO.getPorcentajeDescuento()).divide(new java.math.BigDecimal("100"))));
                detalle.setPrecioTotalSinImpuesto(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getDetalleTotal()
                        .subtract(invVentasDetalleTO.getIvaCobrado())
                        .subtract(invVentasDetalleTO.getDetMontoIce())
                        .add(cero), 2, RoundingMode.HALF_UP));

                //Para añadir detalles a cada registro
                Factura.Detalles.Detalle.DetallesAdicionales obj = this.facturaFactory.createFacturaDetallesDetalleDetallesAdicionales();

                if (!emisor.getRuc().contains("0704469998001")) {
                    Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional det = this.facturaFactory.createFacturaDetallesDetalleDetallesAdicionalesDetAdicional();
                    det.setNombre("Medida");
                    det.setValor(invVentasDetalleTO.getMedidaDetalle());
                    obj.getDetAdicional().add(det);
                    detalle.setDetallesAdicionales(obj);
                }
                detalle.setImpuestos(obtenerImpuestosProductoFactura(invVentasDetalleTO));
                resultado.getDetalle().add(detalle);
            }

        }
        return resultado;
    }

    private Factura.Detalles.Detalle.Impuestos obtenerImpuestosProductoFactura(InvListaDetalleVentasTO invVentasDetalleTO) {
        Factura.Detalles.Detalle.Impuestos result = this.facturaFactory.createFacturaDetallesDetalleImpuestos();
        ImpuestoFactura i = new ImpuestoFactura();
        BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
        BigDecimal recargo = BigDecimal.ONE.add(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPorcentajeRecargo().divide(new BigDecimal(100)), 6, RoundingMode.HALF_UP));
        BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto().multiply(recargo), 6, RoundingMode.HALF_UP);
        i.setCodigo("2");
        i.setCodigoPorcentaje(codigoPorcentajeIva(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0));
        i.setTarifa(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0 ? ivaVigente : cero);
        i.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                .add(cero).add(invVentasDetalleTO.getDetMontoIce())));
        i.setValor(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0
                ? UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                        .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento()
                                .divide(new java.math.BigDecimal("100"))))
                        .add(cero).multiply(ivaVigente.divide(new java.math.BigDecimal("100.00"), 2,
                        java.math.RoundingMode.HALF_UP)))
                : cero);
        result.getImpuesto().add(i);
        if (invVentasDetalleTO.getDetMontoIce() != null && invVentasDetalleTO.getDetMontoIce().compareTo(BigDecimal.ZERO) > 0) {
            ImpuestoFactura ice = new ImpuestoFactura();
            ice.setCodigo("3");
            ice.setCodigoPorcentaje(invVentasDetalleTO.getIceCodigo());
            ice.setTarifa(invVentasDetalleTO.getIceTarifaFija() != null ? invVentasDetalleTO.getIceTarifaFija() : cero);
            ice.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP));
            ice.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getDetMontoIce(), 2, RoundingMode.HALF_UP));
            result.getImpuesto().add(ice);
        }
        return result;
    }

    private Factura.InfoAdicional generarInformacionAdicionalFactura() throws Exception {
        Factura.InfoAdicional info = this.facturaFactory.createFacturaInfoAdicional();
        if (this.invCliente.getCliDireccion() != null && !this.invCliente.getCliDireccion().equals("")) {
            if (!this.invCliente.getCliDireccion().equals("-")) {
                if (invVentas != null && invVentas.getCliCodigoEstablecimiento() != null && !invVentas.getCliCodigoEstablecimiento().equals("")
                        && invCliente.getCliCodigoEstablecimiento() != null && !invCliente.getCliCodigoEstablecimiento().equals("")
                        && !invCliente.getCliCodigoEstablecimiento().equals(invVentas.getCliCodigoEstablecimiento())) {
//                    InvClientesDirecciones direccion = clientesDireccionesService.obtenerDireccion(invCliente.getInvClientePK().getCliEmpresa(),
//                            invVentas.getCliCodigoEstablecimiento(), invCliente.getInvClientePK().getCliCodigo());
                    if (direccion != null && !direccion.equals("")) {
                        Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                        detalle.setNombre("Dirección");
                        detalle.setValue((String) direccion);
                        info.getCampoAdicional().add(detalle);
                    } else {
                        Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                        detalle.setNombre("Dirección");
                        detalle.setValue((String) this.invCliente.getCliDireccion());
                        info.getCampoAdicional().add(detalle);
                    }
                } else {
                    Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                    detalle.setNombre("Dirección");
                    detalle.setValue((String) this.invCliente.getCliDireccion());
                    info.getCampoAdicional().add(detalle);
                }
            }
        }
        if (this.invCliente.getCliTelefono() != null) {
            if (!this.invCliente.getCliTelefono().equals("")) {
                Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                detalle.setNombre("Teléfono");
                detalle.setValue((String) this.invCliente.getCliTelefono());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invCliente.getCliEmail() != null) {
            if (!this.invCliente.getCliEmail().equals("")) {
                Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                detalle.setNombre("eMail de Notificacion");
                detalle.setValue((String) this.invCliente.getCliEmail());
                info.getCampoAdicional().add(detalle);
            }
        }

        if ((this.invVentas.getInvVentasPK().getVtaEmpresa() != null
                && !this.invVentas.getInvVentasPK().getVtaEmpresa().equals(""))
                && (this.invVentas.getInvVentasPK().getVtaPeriodo() != null
                && !this.invVentas.getInvVentasPK().getVtaPeriodo().equals(""))
                && (this.invVentas.getInvVentasPK().getVtaMotivo() != null
                && !this.invVentas.getInvVentasPK().getVtaMotivo().equals(""))
                && (this.invVentas.getInvVentasPK().getVtaNumero() != null
                && !this.invVentas.getInvVentasPK().getVtaNumero().equals(""))) {
            Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
            detalle.setNombre("Clave Interna");
            detalle.setValue(this.invVentas.getInvVentasPK().getVtaEmpresa() + " | "
                    + this.invVentas.getInvVentasPK().getVtaPeriodo() + " | "
                    + this.invVentas.getInvVentasPK().getVtaMotivo() + " | "
                    + this.invVentas.getInvVentasPK().getVtaNumero() + " | "
                    + this.invVentas.getUsrCodigo());
            info.getCampoAdicional().add(detalle);
        }

        if (this.emisor.getParWebDocumentosElectronicos() != null
                && !this.emisor.getParWebDocumentosElectronicos().equals("")) {
            Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) this.emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }

        if (invVentas.getVtaInformacionAdicional() != null) {
            java.util.List<String> informacionAdicional = UtilsArchivos.separar(invVentas.getVtaInformacionAdicional(), "|");
            for (int i = 0; i < informacionAdicional.size(); i++) {
                if (!informacionAdicional.get(i).equals("|")
                        && informacionAdicional.get(i).compareTo("") > 0
                        && informacionAdicional.get(i).lastIndexOf("=") >= 0) {
                    Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
                    detalle.setNombre(informacionAdicional.get(i).substring(0, informacionAdicional.get(i).lastIndexOf("=")));
                    detalle.setValue(informacionAdicional.get(i).substring(informacionAdicional.get(i).lastIndexOf("=") + 1));
                    info.getCampoAdicional().add(detalle);
                }
            }
        }

        if (this.invVentas.getVtaFormaPago().equalsIgnoreCase("POR PAGAR")
                && (this.emisor.getRuc().equalsIgnoreCase("0993013447001")
                || this.emisor.getRuc().equalsIgnoreCase("0993046590001")
                || this.emisor.getRuc().equalsIgnoreCase("0992879254001")
                || this.emisor.getRuc().equalsIgnoreCase("0791702070001") //NET2
                || this.emisor.getRuc().equalsIgnoreCase("0791702054001") //NET3
                || this.emisor.getRuc().equalsIgnoreCase("0791755093001") //NET1
                || this.emisor.getRuc().equalsIgnoreCase("0993176907001"))) {
            //SOLO SI ES "MARAQUATIK" o "BALANCEADOS DEL PACIFICO BAPACIF SA" o "COPACIGULF o "INSUMOS DEL MAR MARAQUATIK"
            Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
            detalle.setNombre("Fecha Vencimiento");
            detalle.setValue(UtilsArchivos.fecha(this.invVentas.getVtaFechaVencimiento(), "yyyy-MM-dd"));
            info.getCampoAdicional().add(detalle);
        }

        if (this.invVentas.getVtaFormaPago().equalsIgnoreCase("POR PAGAR")
                && (this.emisor.getRuc().equalsIgnoreCase("0992879254001"))) {
            //SOLO SI ES "COPACIGULF"
            Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
            detalle.setNombre("Observaciones");
            detalle.setValue(this.invVentas.getVtaObservaciones());
            info.getCampoAdicional().add(detalle);
        }

        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
            Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
            detalle.setNombre("Régimen");
            detalle.setValue("Contribuyente régimen RIMPE");
            info.getCampoAdicional().add(detalle);
        }
        return info;
    }

    private String codigoPorcentajeIva(boolean llevaIva) {
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

    private Factura.InfoSustitutivaGuiaRemision generarSustitutivaGuia() {
        Factura.InfoSustitutivaGuiaRemision infoSustitutivaGuiaRemision = new Factura.InfoSustitutivaGuiaRemision();
        infoSustitutivaGuiaRemision.setDirPartida(this.invVentaGuiaRemision.getGuiaPuntoPartida());
        infoSustitutivaGuiaRemision.setDirDestinatario(this.invVentaGuiaRemision.getGuiaPuntoLlegada());
        infoSustitutivaGuiaRemision.setFechaIniTransporte(UtilsArchivos.fecha(invVentaGuiaRemision.getGuiaFechaInicioTransporte(), "dd/MM/yyyy"));
        infoSustitutivaGuiaRemision.setFechaFinTransporte(UtilsArchivos.fecha(invVentaGuiaRemision.getGuiaFechaFinTransporte(), "dd/MM/yyyy"));
        infoSustitutivaGuiaRemision.setRazonSocialTransportista(invVentaGuiaRemision.getInvTransportista().getTransNombres());

        Character tipoTransportista = invVentaGuiaRemision.getInvTransportista().getTransIdTipo();
        switch (tipoTransportista) {
            case 'R':
                infoSustitutivaGuiaRemision.setTipoIdentificacionTransportista("04");
                break;
            case 'C':
                infoSustitutivaGuiaRemision.setTipoIdentificacionTransportista("05");
                break;
            case 'P':
                infoSustitutivaGuiaRemision.setTipoIdentificacionTransportista("06");
                break;
        }
        infoSustitutivaGuiaRemision.setRucTransportista(invVentaGuiaRemision.getInvTransportista().getTransIdNumero());
        infoSustitutivaGuiaRemision.setPlaca(invVentaGuiaRemision.getGuiaPlaca());

        Factura.InfoSustitutivaGuiaRemision.Destinos destinos = new Factura.InfoSustitutivaGuiaRemision.Destinos();
        Factura.InfoSustitutivaGuiaRemision.Destinos.Destino destino = new Factura.InfoSustitutivaGuiaRemision.Destinos.Destino();
        destino.setCodEstabDestino(invVentaGuiaRemision.getGuiaCodigoEstablecimiento());
        destino.setMotivoTraslado(invVentaGuiaRemision.getGuiaMotivoTraslado());
        if (invVentaGuiaRemision.getGuiaRuta() != null && !invVentaGuiaRemision.getGuiaRuta().equals("")) {
            destino.setRuta(invVentaGuiaRemision.getGuiaRuta());
        }

        destinos.getDestino().add(destino);
        infoSustitutivaGuiaRemision.setDestinos(destinos);
        return infoSustitutivaGuiaRemision;
    }

    //FACTURA REEMBOLSO
    public FacturaReembolso generarComprobanteFacturaReembolso(InvVentas invVentas, InvCliente invCliente,
            List<InvListaDetalleVentasTO> listaInvVentasDetalleTO, String claveDeAcceso, Emisor emisor,
            String codigoTipoTransaccion, InvVentaGuiaRemision guia, String direccion, List<AnxVentaReembolsoTO> listaReembolso,
            SisEmpresaParametros sisEmpresaParametros) throws Exception {
        this.invVentas = invVentas;
        this.invCliente = invCliente;
        this.listaInvVentasDetalleTO = listaInvVentasDetalleTO;
        this.claveDeAcceso = claveDeAcceso;
        this.emisor = emisor;
        this.codigoTipoTransaccion = codigoTipoTransaccion;
        this.facturaFactory = new ObjectFactoryFactura();
        this.invVentaGuiaRemision = guia;
        this.sisEmpresaParametros = sisEmpresaParametros;
        this.direccion = direccion;
        this.ivaVigente = invVentas.getVtaIvaVigente();
        FacturaReembolso facturaReembolso = null;
        if (!llenarObjetoComprobanteElectronicoFacturaReembolso()) {
            FacturaReembolso.Detalles detalles = generarDetalleFacturaReembolso();
            Factura.InfoAdicional informacion = generarInformacionAdicionalFactura();
            facturaReembolso = new FacturaReembolso();
            facturaReembolso.setInfoTributaria(this.infoTributaria);
            facturaReembolso.setInfoFactura(this.infoFactura);
            if (detalles != null) {
                facturaReembolso.setDetalles(detalles);
            }
            if (informacion.getCampoAdicional().size() > 0) {
                facturaReembolso.setInfoAdicional(informacion);
            }
            facturaReembolso.setVersion("1.0.0");

            facturaReembolso.setId("comprobante");
        }
        return facturaReembolso;
    }

    private FacturaReembolso.Detalles generarDetalleFacturaReembolso() {
        FacturaReembolso.Detalles resultado = new FacturaReembolso.Detalles();
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
                        .add(cero), 2, RoundingMode.HALF_UP));
                //Para añadir detalles a cada registro
                FacturaReembolso.Detalles.Detalle.DetallesAdicionales obj = new FacturaReembolso.Detalles.Detalle.DetallesAdicionales();
                if (!emisor.getRuc().contains("0704469998001")) {
                    FacturaReembolso.Detalles.Detalle.DetallesAdicionales.DetAdicional det = new FacturaReembolso.Detalles.Detalle.DetallesAdicionales.DetAdicional();
                    det.setNombre("Medida");
                    det.setValor(invVentasDetalleTO.getMedidaDetalle());
                    obj.getDetAdicional().add(det);
                    detalle.setDetallesAdicionales(obj);
                }
                detalle.setImpuestos(obtenerImpuestosProductoFacturaReembolso(invVentasDetalleTO));
                resultado.getDetalle().add(detalle);
            }
        }
        return resultado;
    }

    private FacturaReembolso.Reembolsos generarReembolsos(List<AnxVentaReembolsoTO> listaReembolso) {
        FacturaReembolso.Reembolsos resultado = new FacturaReembolso.Reembolsos();
        for (AnxVentaReembolsoTO reembolso : listaReembolso) {
            FacturaReembolso.Reembolsos.ReembolsoDetalle detalle = new FacturaReembolso.Reembolsos.ReembolsoDetalle();

            String tipoProveedor = reembolso.getProvIdTipo();
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
            }
            detalle.setIdentificacionProveedorReembolso(reembolso.getProvIdNumero());
            detalle.setCodPaisPagoProveedorReembolso("593");
            detalle.setTipoProveedorReembolso("");//01 o 02
            detalle.setCodDocReembolso(reembolso.getReembDocumentoTipo());
            detalle.setEstabDocReembolso(reembolso.getReembDocumentoNumero().substring(0, 3));
            detalle.setPtoEmiDocReembolso(reembolso.getReembDocumentoNumero().substring(4, 7));
            detalle.setSecuencialDocReembolso(reembolso.getReembDocumentoNumero().substring(8));
            detalle.setNumeroautorizacionDocReemb(reembolso.getReembAutorizacion());
            detalle.setFechaEmisionDocReembolso(UtilsArchivos.fecha(reembolso.getReembFechaemision(), "yyyy-MM-dd", "dd/MM/yyyy"));
            detalle.setDetalleImpuestos(obtenerImpuestosReembolsoDetalle(detalle));

            //
            resultado.getReembolsoDetalle().add(detalle);

        }
        return resultado;
    }

    private FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos obtenerImpuestosReembolsoDetalle(FacturaReembolso.Reembolsos.ReembolsoDetalle detalle) {
        FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos result = new FacturaReembolso.Reembolsos.ReembolsoDetalle.DetalleImpuestos();

        return result;
    }

    private FacturaReembolso.Detalles.Detalle.Impuestos obtenerImpuestosProductoFacturaReembolso(InvListaDetalleVentasTO invVentasDetalleTO) {
        FacturaReembolso.Detalles.Detalle.Impuestos result = new FacturaReembolso.Detalles.Detalle.Impuestos();
        ImpuestoFactura i = new ImpuestoFactura();
        BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
        BigDecimal recargo = BigDecimal.ONE.add(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPorcentajeRecargo().divide(new BigDecimal(100)), 6, RoundingMode.HALF_UP));
        BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto().multiply(recargo), 6, RoundingMode.HALF_UP);
        i.setCodigo("2");
        i.setCodigoPorcentaje(codigoPorcentajeIva(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0));
        i.setTarifa(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0 ? ivaVigente : cero);
        i.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                .add(cero).add(invVentasDetalleTO.getDetMontoIce())));
        i.setValor(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0
                ? UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                        .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento()
                                .divide(new java.math.BigDecimal("100"))))
                        .add(cero).multiply(ivaVigente.divide(new java.math.BigDecimal("100.00"), 2,
                        java.math.RoundingMode.HALF_UP)))
                : cero);
        result.getImpuesto().add(i);
        if (invVentasDetalleTO.getDetMontoIce() != null && invVentasDetalleTO.getDetMontoIce().compareTo(BigDecimal.ZERO) > 0) {
            ImpuestoFactura ice = new ImpuestoFactura();
            ice.setCodigo("3");
            ice.setCodigoPorcentaje(invVentasDetalleTO.getIceCodigo());
            ice.setTarifa(invVentasDetalleTO.getIceTarifaFija() != null ? invVentasDetalleTO.getIceTarifaFija() : cero);
            ice.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getParcialProducto(), 2, java.math.RoundingMode.HALF_UP));
            ice.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getDetMontoIce(), 2, RoundingMode.HALF_UP));
            result.getImpuesto().add(ice);
        }
        return result;
    }

    private boolean llenarObjetoComprobanteElectronicoFacturaReembolso() {
        boolean error = false;
        this.infoTributaria = new InfoTributaria();
        // ************************<infoTributaria>**********************
        this.infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(emisor.getRazonSocial());
        this.infoTributaria.setNombreComercial(emisor.getNombreComercial());
        this.infoTributaria.setRuc(emisor.getRuc());
        if (this.claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(this.claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.FACTURA.getCode());
        this.infoTributaria.setEstab(invVentas.getVtaDocumentoNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(invVentas.getVtaDocumentoNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(invVentas.getVtaDocumentoNumero().substring(8));
        this.infoTributaria.setDirMatriz(emisor.getDirEstablecimiento());
        if (this.sisEmpresaParametros.getParAgenteRetencion() != null && !this.sisEmpresaParametros.getParAgenteRetencion().equals("")) {
            this.infoTributaria.setAgenteRetencion(sisEmpresaParametros.getParAgenteRetencion());
        }
        // ************************<infoFactura>**********************
        this.infoFacturaReembolso = new FacturaReembolso.InfoFactura();
        this.infoFacturaReembolso.setFechaEmision(UtilsArchivos.fecha(invVentas.getVtaFecha(), "dd/MM/yyyy"));
        this.infoFacturaReembolso.setDirEstablecimiento(emisor.getDirEstablecimiento());
        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            this.infoFacturaReembolso.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        this.infoFacturaReembolso.setObligadoContabilidad(emisor.getLlevaContabilidad());
        this.infoFacturaReembolso.setTipoIdentificacionComprador(codigoTipoTransaccion);
        this.infoFacturaReembolso.setRazonSocialComprador(invCliente.getCliRazonSocial());
        this.infoFacturaReembolso.setIdentificacionComprador(invCliente.getCliIdNumero() == null ? "9999999999999" : invCliente.getCliIdNumero());
        if (invCliente.getCliDireccion() != null && !invCliente.getCliDireccion().equals("")) {
            this.infoFacturaReembolso.setDireccionComprador(invCliente.getCliDireccion());//campo nuevo  
        }
        this.infoFacturaReembolso.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0().add(invVentas.getVtaSubtotalBaseImponible()), 2, java.math.RoundingMode.HALF_UP));
        this.infoFacturaReembolso.setTotalDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaDescuentoBase0()
                .add(invVentas.getVtaDescuentoBaseImponible()).add(invVentas.getVtaDescuentoBaseExenta()).// getVtaDescuentoGeneralBase0
                add(invVentas.getVtaDescuentoBaseNoObjeto())).add(cero));
        //campo nuevo REEMBOLSO
        this.infoFacturaReembolso.setCodDocReemb("41");
        this.infoFacturaReembolso.setTotalComprobantesReembolso(BigDecimal.ZERO);
        this.infoFacturaReembolso.setTotalBaseImponibleReembolso(BigDecimal.ZERO);
        this.infoFacturaReembolso.setTotalImpuestoReembolso(BigDecimal.ZERO);
        //******
        this.infoFacturaReembolso.setTotalConImpuesto(generaTotalesImpuestoFacturaReembolso(
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP),
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBaseImponible(), 2, // el xml no admite mas decimales
                        java.math.RoundingMode.HALF_UP)));
        this.infoFacturaReembolso.setPropina(cero);
        this.infoFacturaReembolso.setImporteTotal(invVentas.getVtaTotal());
        this.infoFacturaReembolso.setMoneda("DOLAR");
        if (invVentas.getVtaPagadoEfectivo().compareTo(cero) != 0
                || invVentas.getVtaPagadoDineroElectronico().compareTo(cero) != 0
                || invVentas.getVtaPagadoTarjetaCredito().compareTo(cero) != 0
                || invVentas.getVtaPagadoOtro().compareTo(cero) != 0) {
            //pagos
            FacturaReembolso.InfoFactura.Pagos respuesta = new FacturaReembolso.InfoFactura.Pagos();
            FacturaReembolso.InfoFactura.Pagos.Pago pago;
            BigDecimal totalPagado = invVentas.getVtaPagadoEfectivo().add(invVentas.getVtaPagadoDineroElectronico().add(invVentas.getVtaPagadoTarjetaCredito().add(invVentas.getVtaPagadoOtro())));
            if (invVentas.getVtaPagadoDineroElectronico().compareTo(cero) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("17");
                pago.setTotal(invVentas.getVtaPagadoDineroElectronico());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoDineroElectronico());
            }
            if (invVentas.getVtaPagadoTarjetaCredito().compareTo(cero) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("19");
                pago.setTotal(invVentas.getVtaPagadoTarjetaCredito());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoTarjetaCredito());
            }
            if (invVentas.getVtaPagadoOtro().compareTo(cero) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("20");
                pago.setTotal(invVentas.getVtaPagadoOtro());
                respuesta.getPago().add(pago);
                totalPagado.subtract(invVentas.getVtaPagadoOtro());
            }
            if (invVentas.getVtaPagadoEfectivo().compareTo(cero) != 0) {
                pago = new FacturaReembolso.InfoFactura.Pagos.Pago();
                pago.setFormaPago("01");
                pago.setTotal(invVentas.getVtaPagadoEfectivo().subtract(totalPagado));
                respuesta.getPago().add(pago);
            }
            this.infoFacturaReembolso.setPagos(respuesta);
        }
        return error;
    }

    private FacturaReembolso.InfoFactura.TotalConImpuesto generaTotalesImpuestoFacturaReembolso(java.math.BigDecimal parcialCero, java.math.BigDecimal parcialImponible) {
        FacturaReembolso.InfoFactura.TotalConImpuesto respuesta = new FacturaReembolso.InfoFactura.TotalConImpuesto();
        FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto impuesto;
        // iva 0%
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false));
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(cero);
            respuesta.getTotalImpuesto().add(impuesto);
        }
        // iva 12%
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = new FacturaReembolso.InfoFactura.TotalConImpuesto.TotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true));
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
}
