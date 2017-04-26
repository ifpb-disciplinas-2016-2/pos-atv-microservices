/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.agency.entities;

import ifpb.pos.resources.adapters.DateAdapter;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Entity
@XmlRootElement
public class Package implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @XmlElement(required = true)
    private int client;
    private int hotelBooking;
    private int ticketBooking;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @Temporal(TemporalType.DATE)
    private Date date;

    public Package() {
    }

    public Package(int idCliente, int reservaHotel, int reservaPassagem) {
        this.client = idCliente;
        this.hotelBooking = reservaHotel;
        this.ticketBooking = reservaPassagem;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return client;
    }

    public void setIdCliente(int idClient) {
        this.client = idClient;
    }

    public int getReservaHotel() {
        return hotelBooking;
    }

    public void setHotelBooking(int hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public int getReservaPassagem() {
        return ticketBooking;
    }

    public void setTicketBooking(int ticketBooking) {
        this.ticketBooking = ticketBooking;
    }

    @Override
    public String toString() {
        return "client - " + client + ", hotel - " + hotelBooking + ", passagem - " + ticketBooking + ", date - " + date;
    }

}
