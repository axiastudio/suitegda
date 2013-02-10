/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.db.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class InitDB {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
         * 1. dropdb suite && createdb suite
         * 
         * 2. imposta configurazione su "Local Postgresql"
         * 
         * 3. esecuzione di questo main
         * 
         * 4. nice -n 10 time python import.py | psql  suite
         * 
         */
        
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
        properties.put("eclipselink.ddl-generation", "create-tables");

        Database db = new Database();
        db.open("SuitePU", properties);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        
        // inizializza gli schemi
        List<String> schema = new ArrayList();
        schema.add("BASE");
        schema.add("ANAGRAFICHE");
        schema.add("PROTOCOLLO");
        schema.add("PROCEDIMENTI");
        schema.add("PRATICHE");
        schema.add("SEDUTE");
        for( String name: schema){
            try {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/suite","pypapidev","");
                Statement st = conn.createStatement();
                st.executeUpdate("DROP SCHEMA IF EXISTS " + name + ";");
                st.executeUpdate("CREATE SCHEMA " + name + ";");
                st.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Suite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        EntityManager em = emf.createEntityManager();
        
        System.out.println("Done.");
    }
            }
