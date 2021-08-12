/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util.sri;

import com.acosux.SRIMS.entidades.InvCliente;
import com.acosux.SRIMS.entidades.InvGuiaRemision;
import com.acosux.SRIMS.entidades.InvGuiaRemisionDetalleTO;
import com.acosux.SRIMS.entidades.InvTransportista;
import com.acosux.SRIMS.entidades.TipoComprobanteEnum;
import com.acosux.SRIMS.util.UtilsArchivos;
import com.acosux.SRIMS.util.sri.modelo.Emisor;
import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import com.acosux.SRIMS.util.sri.modelo.guiaremision.GuiaRemision;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author CarolValdiviezo
 */
public class GenerarXMLGuiaRemision {

    private InvCliente invCliente = null;
    private InvTransportista invTransportista = null;
    private InvGuiaRemision invGuiaRemision = null;

    public GuiaRemision generarXMGuiaRemision(InvGuiaRemision invGuiaRemision, InvTransportista invTransportista, InvCliente invCliente, List<InvGuiaRemisionDetalleTO> listInvGuiaRemisionDetalleTO, String claveDeAcceso, Emisor emisor, String fechaVenta, String agenteRetencion) throws Exception {
        this.invCliente = invCliente;
        this.invTransportista = invTransportista;
        this.invGuiaRemision = invGuiaRemision;

        GuiaRemision guiaRemision = null;

        if (claveDeAcceso != null) {
            guiaRemision = new GuiaRemision();
            //INFOTRIBUTARIA
            InfoTributaria infoTributaria = generarInfoTributaria(emisor, claveDeAcceso, agenteRetencion);
            guiaRemision.setInfoTributaria(infoTributaria);
            //INFOGUIAREMISION
            GuiaRemision.InfoGuiaRemision infoGuiaRemision = generarInfoGuiaRemision(emisor);
            guiaRemision.setInfoGuiaRemision(infoGuiaRemision);
            //DESTINATARIOS
            GuiaRemision.Destinatarios destinatarios = new GuiaRemision.Destinatarios();
            GuiaRemision.Destinatarios.Destinatario destinatario = generarDestinatario(listInvGuiaRemisionDetalleTO, emisor.getRuc(), fechaVenta);
            destinatarios.getDestinario().add(destinatario);
            guiaRemision.setDestinatarios(destinatarios);
            //INFOADICIONAL
            GuiaRemision.InfoAdicional informacion = generarInformacionAdicionalGuiaRemision(emisor);
            if (informacion.getCampoAdicional().size() > 0) {
                guiaRemision.setInfoAdicional(informacion);
            }
            guiaRemision.setVersion("1.1.0");
            guiaRemision.setId("comprobante");
        }

        return guiaRemision;
    }

    private InfoTributaria generarInfoTributaria(Emisor emisor, String claveDeAcceso, String agenteRetencion) {
        InfoTributaria infoTributaria = new InfoTributaria();
        infoTributaria.setAmbiente(emisor.getTipoAmbiente());
        infoTributaria.setTipoEmision(emisor.getTipoEmision());
        infoTributaria.setRazonSocial(emisor.getRazonSocial());
        infoTributaria.setNombreComercial(emisor.getNombreComercial());
        infoTributaria.setRuc(emisor.getRuc());
        infoTributaria.setClaveAcceso(claveDeAcceso);
        infoTributaria.setCodDoc(TipoComprobanteEnum.GUIA_DE_REMISION.getCode());
        infoTributaria.setEstab(invGuiaRemision.getGuiaDocumentoNumero().substring(0, 3));
        infoTributaria.setPtoEmi(invGuiaRemision.getGuiaDocumentoNumero().substring(4, 7));
        infoTributaria.setSecuencial(invGuiaRemision.getGuiaDocumentoNumero().substring(8));
        infoTributaria.setDirMatriz(emisor.getDirEstablecimiento());
        if (agenteRetencion != null && !agenteRetencion.equals("")) {
            infoTributaria.setAgenteRetencion(agenteRetencion);
        }
        return infoTributaria;
    }

    public GuiaRemision.InfoGuiaRemision generarInfoGuiaRemision(Emisor emisor) {
        GuiaRemision.InfoGuiaRemision infoGuiaRemision = new GuiaRemision.InfoGuiaRemision();
        infoGuiaRemision.setDirEstablecimiento(emisor.getDirEstablecimiento());
        infoGuiaRemision.setDirPartida(invGuiaRemision.getGuiaPuntoPartida());
        infoGuiaRemision.setRazonSocialTransportista(this.invTransportista.getTransNombres());
        Character tipoTransportista = this.invTransportista.getTransIdTipo();
        switch (tipoTransportista) {
            case 'R':
                infoGuiaRemision.setTipoIdentificacionTransportista("04");
                break;
            case 'C':
                infoGuiaRemision.setTipoIdentificacionTransportista("05");
                break;
            case 'P':
                infoGuiaRemision.setTipoIdentificacionTransportista("06");
                break;
        }
        infoGuiaRemision.setRucTransportista(this.invTransportista.getTransIdNumero());
        infoGuiaRemision.setObligadoContabilidad(emisor.getLlevaContabilidad());
        if (emisor.getContribuyenteEspecial() != null && !emisor.getContribuyenteEspecial().equals("")) {
            infoGuiaRemision.setContribuyenteEspecial(emisor.getContribuyenteEspecial());
        }
        infoGuiaRemision.setFechaIniTransporte(UtilsArchivos.fecha(invGuiaRemision.getGuiaFechaInicioTransporte(), "dd/MM/yyyy"));
        infoGuiaRemision.setFechaFinTransporte(UtilsArchivos.fecha(invGuiaRemision.getGuiaFechaFinTransporte(), "dd/MM/yyyy"));
        infoGuiaRemision.setPlaca(invGuiaRemision.getGuiaPlaca());

        return infoGuiaRemision;
    }

    public GuiaRemision.Destinatarios.Destinatario generarDestinatario(List<InvGuiaRemisionDetalleTO> listInvGuiaRemisionDetalleTO, String rucEmisor, String fechaVenta) {
        GuiaRemision.Destinatarios.Destinatario destinatario = new GuiaRemision.Destinatarios.Destinatario();
        destinatario.setIdentificacionDestinatario(this.invCliente.getCliIdNumero());
        destinatario.setRazonSocialDestinatario(this.invCliente.getCliRazonSocial());
        destinatario.setDirDestinatario(invGuiaRemision.getGuiaPuntoLlegada());
        destinatario.setMotivoTraslado(invGuiaRemision.getGuiaMotivoTraslado());
        if (invGuiaRemision.getGuiaDocumentoAduanero() != null && !invGuiaRemision.getGuiaDocumentoAduanero().equalsIgnoreCase("")) {
            destinatario.setDocAduaneroUnico(invGuiaRemision.getGuiaDocumentoAduanero());
        }
        destinatario.setCodEstabDestino(invGuiaRemision.getGuiaCodigoEstablecimiento());
        if (invGuiaRemision.getGuiaRuta() != null && !invGuiaRemision.getGuiaRuta().equalsIgnoreCase("")) {
            destinatario.setRuta(invGuiaRemision.getGuiaRuta());
        }
        destinatario.setCodDocSustento("01");
        destinatario.setNumDocSustento(invGuiaRemision.getNroDocumento());
        destinatario.setNumAutDocSustento(invGuiaRemision.getNroAutorizacion());
        destinatario.setFechaEmisionDocSustento(fechaVenta);
        //detalles
        GuiaRemision.Destinatarios.Destinatario.Detalles detalles = generarDetalleDestinario(listInvGuiaRemisionDetalleTO, rucEmisor);
        destinatario.setDetalles(detalles);

        return destinatario;
    }

    private GuiaRemision.InfoAdicional generarInformacionAdicionalGuiaRemision(Emisor emisor) {
        GuiaRemision.InfoAdicional info = new GuiaRemision.InfoAdicional();
        if (this.invCliente.getCliDireccion() != null && !this.invCliente.getCliDireccion().equals("")) {
            if (!this.invCliente.getCliDireccion().equals("-")) {
                GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
                detalle.setNombre("Dirección");
                detalle.setValue((String) invCliente.getCliDireccion());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invCliente.getCliTelefono() != null) {
            if (!this.invCliente.getCliTelefono().equals("")) {
                GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
                detalle.setNombre("Teléfono");
                detalle.setValue((String) this.invCliente.getCliTelefono());
                info.getCampoAdicional().add(detalle);
            }
        }
        if (this.invCliente.getCliEmail() != null) {
            if (!this.invCliente.getCliEmail().equals("")) {
                GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
                detalle.setNombre("eMail de Notificacion");
                detalle.setValue((String) this.invCliente.getCliEmail());
                info.getCampoAdicional().add(detalle);
            }
        }

        if ((this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaEmpresa() != null
                && !this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaEmpresa().equals(""))
                && (this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaPeriodo() != null
                && !this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaPeriodo().equals(""))
                && (this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaNumero() != null
                && !this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaNumero().equals(""))) {
            GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
            detalle.setNombre("Clave Interna");
            detalle.setValue(this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaEmpresa() + " | "
                    + this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaPeriodo() + " | "
                    + this.invGuiaRemision.getInvGuiaRemisionPK().getGuiaNumero() + " | "
                    + this.invGuiaRemision.getUsrCodigo());
            info.getCampoAdicional().add(detalle);
        }

        if (emisor.getParWebDocumentosElectronicos() != null && !emisor.getParWebDocumentosElectronicos().equals("")) {
            GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
            detalle.setNombre("Web Descarga");
            detalle.setValue((String) emisor.getParWebDocumentosElectronicos());
            info.getCampoAdicional().add(detalle);
        }

        if (this.invGuiaRemision.getGuiaInformacionAdicional() != null) {
            java.util.List<String> informacionAdicional = UtilsArchivos.separar(this.invGuiaRemision.getGuiaInformacionAdicional(), "|");
            for (int i = 0; i < informacionAdicional.size(); i++) {
                if (!informacionAdicional.get(i).equals("|")
                        && informacionAdicional.get(i).compareTo("") > 0
                        && informacionAdicional.get(i).lastIndexOf("=") >= 0) {
                    GuiaRemision.InfoAdicional.CampoAdicional detalle = new GuiaRemision.InfoAdicional.CampoAdicional();
                    detalle.setNombre(informacionAdicional.get(i).substring(0, informacionAdicional.get(i).lastIndexOf("=")));
                    detalle.setValue(informacionAdicional.get(i).substring(informacionAdicional.get(i).lastIndexOf("=") + 1));
                    info.getCampoAdicional().add(detalle);
                }
            }
        }
        return info;
    }

    private GuiaRemision.Destinatarios.Destinatario.Detalles generarDetalleDestinario(List<InvGuiaRemisionDetalleTO> listInvGuiaRemisionDetalleTO, String rucEmisor) {
        //Detalle
        GuiaRemision.Destinatarios.Destinatario.Detalles resultado = new GuiaRemision.Destinatarios.Destinatario.Detalles();
        for (InvGuiaRemisionDetalleTO invGuiaRemisionDetalleTO : listInvGuiaRemisionDetalleTO) {
            GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle detalle = new GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle();
            detalle.setCodigoInterno(invGuiaRemisionDetalleTO.getProCodigoPrincipal());
            detalle.setDescripcion(invGuiaRemisionDetalleTO.getNombreProducto());
            BigDecimal cantidad = UtilsArchivos.redondeoDecimalBigDecimal(invGuiaRemisionDetalleTO.getDetCantidad(), 6, RoundingMode.HALF_UP);
            detalle.setCantidad(cantidad);
            //Para añadir detalles a cada registro
            if (!rucEmisor.contains("0704469998001")) {
                GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle.DetallesAdicionales obj = new GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle.DetallesAdicionales();
                GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle.DetallesAdicionales.DetAdicional det = new GuiaRemision.Destinatarios.Destinatario.Detalles.Detalle.DetallesAdicionales.DetAdicional();
                det.setNombre("Medida");
                det.setValor(invGuiaRemisionDetalleTO.getMedidaProducto());
                obj.getDetAdicional().add(det);
                detalle.setDetallesAdicionales(obj);
            }
            resultado.getDetalle().add(detalle);
        }
        return resultado;
    }

}
