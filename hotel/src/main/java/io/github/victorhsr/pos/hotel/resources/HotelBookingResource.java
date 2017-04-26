/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.hotel.resources;

import io.github.victorhsr.pos.hotel.entities.HotelBooking;
import io.github.victorhsr.pos.hotel.business.HotelBookingManager;
import io.github.victorhsr.pos.hotel.utils.DateUtils;
import io.github.victorhsr.pos.hotel.utils.JsonUtils;
import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Path("/reservas")
public class HotelBookingResource {

    @EJB
    private HotelBookingManager hotelBookingManager;
    @Context
    private UriInfo uriInfo;
    private final Link selfLink = Link.fromUri("http://localhost:8081/hotel/ws/reservas").rel("self").build();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<HotelBooking> bookings = hotelBookingManager.getAll();

        GenericEntity<List<HotelBooking>> genericBookings = new GenericEntity<List<HotelBooking>>(bookings) {
        };

        return Response.ok().entity(genericBookings).links(selfLink).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createHotelBooking(HotelBooking hotelBooking) {

        if (hotelBooking == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (hotelBooking.getDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();
        }

        hotelBookingManager.persistHotelBooking(hotelBooking);

        URI resourceUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(hotelBooking.getId())).build();

        return Response.created(resourceUri).entity(hotelBooking).links(selfLink).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHotelBooking(@PathParam("id") int id) {

        HotelBooking booking = hotelBookingManager.getHotelBooking(id);

        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(booking).links(selfLink).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHotelBooking(@PathParam("id") int id, String jsonDate) {

        HotelBooking booking = hotelBookingManager.getHotelBooking(id);

        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Response invalidDatePatterResponse = Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();

        if (jsonDate == null || jsonDate.trim().isEmpty()) {

            return invalidDatePatterResponse;
        }

        try {

            jsonDate = JsonUtils.getJsonElementFromString(jsonDate, "date").getAsString();

            Date newDate = DateUtils.stringToDate(jsonDate);

            booking.setDate(newDate);
            hotelBookingManager.atualizarReservaHotel(booking);

            return Response.ok().entity(booking).links(selfLink).build();
        } catch (Exception ex) {
            return invalidDatePatterResponse;
        }

    }

    @DELETE
    @Path("/{id}")
    public Response excludeHotelBooking(@PathParam("id") int id) {

        HotelBooking booking = hotelBookingManager.getHotelBooking(id);

        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        hotelBookingManager.excluirReservaHotel(booking);

        return Response.ok().build();
    }

}
