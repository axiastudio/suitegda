/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.anagrafiche.entities;

import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@EntityListeners({TimeStampedListener.class})
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="genrelazionesoggetto", sequenceName="anagrafiche.relazionesoggetto_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaReferenteSoggetto",
        query = "SELECT rs FROM RelazioneSoggetto rs  "
                + "WHERE rs.soggetto.id = :id AND " +
                    "rs.soggetto.tipo!=:tipo AND rs.relazionato.tipo=:tipo AND " +
                    "(rs.datanascita IS NULL OR rs.datanascita<=:data) AND "+
                    "(rs.datacessazione IS NULL OR rs.datacessazione>=:data)")
public class RelazioneSoggetto implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genrelazionesoggetto")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @JoinColumn(name = "relazione", referencedColumnName = "id")
    @ManyToOne
    private Relazione relazione;
    @JoinColumn(name = "relazionato", referencedColumnName = "id")
    @ManyToOne
    private Soggetto relazionato;
    @Column(name="datanascita")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datanascita;
    @Column(name="datacessazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacessazione;
    @Column(name="abilitatoweb")
    private Boolean abilitatoweb=false;
    @Column(name="invertita")
    private Boolean invertita=false;

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

    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    public Relazione getRelazione() {
        return relazione;
    }

    public void setRelazione(Relazione relazione) {
        this.relazione = relazione;
    }

    public Soggetto getRelazionato() {
        return relazionato;
    }

    public void setRelazionato(Soggetto relazionato) {
        this.relazionato = relazionato;
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

    public Boolean getAbilitatoweb() {
        return abilitatoweb;
    }

    public void setAbilitatoweb(Boolean abilitatoweb) {
        this.abilitatoweb = abilitatoweb;
    }

    public Boolean getInvertita() {
        return invertita;
    }

    public void setInvertita(Boolean invertita) {
        this.invertita = invertita;
    }

    public Boolean getAttivo() {
        return (getDatacessazione() == null || getDatacessazione().after(new Date()));
    }

    public void setAttivo(Boolean attivo) {

    }

    
    /*
     * Il predicato esprime la relazione nel corretto verso
     */

    public String getPredicato(){
         return getPredicato(this.getRelazione(), this.getInvertita());
    }

    public String getPredicato(Relazione currentRelazione, Boolean currentInvertita){
        String out = "";

        if( currentRelazione != null ){
            if( currentInvertita==null || ! currentInvertita ){
                out += " " + currentRelazione.getDescrizione() + " ";
            } else {
                out += " " + currentRelazione.getInversa() + " ";
            }
        } else {
            out += " è in relazione con ";
        }
        out += this.getRelazionato().toString();
//        if( this.getDatanascita() != null ){
//            out += " dal " + SuiteUtil.DATE_FORMAT.format(this.getDatanascita());
//        }
//        if( this.getDatacessazione()!= null ){
//            out += " fino al " + SuiteUtil.DATE_FORMAT.format(this.getDatacessazione());
//        }
        return out;
    }

    public void setPredicato(String predicato){
        // non deve fare nulla
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? (id > 0 ? id.hashCode() : ((Long)((-1*id)+1000000)).hashCode()) : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelazioneSoggetto)) {
            return false;
        }
        RelazioneSoggetto other = (RelazioneSoggetto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.getPredicato();
    }
    
}
