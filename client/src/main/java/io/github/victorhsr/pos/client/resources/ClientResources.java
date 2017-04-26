/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.client.entities.resources;

import io.github.victorhsr.pos.client.business.ClientManager;
import io.github.victorhsr.pos.client.entities.Client;
import java.net.URI;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Path("/clientes")
@Stateless
public class ClientResources {

    @EJB
    private ClientManager clientManager;
    @Context
    private UriInfo uriInfo;
    private final Link selfLink = Link.fromUri("http://localhost:8083/client/ws/clientes").rel("self").build();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Client> clients = clientManager.getAll();

        GenericEntity<List<Client>> genericClients = new GenericEntity<List<Client>>(clients) {
        };

        return Response.ok().entity(genericClients).links(selfLink).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClient(Client client) {

        if (client == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        clientManager.persistClient(client);

        URI resourceUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(client.getId())).build();

        return Response.created(resourceUri).entity(client).links(selfLink).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") int id) {

        Client client = clientManager.getClient(id);

        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(client).links(selfLink).build();
    }

    @PUT
    @Path("/{id}/nome/{novo_nome}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(@PathParam("id") int id, @PathParam("novo_nome") String novoNome) {

        Client client = clientManager.getClient(id);

        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (novoNome == null || novoNome.trim().isEmpty()) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid name").build();
        }

        client.setName(novoNome);
        clientManager.updateClient(client);

        return Response.ok().entity(client).links(selfLink).build();
    }

    @DELETE
    @Path("/{id}")
    public Response excludeClient(@PathParam("id") int id) {

        Client client = clientManager.getClient(id);

        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        clientManager.excludeClient(client);

        return Response.ok().build();
    }

}
