/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.ticket.resources;

import io.github.victorhsr.pos.ticket.utils.DateUtils;
import io.github.victorhsr.pos.ticket.utils.JsonUtils;
import io.github.victorhsr.pos.ticket.entities.Ticket;
import io.github.victorhsr.pos.ticket.business.TicketManager;
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
public class TicketResource {

    @EJB
    private TicketManager ticketManager;
    @Context
    private UriInfo uriInfo;
    private final Link selfLink = Link.fromUri("http://localhost:8080/ticket/ws/reservas").rel("self").build();
    private final Link clientLink = Link.fromUri("http://localhost:8083/agency/ws/clientes").rel("self").build();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Ticket> tickets = ticketManager.getAll();

        GenericEntity<List<Ticket>> genericTickets = new GenericEntity<List<Ticket>>(tickets) {
        };

        return Response.ok().entity(genericTickets).links(selfLink, clientLink).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(Ticket ticket) {

        if (ticket == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (ticket.getDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();
        }

        ticketManager.persistTicket(ticket);

        URI resourceUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(ticket.getId())).build();

        return Response.created(resourceUri).entity(ticket).links(selfLink, clientLink).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(@PathParam("id") int id) {

        Ticket ticket = ticketManager.getTicket(id);

        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(ticket).links(selfLink, clientLink).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(@PathParam("id") int id, String jsonDate) {

        Ticket ticket = ticketManager.getTicket(id);

        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Response invalidDatePatternResponse = Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format, use the pattern dd/MM/yyyy").build();

        if (jsonDate == null || jsonDate.trim().isEmpty()) {

            return invalidDatePatternResponse;
        }

        try {

            jsonDate = JsonUtils.getJsonElementFromString(jsonDate, "date").getAsString();

            Date newDate = DateUtils.stringToDate(jsonDate);

            ticket.setDate(newDate);
            ticketManager.updateTicket(ticket);

            return Response.ok().entity(ticket).links(selfLink, clientLink).build();
        } catch (Exception ex) {
            return invalidDatePatternResponse;
        }

    }

    @DELETE
    @Path("/{id}")
    public Response excludeTicket(@PathParam("id") int id) {

        Ticket ticket = ticketManager.getTicket(id);

        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ticketManager.excludeTicket(ticket);

        return Response.ok().build();
    }

}
