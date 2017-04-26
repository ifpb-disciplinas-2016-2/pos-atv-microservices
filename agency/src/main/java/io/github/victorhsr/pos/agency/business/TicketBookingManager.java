/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.agency.business;

import com.google.gson.JsonObject;
import io.github.victorhsr.pos.agency.utils.DateUtils;
import io.github.victorhsr.pos.agency.utils.JsonUtils;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Stateless
public class TicketBookingManager {

    private final Client client = ClientBuilder.newClient();
    private final WebTarget ticketBookingWt = client.target("http://ticket-service:8080/ticket/ws/reservas");

    public void updateTicketBooking(int id, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", date);

        WebTarget target = ticketBookingWt.path("{id}");

        target.resolveTemplate("id", id).request().put(Entity.json(jsonObject.toString()));
    }

    public void updateTicketBooking(int id, Date date) {
        updateTicketBooking(id, DateUtils.dateToString(date));
    }

    public int createTicketBooking(int idClient, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("client", idClient);

        Response response = ticketBookingWt.request().post(Entity.json(jsonObject.toString()));

        String ticketBookingJson = response.readEntity(String.class);

        return JsonUtils.getJsonElementFromString(ticketBookingJson, "id").getAsInt();
    }

    public int createTicketBooking(int idClient, Date date) {
        return createTicketBooking(idClient, DateUtils.dateToString(date));
    }

    public void excludeTicketBooking(int hotelBooking) {
        ticketBookingWt.path("{id}").resolveTemplate("id", hotelBooking).request().delete();
    }

}
