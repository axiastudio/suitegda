
package com.axiastudio.suite.interoperabilita.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indirizzoTelematico",
    "destinatario"
})
@XmlRootElement(name = "Destinazione")
public class Destinazione {

    @XmlAttribute(name = "confermaRicezione")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String confermaRicezione;
    @XmlElement(name = "IndirizzoTelematico", required = true)
    protected IndirizzoTelematico indirizzoTelematico;
    @XmlElement(name = "Destinatario")
    protected List<Destinatario> destinatario;

    /**
     * Gets the value of the confermaRicezione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfermaRicezione() {
        if (confermaRicezione == null) {
            return "no";
        } else {
            return confermaRicezione;
        }
    }

    /**
     * Sets the value of the confermaRicezione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfermaRicezione(String value) {
        this.confermaRicezione = value;
    }

    /**
     * Gets the value of the indirizzoTelematico property.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematico }
     *     
     */
    public IndirizzoTelematico getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    /**
     * Sets the value of the indirizzoTelematico property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematico }
     *     
     */
    public void setIndirizzoTelematico(IndirizzoTelematico value) {
        this.indirizzoTelematico = value;
    }

    /**
     * Gets the value of the destinatario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinatario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinatario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Destinatario }
     * 
     * 
     */
    public List<Destinatario> getDestinatario() {
        if (destinatario == null) {
            destinatario = new ArrayList<Destinatario>();
        }
        return this.destinatario;
    }

}
