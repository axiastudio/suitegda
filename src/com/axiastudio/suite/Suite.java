/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.plugins.barcode.Barcode;
import com.axiastudio.pypapi.plugins.cmis.CMIS;
import com.axiastudio.pypapi.plugins.extraattributes.ExtraAttributes;
import com.axiastudio.pypapi.plugins.ooops.Ooops;
import com.axiastudio.pypapi.plugins.ooops.RuleSet;
import com.axiastudio.pypapi.plugins.ooops.Template;
import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormQuickInsertSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormSoggetto;
import com.axiastudio.suite.base.Login;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.demo.DemoData;
import com.axiastudio.suite.pratiche.PraticaCallbacks;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipologiaPratica;
import com.axiastudio.suite.pratiche.forms.FormPratica;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloCallbacks;
import com.axiastudio.suite.protocollo.ProtocolloPrivate;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
import com.axiastudio.suite.protocollo.forms.FormSoggettoProtocollo;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.forms.FormPubblicazione;
import com.axiastudio.suite.sedute.entities.Carica;
import com.axiastudio.suite.sedute.entities.CaricaCommissione;
import com.axiastudio.suite.sedute.entities.Commissione;
import com.axiastudio.suite.sedute.entities.Seduta;
import com.axiastudio.suite.sedute.entities.TipologiaSeduta;
import com.axiastudio.suite.sedute.forms.FormTipologiaSeduta;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Suite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String jdbcUrl = System.getProperty("jdbc.url");
        String jdbcUser = System.getProperty("jdbc.user");
        String jdbcPassword = System.getProperty("jdbc.password");
        String jdbcDriver = System.getProperty("jdbc.driver");
        Map properties = new HashMap();
        if( jdbcUrl != null ){
            properties.put("javax.persistence.jdbc.url", jdbcUrl);
        }
        if( jdbcUser != null ){
            properties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            properties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            properties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }

        Database db = new Database();
        if( properties.isEmpty() ){
            db.open("SuitePU");
        } else {
            properties.put("eclipselink.ddl-generation", "");
            db.open("SuitePU", properties);
        }
        Register.registerUtility(db, IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        
        
        
        // registro adapter, validatori, e privacy
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(ProtocolloCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(PraticaCallbacks.class));
        Register.registerPrivates(Resolver.privatesFromClass(ProtocolloPrivate.class));
        
        // dati di base
        if( properties.isEmpty() ){
            DemoData.initSchema();
            DemoData.initData();
        }
        
        Application app = new Application(args);
        app.setLanguage("it");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                              Ufficio.class,
                              Window.class,
                              "Uffici");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/utente.ui",
                              Utente.class,
                              Window.class,
                              "Utenti");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                              Soggetto.class,
                              FormSoggetto.class,
                              "Soggetti anagrafici");

        Register.registerUtility(FormQuickInsertSoggetto.class, IQuickInsertDialog.class, Soggetto.class.getName());
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/indirizzo.ui",
                              Indirizzo.class,
                              FormIndirizzo.class,
                              "Indirizzo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/pratica.ui",
                              Pratica.class,
                              FormPratica.class,
                              "Pratiche");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/tipologiapratica.ui",
                              TipologiaPratica.class,
                              Window.class,
                              "Tipologia Pratica");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                              SoggettoProtocollo.class,
                              FormSoggettoProtocollo.class,
                              "Soggetto del protocollo");
      
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                              PraticaProtocollo.class,
                              Window.class,
                              "Pratica contenente il protocollo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                              Protocollo.class,
                              FormProtocollo.class, // custom form
                              "Protocolli");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Fascicolo.class,
                              Window.class,
                              "Finestra fascicolo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pubblicazioni/forms/pubblicazione.ui",
                              Pubblicazione.class,
                              FormPubblicazione.class,
                              "Pubblicazione all'albo");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Carica.class,
                              Window.class,
                              "Carica");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Commissione.class,
                              Window.class,
                              "Commissione");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/caricacommissione.ui",
                              CaricaCommissione.class,
                              Window.class,
                              "Carica-commissione");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/tipologiaseduta.ui",
                              TipologiaSeduta.class,
                              FormTipologiaSeduta.class,
                              "Tipologia seduta");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/seduta.ui",
                              Seduta.class,
                              Window.class,
                              "Seduta");

        
        // Plugin CMIS per accedere ad Alfresco
        CMIS cmisPlugin = new CMIS();
        cmisPlugin.setup("127.0.0.1", 8080, "/alfresco/service/cmis", "admin", "admin", 
                "/Protocollo/${dataprotocollo,date,YYYY}/${dataprotocollo,date,MM}/${dataprotocollo,date,dd}/${iddocumento}/");
        Register.registerPlugin(cmisPlugin, FormProtocollo.class);

        CMIS cmisPluginPubblicazioni = new CMIS();
        cmisPluginPubblicazioni.setup("127.0.0.1", 8080, "/alfresco/service/cmis", "admin", "admin", 
                "/Pubblicazioni/${inizioconsultazione,date,YYYY}/${inizioconsultazione,date,MM}/${inizioconsultazione,date,dd}/${id}/");
        Register.registerPlugin(cmisPluginPubblicazioni, FormPubblicazione.class);
        
        // Plugin Barcode per la stampa del DataMatrix
        Barcode barcodePlugin = new Barcode();
        barcodePlugin.setup("lp -d Zebra_Technologies_ZTC_GK420t", ".\nS1\nb245,34,D,h6,\"0123456789\"\nP1\n.\n");
        Register.registerPlugin(barcodePlugin, FormProtocollo.class);

        // Plugin Ooops per interazione con OpenOffice
        Ooops ooopsPlugin = new Ooops();
        ooopsPlugin.setup("uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager");
        
        // template 1
        HashMap<String,String> rules = new HashMap();
        rules.put("oggetto", "return obj.getDescrizione()");
        RuleSet ruleSet = new RuleSet(rules);
        String url = "file:///Users/tiziano/NetBeansProjects/PyPaPi/plugins/PyPaPiOoops/template/test.ott";
        Template template = new Template(url, "Prova", "Template di prova", ruleSet);
        ooopsPlugin.addTemplate(template);
        
        // template 2
        HashMap<String,String> rules2 = new HashMap();        
        rules2.put("oggetto", "return obj.getDescrizione()+\"!!\"");
        RuleSet ruleSet2 = new RuleSet(rules2);
        Template template2 = new Template(url, "Prova 2", "(come sopra, ma con dei punti esclamativi)", ruleSet2);
        ooopsPlugin.addTemplate(template2);
        
        Register.registerPlugin(ooopsPlugin, FormPratica.class);

        // Plugin ExtraAttributes per le pratiche
        ExtraAttributes extraAttributesPlugin = new ExtraAttributes();
        Register.registerPlugin(extraAttributesPlugin, FormPratica.class);

        /* login */
        Login login = new Login();
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            //mdi.showMaximized();
            mdi.show();
            
            app.setCustomApplicationName("PyPaPi Suite");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2012<br/>");
            app.exec();
        }
    
    }
    
}
