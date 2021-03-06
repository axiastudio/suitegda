/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.anagrafiche.entities;

import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@EntityListeners({TimeStampedListener.class})
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="genindirizzo", sequenceName="anagrafiche.indirizzo_id_seq", initialValue=1, allocationSize=1)
public class Indirizzo implements Serializable, ITimeStamped, Comparable<Indirizzo> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genindirizzo")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="principale")
    private Boolean principale=false;
    @Column(name="tipo")
    @Enumerated(EnumType.STRING)
    private TipoIndirizzo tipo;
    @Column(name="via")
    private String via;
    @Column(name="civico", length=6)
    private String civico;
    @Column(name="cap", length=5)
    private String cap;
    @Column(name="frazione")
    private String frazione;
    @Column(name="comune")
    private String comune;
    @JoinColumn(name = "provincia", referencedColumnName = "codice")
    @ManyToOne
    private Provincia provincia;
    @JoinColumn(name = "stato", referencedColumnName = "codice")
    @ManyToOne
    private Stato stato;
    @Column(name="datanascita")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datanascita;
    @Column(name="datacessazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacessazione;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

    public TipoIndirizzo getTipo() {
        return tipo;
    }

    public void setTipo(TipoIndirizzo tipo) {
        this.tipo = tipo;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getFrazione() {
        return frazione;
    }

    public void setFrazione(String frazione) {
        this.frazione = frazione;
    }  

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public Date getDatanascita() {
        return datanascita;
    }

    public void setDatanascita(Date datanascita) {
        this.datanascita = datanascita;
    }

    public Date getDatacessazione() {
        return datacessazione;
    }

    public void setDatacessazione(Date datacessazione) {
        this.datacessazione = datacessazione;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indirizzo)) {
            return false;
        }
        Indirizzo other = (Indirizzo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        String testo="";
        if ( getPrincipale() ) {
            testo="* ";
        }
        testo+= getVia() + " " + getCivico() + " - " + getComune();
        if ( getProvincia()!=null ){
            testo+= " (" + getProvincia() + ")";
        }
        if ( getDescrizione()!=null ){
            testo+=" - " + getDescrizione();
        }
        return testo;
    }

    @Override
    public int compareTo(Indirizzo o) {
        return Comparators.PRINCIPALEDESC.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Indirizzo> PRINCIPALE = new Comparator<Indirizzo>() {
            @Override
            public int compare(Indirizzo o1, Indirizzo o2) {
                if (o1.principale.equals(o2.principale)) {
                    return o1.id.compareTo(o2.id);
                } else {
                    return o1.principale.compareTo(o2.principale);
                }
            }
        };
        public static Comparator<Indirizzo> PRINCIPALEDESC = new Comparator<Indirizzo>() {
            @Override
            public int compare(Indirizzo o1, Indirizzo o2) {
                if (o1.principale.equals(o2.principale)) {
                    return o2.id.compareTo(o1.id);
                } else {
                    return o2.principale.compareTo(o1.principale);
                }
            }
        };
    }


}
