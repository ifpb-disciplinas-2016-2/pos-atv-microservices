/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.ticket.business;

import io.github.victorhsr.pos.ticket.entities.Ticket;
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
        name = "java:app/jdbc/ticket",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://postgres-ticket:5432/ticket",
        user = "postgres",
        password = "123456")
@Stateless
public class TicketManager {

    @PersistenceContext
    private EntityManager em;

    public List<Ticket> getAll() {
        return em.createNativeQuery("SELECT * FROM Ticket", Ticket.class).getResultList();
    }

    public void persistTicket(Ticket ticket) {
        em.persist(ticket);
    }

    public Ticket getTicket(int id) {
        return em.find(Ticket.class, id);
    }

    public void updateTicket(Ticket ticket) {
        em.merge(ticket);
    }

    public void excludeTicket(Ticket ticket) {
        em.remove(em.merge(ticket));
    }

}
