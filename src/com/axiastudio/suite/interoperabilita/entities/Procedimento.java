
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codiceAmministrazione",
    "codiceAOO",
    "identificativo",
    "tipoProcedimento",
    "oggetto",
    "classifica",
    "responsabile",
    "dataAvvio",
    "dataTermine",
    "note",
    "piuInfo"
})
@XmlRootElement(name = "Procedimento")
public class Procedimento {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "rife")
    @XmlIDREF
    protected Object rife;
    @XmlElement(name = "CodiceAmministrazione", required = true)
    protected String codiceAmministrazione;
    @XmlElement(name = "CodiceAOO", required = true)
    protected String codiceAOO;
    @XmlElement(name = "Identificativo", required = true)
    protected String identificativo;
    @XmlElement(name = "TipoProcedimento")
    protected String tipoProcedimento;
    @XmlElement(name = "Oggetto")
    protected String oggetto;
    @XmlElement(name = "Classifica")
    protected List<Classifica> classifica;
    @XmlElement(name = "Responsabile")
    protected Responsabile responsabile;
    @XmlElement(name = "DataAvvio")
    protected String dataAvvio;
    @XmlElement(name = "DataTermine")
    protected String dataTermine;
    @XmlElement(name = "Note")
    protected String note;
    @XmlElement(name = "PiuInfo")
    protected PiuInfo piuInfo;

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
     * Gets the value of the rife property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRife() {
        return rife;
    }

    /**
     * Sets the value of the rife property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRife(Object value) {
        this.rife = value;
    }

    /**
     * Gets the value of the codiceAmministrazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAmministrazione() {
        return codiceAmministrazione;
    }

    /**
     * Sets the value of the codiceAmministrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAmministrazione(String value) {
        this.codiceAmministrazione = value;
    }

    /**
     * Gets the value of the codiceAOO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAOO() {
        return codiceAOO;
    }

    /**
     * Sets the value of the codiceAOO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAOO(String value) {
        this.codiceAOO = value;
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

    /**
     * Gets the value of the tipoProcedimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProcedimento() {
        return tipoProcedimento;
    }

    /**
     * Sets the value of the tipoProcedimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProcedimento(String value) {
        this.tipoProcedimento = value;
    }

    /**
     * Gets the value of the oggetto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Sets the value of the oggetto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggetto(String value) {
        this.oggetto = value;
    }

    /**
     * Gets the value of the classifica property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifica property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifica().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classifica }
     * 
     * 
     */
    public List<Classifica> getClassifica() {
        if (classifica == null) {
            classifica = new ArrayList<Classifica>();
        }
        return this.classifica;
    }

    /**
     * Gets the value of the responsabile property.
     * 
     * @return
     *     possible object is
     *     {@link Responsabile }
     *     
     */
    public Responsabile getResponsabile() {
        return responsabile;
    }

    /**
     * Sets the value of the responsabile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Responsabile }
     *     
     */
    public void setResponsabile(Responsabile value) {
        this.responsabile = value;
    }

    /**
     * Gets the value of the dataAvvio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAvvio() {
        return dataAvvio;
    }

    /**
     * Sets the value of the dataAvvio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAvvio(String value) {
        this.dataAvvio = value;
    }

    /**
     * Gets the value of the dataTermine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataTermine() {
        return dataTermine;
    }

    /**
     * Sets the value of the dataTermine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataTermine(String value) {
        this.dataTermine = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Gets the value of the piuInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PiuInfo }
     *     
     */
    public PiuInfo getPiuInfo() {
        return piuInfo;
    }

    /**
     * Sets the value of the piuInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PiuInfo }
     *     
     */
    public void setPiuInfo(PiuInfo value) {
        this.piuInfo = value;
    }

}
