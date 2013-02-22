/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.Pratica_;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PraticaCallbacks {
    
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation validaPratica(Pratica pratica){
        if( pratica.getId() == null ){
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            Date date = calendar.getTime();
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pratica> cq = cb.createQuery(Pratica.class);
            Root<Pratica> root = cq.from(Pratica.class);
            cq.select(root);
            cq.where(cb.equal(root.get(Pratica_.anno), year));
            cq.orderBy(cb.desc(root.get("idpratica")));
            TypedQuery<Pratica> tq = em.createQuery(cq).setMaxResults(1);
            Pratica max;
            pratica.setDatapratica(date);
            pratica.setAnno(year);
            try {
                max = tq.getSingleResult();
            } catch (NoResultException ex) {
                max=null;
            }
            String newIdPratica;
            if( max != null ){
                Integer i = Integer.parseInt(max.getIdPratica().substring(4));
                i++;
                newIdPratica = year+String.format("%08d", i);
            } else {
                newIdPratica = year+"00000001";
            }
            pratica.setIdPratica(newIdPratica);
        }
        return new Validation(true);
    }
}
