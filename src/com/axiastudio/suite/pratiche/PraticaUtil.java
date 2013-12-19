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
package com.axiastudio.suite.pratiche;

import com.axiastudio.mapformat.MessageMapFormat;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.entities.Giunta;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.protocollo.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PraticaUtil {

    public static String creaCodificaInterna(TipoPratica tipoPratica){

        Map<String, Object> map = new HashMap();

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();

        // anno
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        map.put("anno", year.toString());

        // giunta
        Giunta giuntaCorrente = SuiteUtil.trovaGiuntaCorrente();
        map.put("giunta", giuntaCorrente.getNumero());

        // codici sezione e numerici
        String formulacodifica = tipoPratica.getFormulacodifica();
        List<TipoPratica> tipi = new ArrayList();
        TipoPratica iter = tipoPratica;
        while( iter != null ){
            tipi.add(0, iter);
            iter = iter.getTipopadre();
        }

        Integer n = tipoPratica.getLunghezzaprogressivo();

        // calcolo progressivi per nodo
        Integer i = 0;
        for( TipoPratica tipo: tipi ){
            i++;
            if (n>0) {
            String sql = "select max(substring(p.codiceinterno, length(p.codiceinterno) - " + tipoPratica.getLunghezzaprogressivo().toString() + " + 1)) " +
                                "from Pratica p join p.tipo t";
            sql += " where p.codiceinterno like '"+tipo.getCodice()+"%'";
            if( tipoPratica.getProgressivoanno() ){
                sql += " and t.progressivoanno = TRUE";
                sql += " and p.anno = " + year.toString();
            }
            if( tipoPratica.getProgressivogiunta() ){
                sql += " and t.progressivogiunta = TRUE";
                sql += " and p.datapratica >= '" + SuiteUtil.DATE_FORMAT.format(giuntaCorrente.getDatanascita()) +"'";
            }
            Query q = em.createQuery(sql);
            String maxString = (String) q.getSingleResult();
            Integer max=1;
            if( maxString != null ){
                max = Integer.parseInt(maxString.substring(maxString.length() - n));
                max += 1;
            }
            map.put("n"+i, max);
            }
            map.put("s"+i, tipo.getCodice());
        }

        // composizione codifica
        MessageMapFormat mmp = new MessageMapFormat(formulacodifica);
        String codifica = mmp.format(map);
        return codifica;
    }

    // Verifica se esiste già una pratica con il codice specificato, x tipologie senza progressivo
    public static Boolean codificaInternaUnivoca(TipoPratica tipoPratica){
        if (tipoPratica.getLunghezzaprogressivo() > 0) {
            return Boolean.TRUE;
        }

        String codifica=creaCodificaInterna(tipoPratica);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
//        String sql = "select count(p.codiceinterno) from Pratica p where p.codiceinterno = '" + codifica + "'";
        Query q = em.createQuery("select count(p.codiceinterno) from Pratica p where p.codiceinterno = '" + codifica + "'");
        Long i = (Long) q.getSingleResult();
        return (i == 0);
    }

    // protocollazione della pratica
    public static Protocollo protocollaPratica(Pratica pratica, Ufficio sportello,
                                           String oggettoProtocollo, List<Ufficio> attribuzioni){
        Oggetto oggetto = null;
        return protocollaPratica(pratica, sportello, oggettoProtocollo, attribuzioni, oggetto, TipoProtocollo.INTERNO, null, null);
    }
    public static Protocollo protocollaPratica(Pratica pratica, Ufficio sportello,
                                           String oggettoProtocollo, List<Ufficio> attribuzioni, Oggetto oggetto,
                                           TipoProtocollo tipo, List<Soggetto> soggetti, List<Ufficio> uffici){
        Protocollo protocollo = new Protocollo();
        protocollo.setOggetto(oggettoProtocollo);
        protocollo.setSportello(sportello);
        protocollo.setTipo(tipo);
        // attribuzioni
        List<Attribuzione> attribuzioniList = new ArrayList<Attribuzione>();
        for( Ufficio ufficio: attribuzioni ){
            Attribuzione attribuzione = new Attribuzione();
            attribuzione.setUfficio(ufficio);
            attribuzione.setUfficio(ufficio);
        }
        protocollo.setAttribuzioneCollection(attribuzioniList);
        // uffici mittenti o destinatari
        if( uffici != null ){
            List<UfficioProtocollo> ufficioProtocolloList = new ArrayList<UfficioProtocollo>();
            for( Ufficio attribuzione: uffici ){
                UfficioProtocollo ufficioProtocollo = new UfficioProtocollo();
                ufficioProtocollo.setUfficio(attribuzione);
            }
            protocollo.setUfficioProtocolloCollection(ufficioProtocolloList);
        }
        // soggetti
        if( soggetti != null ){
            List<SoggettoProtocollo> soggettiProtocollo = new ArrayList<SoggettoProtocollo>();
            for( Soggetto soggetto: soggetti ){
                SoggettoProtocollo soggettoProtocollo = new SoggettoProtocollo();
                soggettoProtocollo.setSoggetto(soggetto);
            }
            protocollo.setSoggettoProtocolloCollection(soggettiProtocollo);
        }
        // inserimento protocollo nella pratica
        Collection<PraticaProtocollo> pratiche = new ArrayList<PraticaProtocollo>();
        PraticaProtocollo pp = new PraticaProtocollo();
        pp.setPratica(pratica);
        pp.setOggetto(oggetto);
        pratiche.add(pp);
        protocollo.setPraticaProtocolloCollection(pratiche);
        // commit
        Controller controller = (Controller) Register.queryUtility(IController.class, Protocollo.class.getName());
        Validation validation = controller.commit(protocollo);
        if( !validation.getResponse() ){
            return null;
        }
        return protocollo;
    }

}
