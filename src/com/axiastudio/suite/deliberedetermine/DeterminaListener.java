package com.axiastudio.suite.deliberedetermine;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.deliberedetermine.entities.Determina;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 12:31
 */
public class DeterminaListener {

    @PreUpdate
    void preUpdate(Determina determina) {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        if( determina.getVistoBilancio() && determina.getDataVistoBilancio() == null ){
            determina.setDataVistoBilancio(today);
        }
        if( determina.getVistoResponsabile() && determina.getDataVistoResponsabile() == null ){
            determina.setDataVistoResponsabile(today);
            int year = calendar.get(Calendar.YEAR);
            determina.setAnno(year);

            // cerchiamo il numero di determina
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Determina> cq = cb.createQuery(Determina.class);
            Root<Determina> root = cq.from(Determina.class);
            cq.select(root);
            cq.where(cb.equal(root.get("anno"), year));
            cq.orderBy(cb.desc(root.get("numero")));
            TypedQuery<Determina> tq = em.createQuery(cq).setMaxResults(1);
            Determina max;
            Integer numero;
            try {
                numero = tq.getSingleResult().getNumero()+1;
            } catch (NoResultException ex) {
                numero = 1;
            }
            determina.setNumero(numero);
        }
        if( determina.getVistoNegato() && determina.getDataVistoNegato() == null ){
            determina.setDataVistoNegato(today);
        }
    }

}
