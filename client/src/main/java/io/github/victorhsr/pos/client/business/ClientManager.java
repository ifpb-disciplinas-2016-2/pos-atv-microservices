/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.client.business;

import io.github.victorhsr.pos.client.entities.Client;
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
        name = "java:app/jdbc/client",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://postgres-client:5432/client",
        user = "postgres",
        password = "123456")
@Stateless
public class ClientManager {

    @PersistenceContext
    private EntityManager em;

    public List<Client> getAll() {
        return em.createNativeQuery("SELECT * FROM Client", Client.class).getResultList();
    }

    public void persistClient(Client client) {
        em.persist(client);
    }

    public Client getClient(int id) {
        return em.find(Client.class, id);
    }

    public void updateClient(Client client) {
        em.merge(client);
    }

    public void excludeClient(Client client) {
        em.remove(em.merge(client));
    }
}
