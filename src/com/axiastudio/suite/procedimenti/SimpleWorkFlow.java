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
package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.DeterminaUtil;
import com.axiastudio.suite.finanziaria.entities.IFinanziaria;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.PraticaUtil;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.Visto;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class SimpleWorkFlow {

    private List<FasePratica> fasi = new ArrayList<FasePratica>();
    private Object obj=null;
    private Procedimento procedimento=null;
    private Object result = null;

    /*
     *  L'entità da cui reperire un procedimento può essere di due tipi:
     *
     *  - Pratica
     *  - IDettaglio
     *
     *  Opzionalmente può essere indicato un procedimento da utilizzare;
     *  in questo caso le fasi considerate non sono quelle della pratica
     *  associata, ma quelle del procedimento.
     *
     */
    public SimpleWorkFlow(Pratica pratica){
        for( FasePratica fp: pratica.getFasePraticaCollection() ){
            fasi.add(fp);
        }
        obj = pratica;
        procedimento = pratica.getTipo().getProcedimento();
    }
    public SimpleWorkFlow(IDettaglio dettaglio){
        this(dettaglio.getPratica());
        obj = dettaglio;
    }
    public SimpleWorkFlow(Pratica pratica, Procedimento procedimento){
        this(pratica);
        this.procedimento = procedimento;
    }
    public SimpleWorkFlow(IDettaglio dettaglio, Procedimento procedimento){
        this(dettaglio);
        this.procedimento = procedimento;
    }


    /*
     *  default bindings
     */
    private Binding createBindings(){
        Binding binding = new Binding();
        binding.setVariable("obj", obj);
        IGestoreDeleghe gestoreDeleghe = (IGestoreDeleghe) Register.queryUtility(IGestoreDeleghe.class);
        IFinanziaria finanziariaUtil = (IFinanziaria) Register.queryUtility(IFinanziaria.class);
        Class formClass = (Class) Register.queryUtility(IForm.class, obj.getClass().getName());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(formClass, "CMIS");
        AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(obj);
        List<String> documenti = new ArrayList<String>();
        List<HashMap> children=null;
        try {
            children = alfrescoHelper.children();
        } catch (Exception e){
            // log?
        }
        // XXX: e se Alfresco non è disponibile?
        if( children != null ){
            for( Map child: children ){
                String fileName = (String) child.get("contentStreamFileName");
                if( fileName != null ){ // XXX: per saltare le cartelle
                    documenti.add(fileName);
                }
            }
        }
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        binding.setVariable("utente", utente);
        binding.setVariable("gestoreDeleghe", gestoreDeleghe);
        binding.setVariable("finanziariaUtil", finanziariaUtil);
        binding.setVariable("alfrescoHelper", alfrescoHelper);
        binding.setVariable("documenti", documenti);
        binding.setVariable("CodiceCarica", String.class);
        binding.setVariable("PraticaUtil", PraticaUtil.class);
        binding.setVariable("DeterminaUtil", DeterminaUtil.class);
        return binding;
    }

    public Boolean permesso(FasePratica fp){
        if ( checkTitoloDelega(fp) != null ){
            return true;
        }
        result = "Non disponi dei permessi"; // TODO: magari descrivere meglio.
        return false;
    }

    public TitoloDelega checkTitoloDelega(FasePratica fp) {
        IDettaglio dettaglio = PraticaUtil.trovaDettaglioDaPratica(fp.getPratica());
        String cariche = fp.getCariche();
        if( cariche == null || cariche.equals("")){
            Utente utente = (Utente) Register.queryUtility(IUtente.class);
            return new TitoloDelega(true, false, null, utente, null);
        }
        GestoreDeleghe gestoreDeleghe = (GestoreDeleghe) Register.queryUtility(IGestoreDeleghe.class);
        for( String carica: cariche.split(",") ){
            Utente utente = (Utente) Register.queryUtility(IUtente.class);
            TitoloDelega check = gestoreDeleghe.checkTitoloODelega(carica, dettaglio.getServizio(), procedimento,
                    dettaglio.getUfficio(), utente);
            if( check != null ){
                return check;
            }
        }
        return null;
    }

    public Boolean condizione(FasePratica fp){
        String groovyClosure = fp.getCondizione();
        return eseguiClosure(groovyClosure);
    }

    public Boolean azione(FasePratica fp){
        String groovyClosure = fp.getAzione();
        return eseguiClosure(groovyClosure);
    }

    public Boolean eseguiClosure(String groovyClosure) {
        result = null;
        if( groovyClosure == null || groovyClosure.length()==0 ){
            return true;
        }
        Binding binding = createBindings();

        GroovyShell shell = new GroovyShell(binding);
        String groovy = groovyClosure.trim() + "(obj)";
        Object value=null;
        try{
            value = shell.evaluate(groovy);
        } catch (Exception ex){
            String msg = "Errore eseguendo il codice di workflow:\n" + groovyClosure;
            Logger.getLogger(SimpleWorkFlow.class.getName()).log(Level.WARNING, msg, ex);
            return false;
        }
        if( value instanceof Boolean ){
            return (Boolean) value;
        } else {
            result = value;
            return false;
        }
    }

    public Object getResult() {
        return result;
    }
/*
     *   GESTIONE FASI DEL PROCEDIMENTO
     */

    public List<FasePratica> getFasi() {
        return fasi;
    }

    public FasePratica getFase(Integer i){
        return fasi.get(i);
    }

    public FasePratica getFaseAttiva(){
        // per ora restituisco la prima attiva che trovo (è unica)
        for( FasePratica fp: getFasi() ){
            if( fp.getAttiva() ){
                return fp;
            }
        }
        return null;
    }

    public Boolean completaFase(FasePratica fp){
        return completaFase(fp, Boolean.TRUE, "");
    }

    public Boolean completaFase(FasePratica fp, Boolean confermata){
        return completaFase(fp, confermata, "");
    }

    public Boolean completaFase(FasePratica fp, Boolean confermata, String commento){

        if( confermata ){
            fp.setCompletata(true);
            fp.setNegata(false);
            FasePratica successiva = fp.getConfermata();
            while( successiva.getDascartare()!=null && successiva.getDascartare() != "" &&
                    eseguiClosure(successiva.getDascartare()) ){
                successiva = successiva.getConfermata();
            }
            setFaseAttiva(successiva);
            creaVisto(fp, commento);
        } else {
            fp.setCompletata(false);
            fp.setNegata(true);
            setFaseAttiva(fp.getRifiutata());
            creaVisto(fp, true, commento); // negato
        }
        return Boolean.TRUE;
    }

    public void creaVisto(FasePratica fp) {
        creaVisto(fp, false, "");
    }

    private void creaVisto(FasePratica fp, String commento) {
        creaVisto(fp, false, commento);
    }

    private void creaVisto(FasePratica fp, Boolean negato, String commento) {
        Pratica pratica = fp.getPratica();
        Visto visto = new Visto();
        visto.setFase(fp.getFase());
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        visto.setUtente(utente);
        visto.setNegato(negato);
        visto.setCommento(commento);
        IDettaglio dettaglio = PraticaUtil.trovaDettaglioDaPratica(fp.getPratica());
        String cariche = fp.getCariche();
        if( cariche != null && !cariche.equals("")){
            // ci sono dele cariche da verificare
            GestoreDeleghe gestoreDeleghe = (GestoreDeleghe) Register.queryUtility(IGestoreDeleghe.class);
            for( String carica: cariche.split(",") ){
                TitoloDelega titoloDelega = gestoreDeleghe.checkTitoloODelega(carica, dettaglio.getServizio(), procedimento, dettaglio.getUfficio());
                if( titoloDelega != null ){
                    visto.setCodiceCarica(carica);
                    if( titoloDelega.getTitolo() ){
                        visto.setResponsabile(utente);
                    } else if( titoloDelega.getDelega() ){
                        visto.setResponsabile(titoloDelega.getDelegante());
                    }
                }
            }
        }
        visto.setPratica(pratica);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(visto.getClass());
        controller.commit(visto);
    }

    public void setFaseAttiva(FasePratica faseAttiva){
        for( FasePratica fp: getFasi() ){
            if( fp.equals(faseAttiva) ){
                fp.setCompletata(false);
                fp.setAttiva(true);
            } else {
                fp.setAttiva(false);
            }
        }
    }

    public Boolean setFasePratica(FasePratica fp, Fase faseAttiva){

        Pratica pratica=fp.getPratica();
        pratica.setFase(faseAttiva);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(pratica);
        try {
            em.getTransaction().commit();
        }  catch (Exception e) {
            result=e.getMessage();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
