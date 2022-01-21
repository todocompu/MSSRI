package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvListaDetalleVentasTO;
import com.acosux.SRIMS.entidades.InvVentas;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.notadecredito.ImpuestoNotaCredito;
import com.acosux.SRIMS.util.sri.modelo.notadecredito.NotaCredito;
import com.acosux.SRIMS.util.sri.modelo.notadecredito.ObjectFactoryNotaCredito;
import com.acosux.SRIMS.util.sri.modelo.notadecredito.TotalConImpuestos;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

public class GenerarXMLNotaCredito {

    private ObjectFactoryNotaCredito notaCreditoFactory = null;
    private NotaCredito.InfoNotaCredito infoNotaCredito = null;
    private InfoTributaria infoTributaria = null;
    private InvVentas invVentas = null;
    private InvCliente invCliente = null;
    private List<InvListaDetalleVentasTO> listaInvVentasDetalleTO;
    private String claveDeAcceso;
    private String tipoIdentificacion;
    private Emisor emisor = null;
    private java.math.BigDecimal ivaVigente;
    private String numeroComplemento;
    private Date fechaComplemento;
    private final java.math.BigDecimal cero = new java.math.BigDecimal("0.00");
    private SisEmpresaParametros sisEmpresaParametros;

    public NotaCredito generarComprobanteNotaCredito(InvVentas invVentas, InvCliente invCliente,
            List<InvListaDetalleVentasTO> listaInvVentasDetalleTO, String claveDeAcceso, Emisor emisor,
            String tipoIdentificacion, String numeroComplemento, Date fechaComplemento, SisEmpresaParametros sisEmpresaParametros) throws Exception {

        this.invVentas = invVentas;
        this.invCliente = invCliente;
        this.listaInvVentasDetalleTO = listaInvVentasDetalleTO;
        this.claveDeAcceso = claveDeAcceso;
        this.emisor = emisor;
        this.tipoIdentificacion = tipoIdentificacion;
        // this.invVentasComplemento =invVentasComplemento;
        this.numeroComplemento = numeroComplemento;
        this.fechaComplemento = fechaComplemento;
        NotaCredito notaCredito = null;
        this.notaCreditoFactory = new ObjectFactoryNotaCredito();
        this.sisEmpresaParametros = sisEmpresaParametros;

        this.ivaVigente = invVentas.getVtaIvaVigente();
        if (!llenarObjetoComprobanteElectronicoNotaCredito()) {
            notaCredito = this.notaCreditoFactory.createNotaCredito();
            NotaCredito.InfoAdicional informacion = generarInformacionAdicionalNotaCredito();
            notaCredito.setInfoTributaria(this.infoTributaria);
            notaCredito.setInfoNotaCredito(this.infoNotaCredito);
            notaCredito.setDetalles(generarDetalleNotaCredito());
            if (informacion.getCampoAdicional().size() > 0) {
                notaCredito.setInfoAdicional(informacion);
            }
            notaCredito.setVersion("1.1.0");
            notaCredito.setId("comprobante");
        }

        return notaCredito;
    }

    private boolean llenarObjetoComprobanteElectronicoNotaCredito() {
        boolean error = false;
        this.infoTributaria = new InfoTributaria();
        // <infoTributaria>
        this.infoTributaria.setAmbiente(this.emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(this.emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(this.emisor.getRazonSocial());
        this.infoTributaria.setNombreComercial(this.emisor.getNombreComercial());

        this.infoTributaria.setRuc(this.emisor.getRuc());
        /// clave de acceso falta
        if (this.claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(this.claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.NOTA_DE_CREDITO.getCode());
        this.infoTributaria.setEstab(invVentas.getVtaDocumentoNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(invVentas.getVtaDocumentoNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(invVentas.getVtaDocumentoNumero().substring(8));
        this.infoTributaria.setDirMatriz(this.emisor.getDireccionMatriz());
        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
//            this.infoTributaria.setContribuyenteRimpe("CONTRIBUYENTE RÉGIMEN RIMPE");
        }

        if (this.sisEmpresaParametros.getParAgenteRetencion() != null && !this.sisEmpresaParametros.getParAgenteRetencion().equals("")) {
            this.infoTributaria.setAgenteRetencion(sisEmpresaParametros.getParAgenteRetencion());
        }

        this.infoNotaCredito = this.notaCreditoFactory.createNotaCreditoInfoNotaCredito();
        this.infoNotaCredito.setFechaEmision(UtilsArchivos.fecha(invVentas.getVtaFecha(), "dd/MM/yyyy"));
        this.infoNotaCredito.setDirEstablecimiento(this.emisor.getDirEstablecimiento());
        this.infoNotaCredito.setTipoIdentificacionComprador(tipoIdentificacion);
        this.infoNotaCredito.setRazonSocialComprador(invCliente.getCliRazonSocial());
        this.infoNotaCredito.setIdentificacionComprador(invCliente.getCliIdNumero() == null ? "9999999999999" : invCliente.getCliIdNumero());

        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            this.infoNotaCredito.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        this.infoNotaCredito.setObligadoContabilidad(emisor.getLlevaContabilidad());
        this.infoNotaCredito.setCodDocModificado(TipoComprobanteEnum.FACTURA.getCode());
        this.infoNotaCredito.setNumDocModificado(numeroComplemento);// invVentasComplemento

        this.infoNotaCredito.setFechaEmisionDocSustento(UtilsArchivos.fecha(fechaComplemento, "dd/MM/yyyy"));
        this.infoNotaCredito.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(
                invVentas.getVtaSubtotalBase0().add(invVentas.getVtaSubtotalBaseImponible()), 2,
                java.math.RoundingMode.HALF_UP));
        this.infoNotaCredito.setValorModificacion(invVentas.getVtaTotal());
        this.infoNotaCredito.setMoneda("DOLAR");
        this.infoNotaCredito.setTotalConImpuestos(generaTotalesImpuestoNotaCredito(
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0(), 2,
                        java.math.RoundingMode.HALF_UP),
                UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBaseImponible(), 2,
                        java.math.RoundingMode.HALF_UP)));
        this.infoNotaCredito.setMotivo(invVentas.getVtaObservaciones().length() > 250
                ? invVentas.getVtaObservaciones().substring(0, 250) : invVentas.getVtaObservaciones());

        return error;
    }

    private TotalConImpuestos generaTotalesImpuestoNotaCredito(java.math.BigDecimal parcialCero,
            java.math.BigDecimal parcialImponible) {
        TotalConImpuestos respuesta = this.notaCreditoFactory.createTotalConImpuestos();
        TotalConImpuestos.TotalImpuesto impuesto;
        // iva 0% -> codigoPorcentaje 0 segun la tabla 16 del manueal codigo 2
        // segun la tabla 16
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = this.notaCreditoFactory.createTotalConImpuestosTotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false));
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(cero);
            respuesta.getTotalImpuesto().add(impuesto);
        }
        // iva 12% -> codigoPorcentaje 2 segun la tabla 16 del manueal codigo 2
        // segun la tabla 16
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = this.notaCreditoFactory.createTotalConImpuestosTotalImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true));
            impuesto.setBaseImponible(parcialImponible);
            impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaMontoiva(), 2,
                    java.math.RoundingMode.HALF_UP));
            respuesta.getTotalImpuesto().add(impuesto);
        }
        return respuesta;
    }

    private NotaCredito.InfoAdicional generarInformacionAdicionalNotaCredito() {
        NotaCredito.InfoAdicional info = this.notaCreditoFactory.createNotaCreditoInfoAdicional();
        if (this.invCliente.getCliDireccion() != null && !this.invCliente.getCliDireccion().equals("")) {
            NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
            detalle.setNombre("Dirección");
            detalle.setValue((String) this.invCliente.getCliDireccion());
            info.getCampoAdicional().add(detalle);
        }
        if (this.invCliente.getCliTelefono() != null && !this.invCliente.getCliTelefono().equals("")) {
            NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
            detalle.setNombre("Teléfono");
            detalle.setValue((String) this.invCliente.getCliTelefono());
            info.getCampoAdicional().add(detalle);
        }
        if (this.invCliente.getCliEmail() != null && !this.invCliente.getCliEmail().equals("")) {
            NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
            detalle.setNombre("eMail de Notificacion");
            detalle.setValue((String) this.invCliente.getCliEmail());
            info.getCampoAdicional().add(detalle);
        }
        if (invVentas.getVtaInformacionAdicional() != null) {
            java.util.List<String> informacionAdicional = UtilsArchivos.separar(invVentas.getVtaInformacionAdicional(), "|");
            for (int i = 0; i < informacionAdicional.size(); i++) {
                if (i <= 4 && !informacionAdicional.get(i).equals("|")
                        && informacionAdicional.get(i).compareTo("") > 0) {
                    NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
                    detalle.setNombre(
                            informacionAdicional.get(i).substring(0, informacionAdicional.get(i).lastIndexOf("=")));
                    detalle.setValue(
                            informacionAdicional.get(i).substring(informacionAdicional.get(i).lastIndexOf("=") + 1));
                    info.getCampoAdicional().add(detalle);
                }
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
            NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
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
            NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) this.emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }
        return info;
    }

    private NotaCredito.Detalles generarDetalleNotaCredito() {
        NotaCredito.Detalles resultado = this.notaCreditoFactory.createNotaCreditoDetalles();

        for (InvListaDetalleVentasTO invVentasDetalleTO : listaInvVentasDetalleTO) {
            if (invVentasDetalleTO.getProComplementario() == null || invVentasDetalleTO.getProComplementario().equals("")) {
                NotaCredito.Detalles.Detalle detalle = this.notaCreditoFactory.createNotaCreditoDetallesDetalle();
                detalle.setCodigoInterno(invVentasDetalleTO.getCodigoProducto());
                detalle.setDescripcion(invVentasDetalleTO.getNombreProducto());
                BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
                BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto(), 6, RoundingMode.HALF_UP);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setDescuento(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto()
                        .multiply(invVentasDetalleTO.getPrecioProducto())
                        .multiply(invVentasDetalleTO.getPorcentajeDescuento()).divide(new java.math.BigDecimal("100"))));
                detalle.setPrecioTotalSinImpuesto(UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO
                        .getParcialProducto()
                        .multiply(BigDecimal.ONE.subtract(
                                invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                        .add(cero)));
                detalle.setImpuestos(obtenerImpuestosProductoNotaCredito(invVentasDetalleTO));
                // List listaInfo = prod.getInfoAdicionalList();
                // if (!listaInfo.isEmpty()) {
                NotaCredito.Detalles.Detalle.DetallesAdicionales obj = this.notaCreditoFactory
                        .createNotaCreditoDetallesDetalleDetallesAdicionales();
                // for (InformacionAdicionalProducto item : listaInfo) {
                NotaCredito.Detalles.Detalle.DetallesAdicionales.DetAdicional det = this.notaCreditoFactory
                        .createNotaCreditoDetallesDetalleDetallesAdicionalesDetAdicional();
                det.setNombre(invVentasDetalleTO.getMedidaDetalle());
                det.setValor("Medida");
                obj.getDetAdicional().add(det);
                // }
                detalle.setDetallesAdicionales(obj);
                // }
                resultado.getDetalle().add(detalle);
            }
        }
        return resultado;
    }

    private NotaCredito.Detalles.Detalle.Impuestos obtenerImpuestosProductoNotaCredito(
            InvListaDetalleVentasTO invVentasDetalleTO) {
        NotaCredito.Detalles.Detalle.Impuestos result = this.notaCreditoFactory
                .createNotaCreditoDetallesDetalleImpuestos();
        ImpuestoNotaCredito i = new ImpuestoNotaCredito();
        BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getCantidadProducto(), 6, RoundingMode.HALF_UP);
        BigDecimal precioUnitario = UtilsArchivos.redondeoDecimalBigDecimal(invVentasDetalleTO.getPrecioProducto(), 6, RoundingMode.HALF_UP);
        i.setCodigo("2");
        i.setCodigoPorcentaje(codigoPorcentajeIva(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0));
        i.setTarifa(invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0 ? ivaVigente : cero);
        i.setBaseImponible(UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                .multiply(BigDecimal.ONE
                        .subtract(invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                .add(cero)));

        i.setValor(
                invVentasDetalleTO.getGravaIva().compareTo("GRAVA") == 0
                ? UtilsArchivos.redondeoDecimalBigDecimal(cantidad.multiply(precioUnitario)
                        .multiply(BigDecimal.ONE.subtract(invVentasDetalleTO.getPorcentajeDescuento()
                                .divide(new java.math.BigDecimal("100"))))
                        .add(cero).multiply(ivaVigente.divide(new java.math.BigDecimal("100.00"), 2,
                        java.math.RoundingMode.HALF_UP)))
                : cero);

        result.getImpuesto().add(i);
        return result;
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

}
