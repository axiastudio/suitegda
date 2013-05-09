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
package com.axiastudio.suite.pratiche.entities;

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
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="gendipendenzapratica", sequenceName="pratiche.dipendenzapratica_id_seq", initialValue=1, allocationSize=1)
public class DipendenzaPratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendipendenzapratica")
    private Long id;
    @JoinColumn(name = "praticadominante", referencedColumnName = "idpratica")
    @ManyToOne
    private Pratica praticadominante;
    @JoinColumn(name = "praticadipendente", referencedColumnName = "idpratica")
    @ManyToOne
    private Pratica praticadipendente;
    @JoinColumn(name = "dipendenza", referencedColumnName = "id")
    @ManyToOne
    private Dipendenza dipendenza;
    @Column(name="invertita")
    private Boolean invertita=false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pratica getPraticadominante() {
        return praticadominante;
    }

    public void setPraticadominante(Pratica praticadominante) {
        this.praticadominante = praticadominante;
    }

    public Pratica getPraticadipendente() {
        return praticadipendente;
    }

    public void setPraticadipendente(Pratica praticadipendente) {
        this.praticadipendente = praticadipendente;
    }

    public Dipendenza getDipendenza() {
        return dipendenza;
    }

    public void setDipendenza(Dipendenza dipendenza) {
        this.dipendenza = dipendenza;
    }

    public Boolean getInvertita() {
        return invertita;
    }

    public void setInvertita(Boolean invertita) {
        this.invertita = invertita;
    }
    
    
    /*
     * Il predicato esprime la relazione nel corretto verso
     */
    public String getPredicato(){
        String out = "";
        if( this.getDipendenza() != null ){
            if( invertita ){
                out += " " + this.getDipendenza().getDescrizionedipendente() + " ";
            } else {
                out += " " + this.getDipendenza().getDescrizionedominante() + " ";
            }
        } else {
            out += " è in relazione con ";
        }
        out += " " + this.getPraticadipendente().getIdpratica();
        return out;
    }
    
    public void setPredicato(String predicato){
        // non deve fare nulla
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
        if (!(object instanceof DipendenzaPratica)) {
            return false;
        }
        DipendenzaPratica other = (DipendenzaPratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.pratiche.entities.DipendenzaPratica[ id=" + id + " ]";
    }
    
}
