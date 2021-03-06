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
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.anagrafiche.entities.*;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormQuickInsertSoggetto extends QDialog implements IQuickInsertDialog {

    private Object entity=null;
    private Store storeStato;
    private Store storeProvincia;

    public FormQuickInsertSoggetto(QWidget parent) {
        super(parent);
        this.initLayout();
    }

    public FormQuickInsertSoggetto() {
        this(null);
    }
    
    private void initLayout(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/anagrafiche/forms/quickinsertsoggetto.ui"));
        this.loadUi(file);
        this.resize(500, 300);
    }
    
    private void loadUi(QFile uiFile){
        QDialog dialog = null;
        try {
            dialog = (QDialog) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: dialog.dynamicPropertyNames()){
            this.setProperty(name.toString(), dialog.property(name.toString()));
        }
        QToolButton buttonCancel = (QToolButton) dialog.findChild(QToolButton.class, "buttonCancel");
        buttonCancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        buttonCancel.clicked.connect(this, "reject()");
        QToolButton buttonAccept = (QToolButton) dialog.findChild(QToolButton.class, "buttonAccept");
        buttonAccept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        buttonAccept.clicked.connect(this, "insertAndAccept()");       
        this.setLayout(dialog.layout());
        
        // provincia
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Provincia.class);
        PyPaPiComboBox comboBoxProvincia = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_provincia");
        comboBoxProvincia.clear();
        storeProvincia = controller.createFullStore();
        storeProvincia.sortByToString();
        comboBoxProvincia.setLookupStore(storeProvincia);
        comboBoxProvincia.setCurrentIndex(comboBoxProvincia.findText("n.d."));
        
        // stato
        controller = db.createController(Stato.class);
        PyPaPiComboBox comboBoxStato = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_stato");
        comboBoxStato.clear();
        storeStato = controller.createFullStore();
        storeStato.sortByToString();
        comboBoxStato.setLookupStore(storeStato);
        comboBoxStato.setCurrentIndex(comboBoxStato.findText("n.d."));
    }
        
    private void insertAndAccept(){
        QTabWidget tabs = (QTabWidget) this.findChild(QTabWidget.class, "tabs");
        int idx = tabs.currentIndex();
        Soggetto s = new Soggetto();
        if( idx == 0 ){
            List<SessoSoggetto> sessi = new ArrayList();
            sessi.add(SessoSoggetto.ND);
            sessi.add(SessoSoggetto.M);
            sessi.add(SessoSoggetto.F);
            s.setTipo(TipoSoggetto.PERSONA);
            s.setNome(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_nome")).text());
            s.setCognome(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_cognome")).text());
            s.setCodicefiscale(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiscaleP")).text());
            s.setSessosoggetto(sessi.get(((QComboBox) this.findChild(QComboBox.class, "comboBox_sesso")).currentIndex()));
        } else if( idx == 1 ){
            s.setTipo(TipoSoggetto.AZIENDA);
            s.setRagionesociale(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_ragionesociale")).text());
            s.setCodicefiscale(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiscaleA")).text());
            s.setPartitaiva(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_partitaiva")).text());
        } else if( idx == 2 ){
            s.setTipo(TipoSoggetto.ENTE);
            s.setDenominazione(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_denominazione")).text());
            s.setDenominazione2(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_denominazione2")).text());
            s.setDenominazione3(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_denominazione3")).text());
            s.setCodicefiscale(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiscaleE")).text());
        }
        List<Indirizzo> indirizzi = new ArrayList();
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_via")).text());
        indirizzo.setCivico(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_civico")).text());
        indirizzo.setCap(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_cap")).text());
        indirizzo.setFrazione(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_frazione")).text());
        indirizzo.setComune(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_comune")).text());
        indirizzo.setDescrizione(((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_descrizione")).text());
        indirizzo.setProvincia((Provincia) storeProvincia.get(((QComboBox) this.findChild(QComboBox.class, "comboBox_provincia")).currentIndex()));
        indirizzo.setTipo(TipoIndirizzo.valueOf(((QComboBox) this.findChild(QComboBox.class, "comboBox_tipo")).currentText()));
        indirizzo.setStato((Stato) storeStato.get(((QComboBox) this.findChild(QComboBox.class, "comboBox_stato")).currentIndex()));
        indirizzo.setPrincipale(Boolean.TRUE);
        indirizzi.add(indirizzo);
        s.setIndirizzoCollection(indirizzi);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Soggetto.class);
        Validation res = controller.commit(s);
        if( res.getResponse() ) {
            this.entity = s;
            this.accept();
        } else {
            QMessageBox.critical(this, "Errore nell'inserimento", res.getMessage());
        }
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public int exec() {
        return super.exec();
    }
    
}
