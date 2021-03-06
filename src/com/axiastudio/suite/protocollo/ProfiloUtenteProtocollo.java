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
package com.axiastudio.suite.protocollo;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class ProfiloUtenteProtocollo {
    private final Ufficio sportello;
    private Ufficio attribuzionePrincipale = null;
    private ArrayList attribuzioni = new ArrayList();
    private ArrayList ufficiUtente = new ArrayList();
    private ArrayList ufficiRicercaUtente = new ArrayList();
    private ArrayList ufficiRiservatoUtente = new ArrayList();
    private ArrayList ufficiVisualizzaUtente = new ArrayList();
    private ArrayList ufficiConsolidaUtente = new ArrayList();

    public ProfiloUtenteProtocollo(Protocollo protocollo, Utente autenticato) {

        this.sportello = protocollo.getSportello();
        if( protocollo.getAttribuzioneCollection() != null ){
            for( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ){
                this.attribuzioni.add(attribuzione.getUfficio());
                if( attribuzione.getPrincipale() ){
                    this.attribuzionePrincipale = attribuzione.getUfficio();
                }
            }
        }
        if( autenticato.getUfficioUtenteCollection() != null ){
            for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
                this.ufficiUtente.add(uu.getUfficio());
                if( uu.getRicerca() ){
                    this.ufficiRicercaUtente.add(uu.getUfficio());
                }
                if( uu.getRiservato() ){
                    this.ufficiRiservatoUtente.add(uu.getUfficio());
                }
                if( uu.getVisualizza() ){
                    this.ufficiVisualizzaUtente.add(uu.getUfficio());
                }
                if( uu.getConsolida() ){
                    this.ufficiConsolidaUtente.add(uu.getUfficio());
                }
            }
        }
    }
        
    /*
     * Inserito nell'ufficio sportello o in almeno una attribuzione
     */
    public Boolean inSportelloOAttribuzione(){
        List intersezione = new ArrayList(attribuzioni);
        intersezione.retainAll(ufficiUtente);
        return ufficiUtente.contains(sportello) || intersezione.size()>0;
    }
    
    /*
     * Inserito nell'ufficio sportello o nell'attribuzione principale
     */
    public Boolean inSportelloOAttribuzionePrincipale(){
        return ufficiUtente.contains(sportello) || ufficiUtente.contains(attribuzionePrincipale);
    }
    
    /*
     * Inserito nell'ufficio sportello o in almeno una attribuzione con flag riservato
     */
    public Boolean inSportelloOAttribuzioneR(){
        List intersezione = new ArrayList(attribuzioni);
        intersezione.retainAll(ufficiRiservatoUtente);
        return ufficiRiservatoUtente.contains(sportello) || intersezione.size()>0;
    }

    /*
     * Inserito nell'ufficio sportello o in almeno una attribuzione con flag visualizza
     */
    public Boolean inSportelloOAttribuzioneV(){
        List intersezione = new ArrayList(attribuzioni);
        intersezione.retainAll(ufficiVisualizzaUtente);
        return ufficiVisualizzaUtente.contains(sportello) || intersezione.size()>0;
    }
    
    /*
     * Inserito nell'ufficio sportello o nell'attribuzione principale con flag riservato
     */
    public Boolean inSportelloOAttribuzionePrincipaleR(){
        return ufficiRiservatoUtente.contains(sportello) || ufficiRiservatoUtente.contains(attribuzionePrincipale);
    }

    /*
     * Inserito nell'ufficio sportello o nell'attribuzione principale con flag visualizza
     */
    public Boolean inSportelloOAttribuzionePrincipaleV(){
        return ufficiVisualizzaUtente.contains(sportello) || ufficiVisualizzaUtente.contains(attribuzionePrincipale);
    }

    /*
     * Inserito nell'attribuzione principale
     */
    public Boolean inAttribuzionePrincipale(){
        return ufficiUtente.contains(attribuzionePrincipale);
    }

    /*
     * Inserito nell'attribuzione principale con flag consolida
     */
    public Boolean inAttribuzionePrincipaleC(){
        return ufficiConsolidaUtente.contains(attribuzionePrincipale);
    }

    /*
     * Inserito nell'attribuzione principale con flag consolida
     */
    public Boolean inSportelloOAttribuzionePrincipaleC(){
        return ufficiConsolidaUtente.contains(attribuzionePrincipale) || ufficiConsolidaUtente.contains(sportello);
    }

    /*
     * Non inserito nello sportello o in una attribuzione
     */
    public Boolean neSportelloNeAttribuzione(){
        return !(inSportelloOAttribuzione() || inSportelloOAttribuzioneR());
    }
    
    
    
}
