/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.base;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Login extends QDialog {
    private final QLineEdit username;
    private final QLineEdit password;
    private Utente utente;

    public Login() {
        super();
        QVBoxLayout layout = new QVBoxLayout();
        QGridLayout gridLayout = new QGridLayout();
        QLabel labelUsername = new QLabel("Utente");
        gridLayout.addWidget(labelUsername, 0, 0);
        this.username = new QLineEdit();
        gridLayout.addWidget(username, 0, 1);
        QLabel labelPassword = new QLabel("Password");
        gridLayout.addWidget(labelPassword, 1, 0);
        this.password = new QLineEdit();
        this.password.setEchoMode(QLineEdit.EchoMode.Password);
        gridLayout.addWidget(password, 1, 1);
        QPushButton pushButtonCancel = new QPushButton("Annulla");
        gridLayout.addWidget(pushButtonCancel, 2, 0);
        QPushButton pushButtonOk = new QPushButton("OK");
        gridLayout.addWidget(pushButtonOk, 2, 1);
        QLabel logo = new QLabel();
        logo.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/pypapi128.png"));
        layout.addWidget(logo, 0, Qt.AlignmentFlag.AlignCenter);
        layout.addLayout(gridLayout);
        this.setLayout(layout);
        pushButtonOk.clicked.connect(this, "accept()");
        pushButtonCancel.clicked.connect(this, "reject()");
        pushButtonOk.setDefault(true);
    }
    
    @Override
    public void accept() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root from = cq.from(Utente.class);
        cq.select(from);
        Predicate predicate = cb.equal(from.get("login"), this.username.text());
        cq = cq.where(predicate);
        Query q = em.createQuery(cq);
        List entities = q.getResultList();
        if( entities.size() == 1 ){
            utente = (Utente) entities.get(0);
            String pwd = this.password.text();
            if( SuiteUtil.digest(pwd).equals(utente.getPassword()) ){
                Register.registerUtility(utente, IUtente.class);
                super.accept();
                return;
            }
        }
        QMessageBox.critical(this, "Utente o password errati", "Il nome utente o la password risultano errati.");
    }

}