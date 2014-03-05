
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "collocazioneTelematica",
    "impronta",
    "titoloDocumento",
    "primaRegistrazione",
    "tipoDocumento",
    "oggetto",
    "classifica",
    "numeroPagine",
    "note",
    "piuInfo"
})
@XmlRootElement(name = "Documento")
public class Documento {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "rife")
    @XmlIDREF
    protected Object rife;
    @XmlAttribute(name = "nome")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String nome;
    @XmlAttribute(name = "tipoMIME")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String tipoMIME;
    @XmlAttribute(name = "tipoRiferimento")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tipoRiferimento;
    @XmlElement(name = "CollocazioneTelematica")
    protected String collocazioneTelematica;
    @XmlElement(name = "Impronta")
    protected Impronta impronta;
    @XmlElement(name = "TitoloDocumento")
    protected String titoloDocumento;
    @XmlElement(name = "PrimaRegistrazione")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "TipoDocumento")
    protected String tipoDocumento;
    @XmlElement(name = "Oggetto")
    protected String oggetto;
    @XmlElement(name = "Classifica")
    protected List<Classifica> classifica;
    @XmlElement(name = "NumeroPagine")
    protected String numeroPagine;
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
     * Gets the value of the nome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the value of the nome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Gets the value of the tipoMIME property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIME() {
        return tipoMIME;
    }

    /**
     * Sets the value of the tipoMIME property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIME(String value) {
        this.tipoMIME = value;
    }

    /**
     * Gets the value of the tipoRiferimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRiferimento() {
        if (tipoRiferimento == null) {
            return "MIME";
        } else {
            return tipoRiferimento;
        }
    }

    /**
     * Sets the value of the tipoRiferimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRiferimento(String value) {
        this.tipoRiferimento = value;
    }

    /**
     * Gets the value of the collocazioneTelematica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollocazioneTelematica() {
        return collocazioneTelematica;
    }

    /**
     * Sets the value of the collocazioneTelematica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollocazioneTelematica(String value) {
        this.collocazioneTelematica = value;
    }

    /**
     * Gets the value of the impronta property.
     * 
     * @return
     *     possible object is
     *     {@link Impronta }
     *     
     */
    public Impronta getImpronta() {
        return impronta;
    }

    /**
     * Sets the value of the impronta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Impronta }
     *     
     */
    public void setImpronta(Impronta value) {
        this.impronta = value;
    }

    /**
     * Gets the value of the titoloDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitoloDocumento() {
        return titoloDocumento;
    }

    /**
     * Sets the value of the titoloDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitoloDocumento(String value) {
        this.titoloDocumento = value;
    }

    /**
     * Gets the value of the primaRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public PrimaRegistrazione getPrimaRegistrazione() {
        return primaRegistrazione;
    }

    /**
     * Sets the value of the primaRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public void setPrimaRegistrazione(PrimaRegistrazione value) {
        this.primaRegistrazione = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
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
     * Gets the value of the numeroPagine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroPagine() {
        return numeroPagine;
    }

    /**
     * Sets the value of the numeroPagine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroPagine(String value) {
        this.numeroPagine = value;
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
