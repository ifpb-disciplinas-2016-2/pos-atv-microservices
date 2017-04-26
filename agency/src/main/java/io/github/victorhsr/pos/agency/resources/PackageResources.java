/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.agency.resources;

import io.github.victorhsr.pos.agency.entities.Package;
import io.github.victorhsr.pos.agency.business.PackageManager;
import io.github.victorhsr.pos.agency.business.HotelBookingManager;
import io.github.victorhsr.pos.agency.business.TicketBookingManager;
import io.github.victorhsr.pos.agency.utils.DateUtils;
import io.github.victorhsr.pos.agency.utils.JsonUtils;
import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Path("/pacotes")
@Stateless
public class PackageResources {

    @EJB
    private PackageManager packageManager;
    @EJB
    private TicketBookingManager ticketBookingManager;
    @EJB
    private HotelBookingManager hotelBookingManager;
    @Context
    private UriInfo uriInfo;
    private final Link hotelLink = Link.fromUri("http://localhost:8081/hotel/ws/reservas").rel("hotel").build();
    private final Link ticketLink = Link.fromUri("http://localhost:8080/ticket/ws/reservas").rel("passagem").build();
    private final Link selfLink = Link.fromUri("http://localhost:8082/agency/ws/pacotes").rel("self").build();
    private final Link clientLink = Link.fromUri("http://localhost:8083/agency/ws/clientes").rel("self").build();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Package> packages = packageManager.getAll();

        GenericEntity<List<Package>> genericPackages = new GenericEntity<List<Package>>(packages) {
        };

        return Response.ok().entity(genericPackages).links(selfLink, ticketLink, hotelLink, clientLink).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPackage(Package myPackage) {

        if (myPackage == null) {
            return Response.status(Status.FORBIDDEN).build();
        }

        if (myPackage.getDate() == null) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();
        }

        final int ticketBooking = ticketBookingManager.createTicketBooking(myPackage.getIdCliente(), myPackage.getDate());
        final int hotelBooking = hotelBookingManager.createHotelBooking(myPackage.getIdCliente(), myPackage.getDate());

        myPackage.setHotelBooking(hotelBooking);
        myPackage.setTicketBooking(ticketBooking);
        packageManager.persistPackage(myPackage);

        URI resourceUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(myPackage.getId())).build();

        return Response.created(resourceUri).entity(myPackage).links(selfLink, hotelLink, ticketLink, clientLink).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPackage(@PathParam("id") int id) {

        Package myPackage = packageManager.getPackage(id);

        if (myPackage == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(myPackage).links(selfLink, hotelLink, ticketLink, clientLink).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePackage(@PathParam("id") int id, String jsonDate) {

        Package myPackage = packageManager.getPackage(id);

        if (myPackage == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        final Response invalidDatePatternResponse = Response.status(Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();

        if (jsonDate == null || jsonDate.trim().isEmpty()) {

            return invalidDatePatternResponse;
        }

        try {

            jsonDate = JsonUtils.getJsonElementFromString(jsonDate, "date").getAsString();

            ticketBookingManager.updateTicketBooking(myPackage.getReservaPassagem(), jsonDate);
            hotelBookingManager.updateHotelBooking(myPackage.getReservaHotel(), jsonDate);

            Date newDate = DateUtils.stringToDate(jsonDate);

            myPackage.setDate(newDate);
            packageManager.atualizarPacote(myPackage);

            return Response.ok().entity(myPackage).links(selfLink, hotelLink, ticketLink, clientLink).build();
        } catch (Exception ex) {
            return invalidDatePatternResponse;
        }

    }

    @DELETE
    @Path("/{id}")
    public Response excludePackage(@PathParam("id") int id) {

        Package myPackage = packageManager.getPackage(id);

        if (myPackage == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        hotelBookingManager.excludeHotelPackage(myPackage.getReservaHotel());
        ticketBookingManager.excludeTicketBooking(myPackage.getReservaPassagem());
        packageManager.excludePackage(myPackage);

        return Response.ok().build();
    }

}
