/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.suite.procedimenti.entities.Procedimento;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="gentipologiapratica", sequenceName="pratiche.tipologiapratica_id_seq", initialValue=1, allocationSize=1)
public class TipologiaPratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipologiapratica")
    private Long id;
    @Column(name="codice")
    private String codice;
    @Column(name="descrizione")
    private String descrizione;
    @JoinColumn(name = "tipologiapadre", referencedColumnName = "id")
    @ManyToOne
    private TipologiaPratica tipologiapadre;
    @JoinColumn(name="procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public TipologiaPratica getTipologiapadre() {
        return tipologiapadre;
    }

    public void setTipologiapadre(TipologiaPratica tipologiapadre) {
        this.tipologiapadre = tipologiapadre;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipologiaPratica)) {
            return false;
        }
        TipologiaPratica other = (TipologiaPratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.codice;
    }
    
}