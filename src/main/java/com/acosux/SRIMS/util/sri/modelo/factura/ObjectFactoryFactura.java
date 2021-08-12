package com.acosux.SRIMS.util.sri.modelo.factura;

import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import javax.xml.bind.annotation.XmlRegistry;

/**
 *
 * @author Ing. Mario
 */
@XmlRegistry
public class ObjectFactoryFactura {

    public Factura.Detalles.Detalle.DetallesAdicionales createFacturaDetallesDetalleDetallesAdicionales() {
        return new Factura.Detalles.Detalle.DetallesAdicionales();
    }

    public Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional createFacturaDetallesDetalleDetallesAdicionalesDetAdicional() {
        return new Factura.Detalles.Detalle.DetallesAdicionales.DetAdicional();
    }

    public Factura.Detalles createFacturaDetalles() {
        return new Factura.Detalles();
    }

    public Factura.Detalles.Detalle createFacturaDetallesDetalle() {
        return new Factura.Detalles.Detalle();
    }

    public Factura.InfoFactura createFacturaInfoFactura() {
        return new Factura.InfoFactura();
    }

    public Factura.InfoAdicional createFacturaInfoAdicional() {
        return new Factura.InfoAdicional();
    }

    public ImpuestoFactura createImpuesto() {
        return new ImpuestoFactura();
    }

    public InfoTributaria createInfoTributaria() {
        return new InfoTributaria();
    }

    public Factura createFactura() {
        return new Factura();
    }

    public Factura.InfoAdicional.CampoAdicional createFacturaInfoAdicionalCampoAdicional() {
        return new Factura.InfoAdicional.CampoAdicional();
    }

    public Factura.InfoFactura.TotalConImpuestos createFacturaInfoFacturaTotalConImpuestos() {
        return new Factura.InfoFactura.TotalConImpuestos();
    }

    public Factura.InfoFactura.Pagos createFacturaPagos() {
        return new Factura.InfoFactura.Pagos();
    }

    public Factura.Detalles.Detalle.Impuestos createFacturaDetallesDetalleImpuestos() {
        return new Factura.Detalles.Detalle.Impuestos();
    }

    public Factura.InfoFactura.TotalConImpuestos.TotalImpuesto createFacturaInfoFacturaTotalConImpuestosTotalImpuesto() {
        return new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
    }

//	public Factura.InfoFactura.Pagos.Pago createFacturaInfoFacturaPagosPago() {
//		return new Factura.InfoFactura.Pagos.Pago();
//	}
}
