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
public class HotelBookingManager {

    private final Client client = ClientBuilder.newClient();
    private final WebTarget hotelBookingWt = client.target("http://hotel-service:8080/hotel/ws/reservas");

    public void updateHotelBooking(int id, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", date);

        WebTarget target = hotelBookingWt.path("{id}");

        target.resolveTemplate("id", id).request().put(Entity.json(jsonObject.toString()));
    }

    public void updateHotelBooking(int id, Date date) {
        updateHotelBooking(id, DateUtils.dateToString(date));
    }

    public int createHotelBooking(int idClient, Date bookingDate) {
        return createHotelBooking(idClient, DateUtils.dateToString(bookingDate));
    }

    public int createHotelBooking(int idClient, String bookingDate) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("client", idClient);
        jsonObject.addProperty("date", bookingDate);
        String json = jsonObject.toString();

        Response response = hotelBookingWt.request().post(Entity.json(json));

        String hotelBookingJson = response.readEntity(String.class);

        return JsonUtils.getJsonElementFromString(hotelBookingJson, "id").getAsInt();
    }

    public void excludeHotelPackage(int hotelBooking) {
        hotelBookingWt.path("{id}").resolveTemplate("id", hotelBooking).request().delete();
    }

}
