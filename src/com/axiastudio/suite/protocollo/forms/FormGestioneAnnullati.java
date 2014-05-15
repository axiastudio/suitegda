package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 13/05/14
 * Time: 14:55
 */
public class FormGestioneAnnullati extends QMainWindow {

    private final QTableView annullati;
    private List<AnnullamentoProtocollo> selectionAnnullamentoProtocollo = new ArrayList<AnnullamentoProtocollo>();

    public FormGestioneAnnullati() {
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/gestioneannullati.ui"));
        loadUi(file);
        annullati = (QTableView) this.findChild(QTableView.class, "annullati");
        annullati.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        annullati.doubleClicked.connect(this, "apri()");
        popolaAnnullati();
    }

    private void loadUi(QFile uiFile){
        QMainWindow window = null;
        try {
            window = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: window.dynamicPropertyNames()){
            this.setProperty(name.toString(), window.property(name.toString()));
        }
        this.setCentralWidget(window.centralWidget());
        this.setWindowTitle(window.windowTitle());
    }

    private void popolaAnnullati() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        List<AnnullamentoProtocollo> annullamentiRichiesti =
                em.createNamedQuery("annullamentiRichiesti", AnnullamentoProtocollo.class).getResultList();

        List<Column> colonne = new ArrayList();
        colonne.add(new Column("datarichiesta", "Data", "Data di richiesta"));
        colonne.add(new Column("esecutorerichiesta", "Richiedente", "Esecutore della richiesta di annullamento"));
        colonne.add(new Column("motivazioneannullamento", "Motivazione", "Motivazione della richiesta di annullamento"));
        TableModel model = new TableModel(new Store(annullamentiRichiesti), colonne);
        annullati.clearSelection();
        model.setEditable(false);
        annullati.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        annullati.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
        annullati.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents);
        annullati.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents);
        annullati.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Stretch);
    }

    private void selectRows(QItemSelection selected, QItemSelection deselected){

        TableModel model = (TableModel) annullati.model();
        List<Integer> selectedIndexes = new ArrayList();
        List<Integer> deselectedIndexes = new ArrayList();
        for (QModelIndex i: selected.indexes()){
            if(!selectedIndexes.contains(i.row())){
                selectedIndexes.add(i.row());
            }
        }
        for (QModelIndex i: deselected.indexes()){
            if(!deselectedIndexes.contains(i.row())){
                deselectedIndexes.add(i.row());
            }
        }
        for (Integer idx: selectedIndexes){
            this.selectionAnnullamentoProtocollo.add((AnnullamentoProtocollo) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            this.selectionAnnullamentoProtocollo.remove(model.getEntityByRow(idx));
        }

        //refreshInfo();
    }

    private void apri(){
        AnnullamentoProtocollo annullamentoProtocollo = this.selectionAnnullamentoProtocollo.get(0);
        FormAnnullamentoProtocollo form = (FormAnnullamentoProtocollo) Util.formFromEntity(annullamentoProtocollo);
        if( form == null ){
            return;
        }
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow(form);
        }
        int res = form.exec();
        popolaAnnullati();
    }

}
