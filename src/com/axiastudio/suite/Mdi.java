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
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.IUIFile;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.base.BaseUtil;
import com.axiastudio.suite.base.entities.CambiaPassword;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.forms.FormCorreggiUfficiDetermina;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.PraticaUtil;
import com.axiastudio.suite.pratiche.forms.FormTipoPraticaTree;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.TitoloDelega;
import com.axiastudio.suite.protocollo.forms.FormContaDocumenti;
import com.axiastudio.suite.protocollo.forms.FormScrivania;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.axiastudio.suite.scanndo.ScanNDo;
import com.axiastudio.suite.urp.entities.Sportello;
import com.axiastudio.suite.urp.forms.TicketApplet;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Mdi extends QMainWindow implements IMdi {
    
    private static String ICON = "classpath:com/axiastudio/pypapi/ui/resources/pypapi32.png";
    private QMdiArea workspace;
    private QTreeWidget tree;
    private QMenu menuWindows;
    private QAction actionCloseAll;
    private QAction actionTile;
    private QAction actionCascade;
    private QAction actionNext;
    private QAction actionPrevious;
    private QAction actionSeparator;
    private QAction actionClose;
    private QSignalMapper windowMapper;
    private QTreeWidgetItem itemPassword;

    public Mdi(){
        this.setWindowIcon(new QIcon(ICON));
        this.createWorkspace();
        this.createTree();
        //this.createSystemTray();
        this.createMenu();
        this.workspace.setActivationOrder(QMdiArea.WindowOrder.ActivationHistoryOrder);

        Register.registerUtility(this, IMdi.class);

    }
    

    private void createMenu(){
        menuWindows = this.menuBar().addMenu("Finestre");
        actionClose = new QAction("Chiudi", this);
        actionClose.triggered.connect(this.workspace, "closeActiveSubWindow()");
        actionCloseAll = new QAction("Chiudi tutte", this);
        actionCloseAll.triggered.connect(this.workspace, "closeAllSubWindows()");
        actionTile = new QAction("Allinea", this);
        actionTile.triggered.connect(this.workspace, "tileSubWindows()");
        actionCascade = new QAction("Disponi a cascata", this);
        actionCascade.triggered.connect(this.workspace, "cascadeSubWindows()");
        actionNext = new QAction("Finestra successiva", this);
        actionNext.triggered.connect(this.workspace, "activateNextSubWindow()");
        actionPrevious = new QAction("Finestra precedente", this);
        actionPrevious.triggered.connect(this.workspace, "activatePreviousSubWindow()");
        actionSeparator = new QAction(this);
        actionSeparator.setSeparator(true);
        
        menuWindows.aboutToShow.connect(this, "refreshMenuWindows()");     
    }

    private void refreshMenuWindows(){
        
        menuWindows.clear();
        menuWindows.addAction(actionClose);
        menuWindows.addAction(actionCloseAll);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionTile);
        menuWindows.addAction(actionCascade);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionNext);
        menuWindows.addAction(actionPrevious);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionCloseAll);
        
        for( QMdiSubWindow subWindow: this.workspace.subWindowList() ){
            
            String title="";
            if( subWindow.widget() instanceof QMainWindow ){
                title = subWindow.widget().windowTitle();
            }
            if( subWindow.widget() instanceof QDialog ){
                title = subWindow.widget().windowTitle();
            }
            
            QAction action = menuWindows.addAction(title);
            action.setCheckable(true);
            action.setChecked(subWindow.equals(this.workspace.activeSubWindow()));
            action.triggered.connect(windowMapper, "map()");
            windowMapper.setMapping(action, subWindow);
        }
    }
    
    private void createSystemTray(){
        QMenu menu = new QMenu(this);
        menu.addAction("prova");
        QSystemTrayIcon trayIcon = new QSystemTrayIcon(new QIcon(ICON), this);
        trayIcon.setContextMenu(menu);
        trayIcon.show();
        trayIcon.showMessage("PyPaPi Suite", "Applicazione avviata.");
        
    }
    
    private void createWorkspace() {
        QSplitter splitter = new QSplitter();
        this.tree = new QTreeWidget(splitter);
        this.workspace = new QMdiArea(splitter);
        this.setCentralWidget(splitter);        
        this.workspace.subWindowActivated.connect(this, "refreshMenuWindows()");
        windowMapper = new QSignalMapper(this);
        windowMapper.mappedQObject.connect(this, "setActiveSubWindow(QObject)");
        this.workspace.setMinimumWidth(500);
    }
    
    private void setActiveSubWindow(QObject obj){
        this.workspace.setActiveSubWindow((QMdiSubWindow) obj);
    }
    
    private void createTree() {
        this.tree.setColumnCount(2);
        this.tree.setHeaderLabel("PyPaPi Suite");
        this.tree.setColumnHidden(1, true);
        
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);

        /* scrivania */
        QTreeWidgetItem itemScrivania = new QTreeWidgetItem(this.tree);
        itemScrivania.setText(0, "Scrivania");
        itemScrivania.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/house.png"));
        itemScrivania.setText(1, "SCRIVANIA");
//        itemScrivania.setDisabled(!autenticato.getOperatoreprotocollo());

        /* Protocollo informatico */
        QTreeWidgetItem itemProtocolloInformatico = new QTreeWidgetItem(this.tree);
        itemProtocolloInformatico.setText(0, "Protocollo informatico");
        this.tree.addTopLevelItem(itemProtocolloInformatico);
//        itemProtocolloInformatico.setDisabled(!autenticato.getOperatoreprotocollo());
        
        QTreeWidgetItem itemProtocollo = new QTreeWidgetItem(itemProtocolloInformatico);
        itemProtocollo.setText(0, "Protocollo");
        itemProtocollo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemProtocollo.setText(1, "com.axiastudio.suite.protocollo.entities.Protocollo");
        itemProtocollo.setText(2, "NEW");

        QTreeWidgetItem itemGestioneAnnullati = new QTreeWidgetItem(itemProtocolloInformatico);
        itemGestioneAnnullati.setText(0, "Protocolli da annullare");
        itemGestioneAnnullati.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemGestioneAnnullati.setText(1, "com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo");
        itemGestioneAnnullati.setText(2, "NAMEDQUERY:annullamentiRichiesti");
        Costante costanteUfficioAnnullati = SuiteUtil.trovaCostante("UFFICIO_ANNULLATI");
        Long idUfficioAnnullati = Long.parseLong(costanteUfficioAnnullati.getValore());
        Boolean inUfficioAnnullati = Boolean.FALSE;
        for( UfficioUtente ufficio: autenticato.getUfficioUtenteCollection()) {
            if (ufficio.getUfficio().getId().equals(idUfficioAnnullati) && ufficio.getRicerca()) {
                inUfficioAnnullati = Boolean.TRUE;
            }
        }
        Boolean permessoGestioneAnnullati = autenticato.getAttributoreprotocollo() && inUfficioAnnullati;
        itemGestioneAnnullati.setDisabled(!permessoGestioneAnnullati);

        QTreeWidgetItem itemTitolario = new QTreeWidgetItem(itemProtocolloInformatico);
        itemTitolario.setText(0, "Titolario");
        itemTitolario.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTitolario.setText(1, "TITOLARIO");

        QTreeWidgetItem itemPubblicazioni = new QTreeWidgetItem(itemProtocolloInformatico);
        itemPubblicazioni.setText(0, "Pubblicazioni");
        itemPubblicazioni.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemPubblicazioni.setText(1, "com.axiastudio.suite.pubblicazioni.entities.Pubblicazione");
        //itemPubblicazioni.setDisabled(true);

        QTreeWidgetItem itemScanNDo = new QTreeWidgetItem(itemProtocolloInformatico);
        itemScanNDo.setText(0, "Scan'n'do - marcatura rapida protocolli");
        itemScanNDo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemScanNDo.setText(1, "SCANNDO");
        itemScanNDo.setDisabled(!autenticato.getSpedisceprotocollo());

        QTreeWidgetItem itemContaDocumenti = new QTreeWidgetItem(itemProtocolloInformatico);
        itemContaDocumenti.setText(0, "Controllo documenti");
        itemContaDocumenti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemContaDocumenti.setText(1, "DOCUMENTI");

        /* Anagrafiche */
        QTreeWidgetItem itemAnagrafiche = new QTreeWidgetItem(this.tree);
        itemAnagrafiche.setText(0, "Anagrafiche");
        this.tree.addTopLevelItem(itemAnagrafiche);
        itemAnagrafiche.setDisabled(!autenticato.getOperatoreanagrafiche());

        QTreeWidgetItem itemSoggetti = new QTreeWidgetItem(itemAnagrafiche);
        itemSoggetti.setText(0, "Soggetti");
        itemSoggetti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemSoggetti.setText(1, "com.axiastudio.suite.anagrafiche.entities.Soggetto");
        itemSoggetti.setText(2, "NEW");
        QTreeWidgetItem itemGruppi = new QTreeWidgetItem(itemAnagrafiche);
        itemGruppi.setText(0, "Gruppi");
        itemGruppi.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemGruppi.setText(1, "com.axiastudio.suite.anagrafiche.entities.Gruppo");
        itemGruppi.setDisabled(!autenticato.getAmministratore());
        QTreeWidgetItem itemRelazioni = new QTreeWidgetItem(itemAnagrafiche);
        itemRelazioni.setText(0, "Relazioni");
        itemRelazioni.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemRelazioni.setText(1, "com.axiastudio.suite.anagrafiche.entities.Relazione");
        
        /* Pratiche */
        QTreeWidgetItem itemPraticheRoot = new QTreeWidgetItem(this.tree);
        itemPraticheRoot.setText(0, "Pratiche...");
        this.tree.addTopLevelItem(itemPraticheRoot);
        itemPraticheRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemPratiche = new QTreeWidgetItem(itemPraticheRoot);
        itemPratiche.setText(0, "Pratiche");
        itemPratiche.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemPratiche.setText(1, "com.axiastudio.suite.pratiche.entities.Pratica");
        itemPratiche.setText(2, "NEW");

        QTreeWidgetItem itemTipoPratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipoPratica.setText(0, "Tipo di pratica");
        itemTipoPratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemTipoPratica.setText(1, "com.axiastudio.suite.pratiche.entities.TipoPratica");
        itemTipoPratica.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemTipiPratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipiPratica.setText(0, "Struttura tipi di pratica");
        itemTipiPratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTipiPratica.setText(1, "TIPIPRATICA");

        /* Richieste */
        QTreeWidgetItem itemRichiesteRoot = new QTreeWidgetItem(this.tree);
        itemRichiesteRoot.setText(0, "Richieste...");
        this.tree.addTopLevelItem(itemRichiesteRoot);
        itemRichiesteRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemRichieste = new QTreeWidgetItem(itemRichiesteRoot);
        itemRichieste.setText(0, "Richieste");
        itemRichieste.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemRichieste.setText(1, "com.axiastudio.suite.richieste.entities.Richiesta");
        itemRichieste.setText(2, "NEW");

        /* Delibere e determine */
        QTreeWidgetItem itemDelibereDetermineRoot = new QTreeWidgetItem(this.tree);
        itemDelibereDetermineRoot.setText(0, "Delibere e determine");
        this.tree.addTopLevelItem(itemDelibereDetermineRoot);
        itemDelibereDetermineRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemDelibere = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDelibere.setText(0, "Delibere");
        itemDelibere.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDelibere.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDelibere.setText(2, "NEW");
        itemDelibere.setDisabled(true);

        QTreeWidgetItem itemSedute = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemSedute.setText(0, "Sedute");
        itemSedute.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemSedute.setText(1, "com.axiastudio.suite.sedute.entities.Seduta");
        itemSedute.setText(2, "NEW");
        itemSedute.setDisabled(true);

        QTreeWidgetItem separator = new QTreeWidgetItem(itemDelibereDetermineRoot);
        separator.setText(0, "--------");
        separator.setDisabled(true);

        QTreeWidgetItem itemDetermine = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDetermine.setText(0, "Determine");
        itemDetermine.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDetermine.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDetermine.setText(2, "NEW");

        GestoreDeleghe gestoreDeleghe = (GestoreDeleghe) Register.queryUtility(IGestoreDeleghe.class);
        QTreeWidgetItem itemDetermineVistoResp = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDetermineVistoResp.setText(0, "Visto responsabile");
        itemDetermineVistoResp.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDetermineVistoResp.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDetermineVistoResp.setText(2, "NAMEDQUERY:inAttesaDiVistoResp:autenticato,Integer,"+autenticato.getId());
        itemDetermineVistoResp.setDisabled(gestoreDeleghe.checkCarica("RESPONSABILE_DI_SERVIZIO").isEmpty());

        QTreeWidgetItem itemDetermineVistoRespDelega = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDetermineVistoRespDelega.setText(0, "Visto responsabile (delegato)");
        itemDetermineVistoRespDelega.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDetermineVistoRespDelega.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDetermineVistoRespDelega.setText(2, "NAMEDQUERY:inAttesaDiVistoRespDelegato:autenticato,Integer,"+autenticato.getId());
        itemDetermineVistoRespDelega.setDisabled(gestoreDeleghe.checkCarica("RESPONSABILE_DI_SERVIZIO").isEmpty());

        QTreeWidgetItem itemDetermineVistoBilancio = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDetermineVistoBilancio.setText(0, "Visto bilancio");
        itemDetermineVistoBilancio.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDetermineVistoBilancio.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        String faseVistoBilancio = SuiteUtil.trovaCostante("FASE_VISTO_BILANCIO").getValore();
        itemDetermineVistoBilancio.setText(2, "NAMEDQUERY:inAttesaDiVistoDiBilancio:idfase,Integer,"+faseVistoBilancio);
        itemDetermineVistoBilancio.setDisabled(gestoreDeleghe.checkCarica("RESPONSABILE_DI_BILANCIO").isEmpty() &&
                 !BaseUtil.utenteInUfficio(autenticato,
                         Integer.parseInt(SuiteUtil.trovaCostante("UFFICIO_RAGIONERIA_E_CONTABILITA").getValore()), true));

        /* Procedimento */
        QTreeWidgetItem itemProcedimentiRoot = new QTreeWidgetItem(this.tree);
        itemProcedimentiRoot.setText(0, "Procedimenti");
        this.tree.addTopLevelItem(itemProcedimentiRoot);
        itemProcedimentiRoot.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemProcedimento = new QTreeWidgetItem(itemProcedimentiRoot);
        itemProcedimento.setText(0, "Procedimenti");
        itemProcedimento.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemProcedimento.setText(1, "com.axiastudio.suite.procedimenti.entities.Procedimento");
        itemProcedimento.setText(2, "NEW");

        QTreeWidgetItem itemNorma = new QTreeWidgetItem(itemProcedimentiRoot);
        itemNorma.setText(0, "Norme");
        itemNorma.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemNorma.setText(1, "com.axiastudio.suite.procedimenti.entities.Norma");
        itemNorma.setText(2, "NEW");
        
        /* Configurazione sedute */
        QTreeWidgetItem itemConfigurazioneSeduteRoot = new QTreeWidgetItem(this.tree);
        itemConfigurazioneSeduteRoot.setText(0, "Configurazione sedute");
        this.tree.addTopLevelItem(itemConfigurazioneSeduteRoot);
        itemConfigurazioneSeduteRoot.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemCarica = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCarica.setText(0, "Cariche");
        itemCarica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCarica.setText(1, "com.axiastudio.suite.procedimenti.entities.Carica");

        QTreeWidgetItem itemCommissione = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCommissione.setText(0, "Commissioni");
        itemCommissione.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCommissione.setText(1, "com.axiastudio.suite.sedute.entities.Commissione");

        QTreeWidgetItem itemCaricaCommissione = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCaricaCommissione.setText(0, "Cariche-commissioni");
        itemCaricaCommissione.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCaricaCommissione.setText(1, "com.axiastudio.suite.sedute.entities.CaricaCommissione");
        
        QTreeWidgetItem itemTipoSeduta = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemTipoSeduta.setText(0, "Tipi di seduta");
        itemTipoSeduta.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemTipoSeduta.setText(1, "com.axiastudio.suite.sedute.entities.TipoSeduta");
        itemTipoSeduta.setDisabled(!autenticato.getAmministratore());

        /* URP */
        QTreeWidgetItem itemUrp = new QTreeWidgetItem(this.tree);
        itemUrp.setText(0, "URP");
        this.tree.addTopLevelItem(itemUrp);

        QTreeWidgetItem itemApplet = new QTreeWidgetItem(itemUrp);
        itemApplet.setText(0, "Applet");
        itemApplet.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemApplet.setText(1, "TICKET");
        itemApplet.setDisabled(!autenticato.getOperatoreurp());

        QTreeWidgetItem itemServizioAlCittadino = new QTreeWidgetItem(itemUrp);
        itemServizioAlCittadino.setText(0, "Servizo al cittadino");
        itemServizioAlCittadino.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemServizioAlCittadino.setText(1, "com.axiastudio.suite.urp.entities.ServizioAlCittadino");
        itemServizioAlCittadino.setDisabled(!autenticato.getSupervisoreurp());

        QTreeWidgetItem itemSportello = new QTreeWidgetItem(itemUrp);
        itemSportello.setText(0, "Sportelli");
        itemSportello.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemSportello.setText(1, "com.axiastudio.suite.urp.entities.Sportello");
        itemSportello.setDisabled(!autenticato.getSupervisoreurp());

        QTreeWidgetItem itemAperturaURP = new QTreeWidgetItem(itemUrp);
        itemAperturaURP.setText(0, "Aperture URP");
        itemAperturaURP.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemAperturaURP.setText(1, "com.axiastudio.suite.urp.entities.AperturaURP");
        itemAperturaURP.setDisabled(!autenticato.getSupervisoreurp());

        QTreeWidgetItem itemNotiziaURP = new QTreeWidgetItem(itemUrp);
        itemNotiziaURP.setText(0, "Notizie URP");
        itemNotiziaURP.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemNotiziaURP.setText(1, "com.axiastudio.suite.urp.entities.NotiziaURP");
        itemNotiziaURP.setDisabled(!autenticato.getSupervisoreurp());

        /* Amministrazione */
        QTreeWidgetItem itemAmministrazione = new QTreeWidgetItem(this.tree);
        itemAmministrazione.setText(0, "Amministrazione");
        this.tree.addTopLevelItem(itemAmministrazione);

        QTreeWidgetItem itemCostanti = new QTreeWidgetItem(itemAmministrazione);
        itemCostanti.setText(0, "Costanti");
        itemCostanti.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemCostanti.setText(1, "com.axiastudio.suite.generale.entities.Costante");
        itemCostanti.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemEtichette = new QTreeWidgetItem(itemAmministrazione);
        itemEtichette.setText(0, "Etichette");
        itemEtichette.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemEtichette.setText(1, "com.axiastudio.suite.generale.entities.Etichetta");
        itemEtichette.setDisabled(!autenticato.getAmministratore());
        
        QTreeWidgetItem itemUtenti = new QTreeWidgetItem(itemAmministrazione);
        itemUtenti.setText(0, "Utenti");
        itemUtenti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/user.png"));
        itemUtenti.setText(1, "com.axiastudio.suite.base.entities.Utente");
        itemUtenti.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemUffici = new QTreeWidgetItem(itemAmministrazione);
        itemUffici.setText(0, "Uffici");
        itemUffici.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemUffici.setText(1, "com.axiastudio.suite.base.entities.Ufficio");
        itemUffici.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemModelli = new QTreeWidgetItem(itemAmministrazione);
        itemModelli.setText(0, "Modelli");
        itemModelli.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemModelli.setText(1, "com.axiastudio.suite.modelli.entities.Modello");
        itemModelli.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemGiunte = new QTreeWidgetItem(itemAmministrazione);
        itemGiunte.setText(0, "Giunte");
        itemGiunte.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemGiunte.setText(1, "com.axiastudio.suite.base.entities.Giunta");
        itemGiunte.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemDeleghe = new QTreeWidgetItem(itemAmministrazione);
        itemDeleghe.setText(0, "Incarichi e deleghe");
        itemDeleghe.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemDeleghe.setText(1, "com.axiastudio.suite.procedimenti.entities.Delega");
        //itemDeleghe.setDisabled(!autenticato.getAmministratore());

        itemPassword = new QTreeWidgetItem(itemAmministrazione);
        itemPassword.setText(0, "Cambio password");
        itemPassword.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/key.png"));
        itemPassword.setText(1, "PASSWORD");

        QTreeWidgetItem itemCorreggiUffici = new QTreeWidgetItem(itemAmministrazione);
        itemCorreggiUffici.setText(0, "Correzione uffici determine");
        itemCorreggiUffici.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/note.png"));
        itemCorreggiUffici.setText(1, "UFFICIDETERMINA");
        itemCorreggiUffici.setDisabled(!autenticato.getAmministratore());

        this.tree.activated.connect(this, "runTask()");
        this.tree.setMinimumWidth(200);

    }
    
    private void runTask() {
        String formName = this.tree.currentItem().text(1);
        if (formName == null || formName.equals("")) {
            return;  // item di raggruppamento
        }

        String mode = this.tree.currentItem().text(2);
        /* cambio password */
        if( "PASSWORD".equals(formName) ){
            CambiaPassword passDlg = new CambiaPassword(this);
            int exec = passDlg.exec();
        } else if( "TITOLARIO".equals(formName) ){
            FormTitolario titolario = new FormTitolario();
            this.workspace.addSubWindow(titolario);
            int exec = titolario.exec();
        } else if( "TIPIPRATICA".equals(formName) ){
            FormTipoPraticaTree tipipratica = new FormTipoPraticaTree();
            this.workspace.addSubWindow(tipipratica);
            int exec = tipipratica.exec();
        } else if( "SCRIVANIA".equals(formName) ){
            FormScrivania form = new FormScrivania();
            this.workspace.addSubWindow(form);
            form.show();
        } else if( "DOCUMENTI".equals(formName) ){
            FormContaDocumenti form = new FormContaDocumenti();
            this.workspace.addSubWindow(form);
            form.show();
        } else if( "SCANNDO".equals(formName) ){
            ScanNDo form = new ScanNDo();
            this.workspace.addSubWindow(form);
            form.show();
        } else if( "UFFICIDETERMINA".equals(formName) ){
            FormCorreggiUfficiDetermina form = new FormCorreggiUfficiDetermina(
                    "classpath:com/axiastudio/suite/deliberedetermine/forms/correggiufficidetermina.ui",
                    Determina.class, "Correggi uffici determina");
            this.workspace.addSubWindow(form);
            form.show();
        } else if( "TICKET".equals(formName) ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Store sportelli = db.createController(Sportello.class).createFullStore();
            List<String> nomiSportelli = new ArrayList<String>();
            for( Object obj: sportelli ){
                Sportello sportello = (Sportello) obj;
                nomiSportelli.add(sportello.getDescrizione());
            }
            String item = QInputDialog.getItem(this, "Apertura sportello", "Scegli lo sportello da aprire",
                    nomiSportelli, 0, false);
            int i = nomiSportelli.indexOf(item);
            if( i != -1 ) {
                Sportello sportello = (Sportello) sportelli.get(i);
                TicketApplet form = new TicketApplet(sportello);
                //this.workspace.addSubWindow(form);
                form.show();
            }
        } else {
            /* form registrata */
            Window form=null;
            Class<? extends Window> formClass = (Class) Register.queryUtility(IForm.class, formName);
            String uiFile = (String) Register.queryUtility(IUIFile.class, formName);
            Class factory = (Class) Register.queryUtility(IFactory.class, formName);
            try {
                Constructor<? extends Window> constructor = formClass.getConstructor(String.class, Class.class, String.class);
                try {
                    form = constructor.newInstance(uiFile, factory, "");
                } catch (InstantiationException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            }
            // A store with a new element
            Store store = null;

            if( "NEW".equals(mode) ){
                store = new Store(new ArrayList<Object>());
                try {
                    Constructor entityConstructor = factory.getConstructor();
                    Object entity = entityConstructor.newInstance();
                    store.add(entity);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if( mode.startsWith("NAMEDQUERY") ){
                Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
                String[] split = mode.split(":");
                String namedQueryName = split[1];

                Database db = (Database) Register.queryUtility(IDatabase.class);
                EntityManagerFactory emf = db.getEntityManagerFactory();
                EntityManager em = emf.createEntityManager();
                TypedQuery namedQuery = em.createNamedQuery(namedQueryName, factory);

                // TODO: aggiungere qualche controllo?
                for( Integer i=2; i<split.length; i++){
                    String parameters = split[i];
                    String[] split1 = parameters.split(",");
                    String fieldName = split1[0];
                    String typeName = split1[1];
                    String stringValue = split1[2];
                    if( "Integer".equals(typeName) ){
                        Object value = Integer.parseInt(stringValue);
                        namedQuery = namedQuery.setParameter(fieldName, value);
                    }
                }

                List<?> resultList = namedQuery.getResultList();
                store = new Store(resultList);
            }

            if( store != null ){
                if ( store.size()>0 ) {
                    form.init(store);
                }
                else {
                    Util.warningBox(this, "Attenzione", "Nessun record trovato");
                    return;
                }
            } else {
                form.init();
            }
            this.workspace.addSubWindow(form);
            this.showForm(form);
            if( store != null ) {
                if ( store.size()==0 || store.get(0).hashCode() == 0 ) {
                    form.getContext().getDirty();
                }
            }
            this.menuWindows.addAction(form.toString());
        }
    }
    
    private void showForm(Window form) {
        if( this.workspace.subWindowList().size()>1 ){
            form.show();
        } else {
            form.showMaximized();
        }
    }

    public QTreeWidgetItem getItemPassword() {
        return itemPassword;
    }

    public void setItemPassword(QTreeWidgetItem itemPassword) {
        this.itemPassword = itemPassword;
    }

    @Override
    public QMdiArea getWorkspace() {
        return workspace;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        boolean iHaveToClose = true;
        for( QMdiSubWindow subWindow: this.workspace.subWindowList() ){
            if( subWindow.widget() instanceof QMainWindow ){
                subWindow.widget().setFocus();
                iHaveToClose = iHaveToClose && subWindow.close();
            }
        }
        if( iHaveToClose ) {
            super.closeEvent(event);
            return;
        }
        event.ignore();
    }
}
