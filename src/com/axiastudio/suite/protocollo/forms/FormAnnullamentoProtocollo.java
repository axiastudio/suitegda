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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.gui.QCheckBox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormAnnullamentoProtocollo extends Dialog {
    
    public FormAnnullamentoProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.storeInitialized.connect(this, "updatePermission()");        
    }

    private void updatePermission() {
        /* permesso di confermare o respingere */
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Costante costanteUfficioAnnullati = SuiteUtil.trovaCostante("UFFICIO_ANNULLATI");
        Long idUfficioAnnullati = Long.parseLong(costanteUfficioAnnullati.getValore());
        Boolean inUfficioAnnullati = Boolean.FALSE;     // TODO: conferma annullamento da rifare tramite finestra 'scollegata' da form Protocollo (e dalla logica della callback)
        for( UfficioUtente ufficio: autenticato.getUfficioUtenteCollection()) {
            if (ufficio.getUfficio().getId().equals(idUfficioAnnullati) && ufficio.getRicerca()) {
                inUfficioAnnullati = Boolean.TRUE;
            }
        }
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        QCheckBox checkBox_autorizzato = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_autorizzato");
        QCheckBox checkBox_respinto = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_respinto");
        Boolean modifica = annullamento.getId()!=null && !annullamento.getRespinto() && !annullamento.getAutorizzato()
                            && autenticato.getAttributoreprotocollo() && autenticato.getSupervisorepratiche() && inUfficioAnnullati;
        checkBox_autorizzato.setEnabled( modifica );
        checkBox_respinto.setEnabled( modifica );
    }

    @Override
    public void accept() {
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        if( annullamento.getEsecutoreautorizzazione() == null && annullamento.getId() != null &&
                                    (annullamento.getAutorizzato() || annullamento.getRespinto()) ){
            
            /* devo registrare indipendentemente dal protocollo */
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
            annullamento.setEsecutoreautorizzazione(autenticato.getLogin());
            annullamento.setDataautorizzazione(today);
            
            /* se è un annullamento:
             *  - aggiungo l'ufficio protocollo come attribuzione in via principale
             *  - inserisco nella pratica (unica) dei protocolli annullati
             *  - consolido il protocollo
             *  - marco il protocollo come annullato
             */
            Database db = (Database) Register.queryUtility(IDatabase.class);
            if( annullamento.getAutorizzato() ){
                Costante costantePraticaAnnullati = SuiteUtil.trovaCostante("PRATICA_ANNULLATI");
                Pratica praticaAnnullati = SuiteUtil.trovaPratica(costantePraticaAnnullati.getValore());
                Costante costanteUfficioAnnullati = SuiteUtil.trovaCostante("UFFICIO_ANNULLATI");
                Long idUfficioAnnullati = Long.parseLong(costanteUfficioAnnullati.getValore());
                Controller controllerUfficio = db.createController(Ufficio.class);
                Ufficio ufficioAnnullati = (Ufficio) controllerUfficio.get(idUfficioAnnullati);
                Protocollo protocollo = annullamento.getProtocollo();
                protocollo.setPraticaProtocolloCollection(null);
                List<Attribuzione> attribuzioni = (List<Attribuzione>) protocollo.getAttribuzioneCollection();
                for( Attribuzione attrib: protocollo.getAttribuzioneCollection()) {
                    if (attrib.getUfficio() == ufficioAnnullati) {
                        attribuzioni.remove(attrib);
                    } else {
                    attrib.setPrincipale(Boolean.FALSE);
                    }
                }
                Attribuzione attribuzione = new Attribuzione();
                attribuzione.setUfficio(ufficioAnnullati);
                attribuzione.setPrincipale(Boolean.TRUE);
                attribuzioni.add(attribuzione);
                protocollo.setAttribuzioneCollection(attribuzioni);
                List<PraticaProtocollo> praticheProtocollo = new ArrayList();
                PraticaProtocollo praticaProtocollo = new PraticaProtocollo();
                praticaProtocollo.setPratica(praticaAnnullati);
                praticaProtocollo.setOriginale(Boolean.TRUE);
                praticheProtocollo.add(praticaProtocollo);
                protocollo.setPraticaProtocolloCollection(praticheProtocollo);
                protocollo.setConvalidaattribuzioni(Boolean.TRUE);
                protocollo.setConvalidaprotocollo(Boolean.TRUE);
                protocollo.setConsolidadocumenti(Boolean.TRUE);
                protocollo.setAnnullato(Boolean.TRUE);  // Possibile che mancasse??
                Controller controllerProtocollo = db.createController(Protocollo.class);
                controllerProtocollo.commit(protocollo);
            } else {
                Controller controllerAnnullamento = db.createController(annullamento.getClass());
                controllerAnnullamento.commit(annullamento);
            }
        }
        super.accept();
    }
    
    
    
}
