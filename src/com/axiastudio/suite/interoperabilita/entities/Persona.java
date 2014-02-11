
package com.axiastudio.suite.interoperabilita.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale",
    "identificativo"
})
@XmlRootElement(name = "Persona")
public class Persona {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlElements({
        @XmlElement(name = "Denominazione", required = true, type = Denominazione.class),
        @XmlElement(name = "Nome", required = true, type = Nome.class),
        @XmlElement(name = "Cognome", required = true, type = Cognome.class),
        @XmlElement(name = "Titolo", required = true, type = Titolo.class),
        @XmlElement(name = "CodiceFiscale", required = true, type = CodiceFiscale.class)
    })
    protected List<Object> denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale;
    @XmlElement(name = "Identificativo")
    protected String identificativo;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDenominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Denominazione }
     * {@link Nome }
     * {@link Cognome }
     * {@link Titolo }
     * {@link CodiceFiscale }
     * 
     * 
     */
    public List<Object> getDenominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale() {
        if (denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale == null) {
            denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale = new ArrayList<Object>();
        }
        return this.denominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale;
    }

    /**
     * Gets the value of the identificativo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativo() {
        return identificativo;
    }

    /**
     * Sets the value of the identificativo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativo(String value) {
        this.identificativo = value;
    }

}
