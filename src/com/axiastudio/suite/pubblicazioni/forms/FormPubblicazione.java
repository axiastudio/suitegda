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
package com.axiastudio.suite.pubblicazioni.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiDateEdit;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.trolltech.qt.core.QDate;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSpinBox;

import java.util.GregorianCalendar;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormPubblicazione extends Window {
    
    public FormPubblicazione(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        QSpinBox n_giorni = (QSpinBox) findChild(QSpinBox.class, "spinBox_n_giorni");
        n_giorni.valueChanged.connect(this, "nGiorniChanged(int)");
        PyPaPiDateEdit dateEdit_inizio = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_inizio");
        dateEdit_inizio.dateChanged.connect(this, "dataInizioChanged(QDate)");
        PyPaPiDateEdit dateEdit_fine = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_fine");
        dateEdit_fine.dateChanged.connect(this, "dataFineChanged(QDate)");

        QPushButton pubblica = (QPushButton) findChild(QPushButton.class, "pushButton_pubblica");
        pubblica.clicked.connect(this, "pubblicaOra()");

    }

    private void nGiorniChanged(int i){
        // sposta la fine della consultazione
        PyPaPiDateEdit dateEdit_inizio = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_inizio");
        QDate inizio = dateEdit_inizio.date();
        PyPaPiDateEdit dateEdit_fine = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_fine");
        QDate fine = inizio.addDays(i);
        dateEdit_fine.setDate(fine);
        Pubblicazione pubblicazione = (Pubblicazione) this.getContext().getCurrentEntity();
        if( pubblicazione != null ) {
            GregorianCalendar gc = new GregorianCalendar(fine.year(), fine.month()-1, fine.day());
            pubblicazione.setFineconsultazione(gc.getTime());
        }
    }

    private void dataInizioChanged(QDate date){
        // sposta la fine della consultazione
        QSpinBox n_giorni = (QSpinBox) findChild(QSpinBox.class, "spinBox_n_giorni");
        int n = n_giorni.value();
        PyPaPiDateEdit dateEdit_fine = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_fine");
        QDate fine = date.addDays(n);
        dateEdit_fine.setDate(fine);
        Pubblicazione pubblicazione = (Pubblicazione) this.getContext().getCurrentEntity();
        if( pubblicazione != null ) {
            GregorianCalendar gc = new GregorianCalendar(fine.year(), fine.month()-1, fine.day());
            pubblicazione.setFineconsultazione(gc.getTime());
        }
    }

    private void dataFineChanged(QDate date){
        // cambia la durata di consultazione
        PyPaPiDateEdit dateEdit_inizio = (PyPaPiDateEdit) findChild(PyPaPiDateEdit.class, "dateEdit_inizio");
        int n = dateEdit_inizio.date().daysTo(date);
        QSpinBox n_giorni = (QSpinBox) findChild(QSpinBox.class, "spinBox_n_giorni");
        n_giorni.setValue(n);
        Pubblicazione pubblicazione = (Pubblicazione) this.getContext().getCurrentEntity();
        if( pubblicazione != null ) {
            pubblicazione.setDurataconsultazione(n);
        }
    }

    public void pubblicaOra(){
        // da implementare per personalizzazione
    }
}
