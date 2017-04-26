/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.agency.business;

import io.github.victorhsr.pos.agency.entities.Package;
import java.util.List;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@DataSourceDefinition(
        name = "java:app/jdbc/agency",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://postgres-agency:5432/agency",
        user = "postgres",
        password = "123456")
@Stateless
public class PackageManager {

    @PersistenceContext
    private EntityManager em;

    public List<Package> getAll() {
        return em.createNativeQuery("SELECT * FROM Package", Package.class).getResultList();
    }

    public void persistPackage(Package myPackage) {
        em.persist(myPackage);
    }

    public Package getPackage(int id) {
        return em.find(Package.class, id);
    }

    public void atualizarPacote(Package myPackage) {
        em.merge(myPackage);
    }

    public void excludePackage(Package myPackage) {
        em.remove(em.merge(myPackage));
    }

}
