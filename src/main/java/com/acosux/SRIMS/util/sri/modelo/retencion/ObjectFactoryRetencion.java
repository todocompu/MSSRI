package com.acosux.SRIMS.util.sri.modelo.retencion;

import com.acosux.SRIMS.util.sri.modelo.InfoTributaria;
import javax.xml.bind.annotation.XmlRegistry;

/**
 *
 * @author Ing. Mario
 */
@XmlRegistry
public class ObjectFactoryRetencion {

    public ComprobanteRetencion.InfoAdicional createComprobanteRetencionInfoAdicional() {
        return new ComprobanteRetencion.InfoAdicional();
    }

    public ComprobanteRetencion.Impuestos createComprobanteRetencionImpuestos() {
        return new ComprobanteRetencion.Impuestos();
    }

    public ComprobanteRetencion.InfoCompRetencion createComprobanteRetencionInfoCompRetencion() {
        return new ComprobanteRetencion.InfoCompRetencion();
    }

    public ComprobanteRetencion.InfoAdicional.CampoAdicional createComprobanteRetencionInfoAdicionalCampoAdicional() {
        return new ComprobanteRetencion.InfoAdicional.CampoAdicional();
    }

    public InfoTributaria createInfoTributaria() {
        return new InfoTributaria();
    }

    public ComprobanteRetencion createComprobanteRetencion() {
        return new ComprobanteRetencion();
    }

    public ImpuestoRetencion createImpuesto() {
        return new ImpuestoRetencion();
    }
}
