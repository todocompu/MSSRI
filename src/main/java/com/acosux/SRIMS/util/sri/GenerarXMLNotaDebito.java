package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvListaDetalleVentasTO;
import com.acosux.SRIMS.entidades.InvVentas;
import com.acosux.SRIMS.entidades.SisEmpresaParametros;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.notadebito.ImpuestoNotaDebito;
import com.acosux.SRIMS.util.sri.modelo.notadebito.NotaDebito;
import com.acosux.SRIMS.util.sri.modelo.notadebito.ObjectFactoryNotaDebito;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class GenerarXMLNotaDebito {

    @Autowired

    private InfoTributaria infoTributaria = null;
    private ObjectFactoryNotaDebito notaDebitoFactory = null;
    private NotaDebito.InfoNotaDebito infoNotaDebito = null;

    private InvVentas invVentas = null;
    private InvCliente invCliente = null;
    private List<InvListaDetalleVentasTO> listaInvVentasDetalleTO;
    private String claveDeAcceso;
    private String codigoTipoTransaccion;
    private Emisor emisor = null;
    private java.math.BigDecimal ivaVigente;
    private String numeroComplemento;
    private Date fechaComplemento;
    //private InvVentasComplemento invVentasComplemento;
    private final java.math.BigDecimal cero = new java.math.BigDecimal("0.00");
    private SisEmpresaParametros sisEmpresaParametros;

    public NotaDebito generarComprobanteNotaDebito(InvVentas invVentas, InvCliente invCliente,
            List<InvListaDetalleVentasTO> listaInvVentasDetalleTO, String claveDeAcceso, Emisor emisor,
            String codigoTipoTransaccion, String numeroComplemento, Date fechaComplemento, SisEmpresaParametros sisEmpresaParametros) throws Exception {

        this.invVentas = invVentas;
        this.invCliente = invCliente;
        this.listaInvVentasDetalleTO = listaInvVentasDetalleTO;

        this.claveDeAcceso = claveDeAcceso;
        this.emisor = emisor;
        this.codigoTipoTransaccion = codigoTipoTransaccion;

        // this.parcialCero = parcialCero;
        // this.parcialImponible = parcialImponible;
        // this.totalIva = totalIva;
        //this.invVentasComplemento = invVentasComplemento;
        this.numeroComplemento = numeroComplemento;
        this.fechaComplemento = fechaComplemento;
        this.sisEmpresaParametros = sisEmpresaParametros;
        NotaDebito notaDebito = null;
        this.notaDebitoFactory = new ObjectFactoryNotaDebito();
        this.ivaVigente = invVentas.getVtaIvaVigente();
        if (!llenarObjetoComprobanteElectronicoNotaDebito()) {
            notaDebito = this.notaDebitoFactory.createNotaDebito();

            NotaDebito.InfoNotaDebito.Impuestos impuestos = obtenerImpuestosNotaDebito(
                    UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBase0(), 2, java.math.RoundingMode.HALF_UP),
                    UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaSubtotalBaseImponible(), 2,
                            java.math.RoundingMode.HALF_UP));

            NotaDebito.InfoAdicional informacion = generarInformacionAdicionalNotaDebito();
            if (impuestos != null) {
                notaDebito.setInfoTributaria(this.infoTributaria);
                this.infoNotaDebito.setImpuestos(impuestos);
                notaDebito.setInfoNotaDebito(this.infoNotaDebito);
                if (informacion.getCampoAdicional().size() > 0) {
                    notaDebito.setInfoAdicional(informacion);
                }
                notaDebito.setMotivos(obtenerMotivos());
                notaDebito.setVersion("1.0.0");
                notaDebito.setId("comprobante");

            } else {
                notaDebito = null;
            }
        }

        return notaDebito;
    }

    private boolean llenarObjetoComprobanteElectronicoNotaDebito() {
        boolean error = false;
        this.infoTributaria = new InfoTributaria();
        // <infoTributaria>
        this.infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        this.infoTributaria.setTipoEmision(emisor.getTipoEmision());
        this.infoTributaria.setRazonSocial(emisor.getRazonSocial());
        this.infoTributaria.setNombreComercial(emisor.getNombreComercial());// sisUsuarioEmpresaTO.getEmpRazonSocial()
        this.infoTributaria.setRuc(emisor.getRuc());
        /// clave de acceso falta
        if (this.claveDeAcceso != null) {
            this.infoTributaria.setClaveAcceso(this.claveDeAcceso);
        } else {
            error = true;
        }
        this.infoTributaria.setCodDoc(TipoComprobanteEnum.NOTA_DE_DEBITO.getCode());
        this.infoTributaria.setEstab(invVentas.getVtaDocumentoNumero().substring(0, 3));
        this.infoTributaria.setPtoEmi(invVentas.getVtaDocumentoNumero().substring(4, 7));
        this.infoTributaria.setSecuencial(invVentas.getVtaDocumentoNumero().substring(8));
        this.infoTributaria.setDirMatriz(emisor.getDireccionMatriz());
        if (this.sisEmpresaParametros.isParContribuyenteRegimenMicroempresa()) {
            this.infoTributaria.setRegimenMicroempresas("CONTRIBUYENTE RÉGIMEN MICROEMPRESAS");
        }

        if (this.sisEmpresaParametros.getParAgenteRetencion() != null && !this.sisEmpresaParametros.getParAgenteRetencion().equals("")) {
            this.infoTributaria.setAgenteRetencion(sisEmpresaParametros.getParAgenteRetencion());
        }
        this.infoNotaDebito = this.notaDebitoFactory.createNotaDebitoInfoNotaDebito();
        this.infoNotaDebito
                .setFechaEmision(UtilsArchivos.fecha(invVentas.getVtaFecha().toString(), "yyyy-MM-dd", "dd/MM/yyyy"));
        this.infoNotaDebito.setDirEstablecimiento(emisor.getDirEstablecimiento());
        this.infoNotaDebito.setTipoIdentificacionComprador(codigoTipoTransaccion);
        this.infoNotaDebito.setRazonSocialComprador(invCliente.getCliRazonSocial());
        this.infoNotaDebito.setIdentificacionComprador(invCliente.getCliIdNumero() == null ? "9999999999999" : invCliente.getCliIdNumero());

        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            this.infoNotaDebito.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        this.infoNotaDebito.setObligadoContabilidad(emisor.getLlevaContabilidad());
        this.infoNotaDebito.setCodDocModificado(TipoComprobanteEnum.FACTURA.getCode());/// corregir
        this.infoNotaDebito.setNumDocModificado(numeroComplemento);
        this.infoNotaDebito.setFechaEmisionDocSustento(
                UtilsArchivos.fecha(fechaComplemento.toString(), "yyyy-MM-dd", "dd/MM/yyyy"));
        this.infoNotaDebito.setTotalSinImpuestos(UtilsArchivos.redondeoDecimalBigDecimal(
                invVentas.getVtaSubtotalBase0().add(invVentas.getVtaSubtotalBaseImponible()), 2,
                java.math.RoundingMode.HALF_UP));
        this.infoNotaDebito.setValorTotal(invVentas.getVtaTotal());

        // </infoTributaria>
        return error;
    }

    NotaDebito.InfoNotaDebito.Impuestos obtenerImpuestosNotaDebito(java.math.BigDecimal parcialCero,
            java.math.BigDecimal parcialImponible) {
        NotaDebito.InfoNotaDebito.Impuestos resultado = this.notaDebitoFactory
                .createNotaDebitoInfoNotaDebitoImpuestos();
        ImpuestoNotaDebito impuesto;
        if (parcialCero.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = this.notaDebitoFactory.createImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(false));
            impuesto.setTarifa(cero);
            impuesto.setBaseImponible(parcialCero);
            impuesto.setValor(cero);
            resultado.getImpuesto().add(impuesto);
        }
        if (parcialImponible.compareTo(BigDecimal.ZERO) > 0) {
            impuesto = this.notaDebitoFactory.createImpuesto();
            impuesto.setCodigo("2");
            impuesto.setCodigoPorcentaje(codigoPorcentajeIva(true));
            impuesto.setTarifa(ivaVigente);
            impuesto.setBaseImponible(parcialImponible);
            impuesto.setValor(UtilsArchivos.redondeoDecimalBigDecimal(invVentas.getVtaMontoiva(), 2,
                    java.math.RoundingMode.HALF_UP));
            resultado.getImpuesto().add(impuesto);
        }
        return resultado;
    }

    private NotaDebito.InfoAdicional generarInformacionAdicionalNotaDebito() {
        NotaDebito.InfoAdicional info = this.notaDebitoFactory.createNotaDebitoInfoAdicional();

        if (this.invCliente.getCliDireccion() != null && !this.invCliente.getCliDireccion().equals("")) {
            if (!this.invCliente.getCliDireccion().equals("-")) {
                NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
                detalle.setNombre("Dirección");
                detalle.setValue((String) this.invCliente.getCliDireccion());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invCliente.getCliTelefono() != null && !this.invCliente.getCliTelefono().equals("")) {
            NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
            detalle.setNombre("Teléfono");
            detalle.setValue((String) this.invCliente.getCliTelefono());
            info.getCampoAdicional().add(detalle);
        }
        if (this.invCliente.getCliEmail() != null && !this.invCliente.getCliEmail().equals("")) {
            NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
            detalle.setNombre("eMail de Notificacion");
            detalle.setValue((String) this.invCliente.getCliEmail());
            info.getCampoAdicional().add(detalle);
        }
        if (invVentas.getVtaInformacionAdicional() != null) {
            java.util.List<String> informacionAdicional = UtilsArchivos.separar(invVentas.getVtaInformacionAdicional(),
                    "|");
            for (int i = 0; i < informacionAdicional.size(); i++) {
                if (i <= 4 && !informacionAdicional.get(i).equals("|")
                        && informacionAdicional.get(i).compareTo("") > 0) {
                    NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
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
            NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
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
            NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) this.emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }
        return info;
    }

    private NotaDebito.Motivos obtenerMotivos() {
        NotaDebito.Motivos resultado = this.notaDebitoFactory.createNotaDebitoMotivos();
        for (InvListaDetalleVentasTO invVentasDetalleTO : listaInvVentasDetalleTO) {
            if (invVentasDetalleTO.getProComplementario() == null || invVentasDetalleTO.getProComplementario().equals("")) {
                BigDecimal valor = invVentasDetalleTO.getCantidadProducto().multiply(invVentasDetalleTO.getPrecioProducto())
                        .multiply(BigDecimal.ONE.subtract(
                                invVentasDetalleTO.getPorcentajeDescuento().divide(new java.math.BigDecimal("100"))))
                        .add(cero);
                NotaDebito.Motivos.Motivo mot = this.notaDebitoFactory.createNotaDebitoMotivosMotivo();
                mot.setRazon(invVentasDetalleTO.getNombreProducto());
                mot.setValor(UtilsArchivos.redondeoDecimalBigDecimal(valor.add(cero)));
                resultado.getMotivo().add(mot);
            }
        }
        return resultado;
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
