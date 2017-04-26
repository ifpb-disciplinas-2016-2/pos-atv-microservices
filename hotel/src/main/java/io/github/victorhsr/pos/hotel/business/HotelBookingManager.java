/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.hotel.business;

import io.github.victorhsr.pos.hotel.entities.HotelBooking;
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
        name = "java:app/jdbc/hotel",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://postgres-hotel:5432/hotel",
        user = "postgres",
        password = "123456")
@Stateless
public class HotelBookingManager {

    @PersistenceContext
    private EntityManager em;

    public List<HotelBooking> getAll() {
        return em.createNativeQuery("SELECT * FROM HotelBooking", HotelBooking.class).getResultList();
    }

    public void persistHotelBooking(HotelBooking hotelBooking) {
        em.persist(hotelBooking);
    }

    public HotelBooking getHotelBooking(int id) {
        return em.find(HotelBooking.class, id);
    }

    public void atualizarReservaHotel(HotelBooking hotelBooking) {
        em.merge(hotelBooking);
    }

    public void excluirReservaHotel(HotelBooking hotelBooking) {
        em.remove(em.merge(hotelBooking));
    }

}
