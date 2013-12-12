package com.axiastudio.suite.procedimenti;

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.IFinanziaria;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.FaseProcedimento;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 02/12/13
 * Time: 15:00
 */
public class SimpleWorkFlow {

    List<FasePratica> fasi = new ArrayList<FasePratica>();
    Object obj=null;
    Procedimento procedimento=null;

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
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        binding.setVariable("gestoreDeleghe", gestoreDeleghe);
        binding.setVariable("finanziariaUtil", finanziariaUtil);
        binding.setVariable("alfrescoHelper", alfrescoHelper);
        binding.setVariable("CodiceCarica", CodiceCarica.class);
        binding.setVariable("utente", utente);
        return binding;
    }

    public Boolean attivabile(FasePratica fp){
        String groovyClosure = fp.getCondizione();
        return (Boolean) eseguiClosure(groovyClosure);
    }

    public Boolean attivabile(FaseProcedimento fp){
        String groovyClosure = fp.getCondizione();
        return (Boolean) eseguiClosure(groovyClosure);
    }

    public Object eseguiClosure(String groovyClosure) {
        if( groovyClosure== null ){
            return true;
        }
        Binding binding = createBindings();

        GroovyShell shell = new GroovyShell(binding);
        String groovy = groovyClosure + "(obj)";
        Object value = shell.evaluate(groovy);
        return value;
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
        for( FasePratica fp: getFasi() ){
            if( !fp.getCompletata() ){
                return fp;
            }
        }
        return null;
    }

    public void completaFase(FasePratica fp){
        completaFase(fp, Boolean.TRUE);
    }

    public void completaFase(FasePratica fp, Boolean confermata){
        //fp.setCompletata(confermata);
        if( confermata ){
            setFaseAttiva(fp.getConfermata());
        } else {
            setFaseAttiva(fp.getRifiutata());
        }

    }

    public void setFaseAttiva(FasePratica faseAttiva){
        Boolean trovata = Boolean.FALSE;
        for( FasePratica fp: getFasi() ){
            if( !trovata ){
                if( !fp.equals(faseAttiva) ){
                    fp.setCompletata(Boolean.TRUE);
                } else {
                    fp.setCompletata(Boolean.FALSE);
                    trovata = Boolean.TRUE;
                }
            } else {
                fp.setCompletata(Boolean.FALSE);
            }
        }
    }

}
