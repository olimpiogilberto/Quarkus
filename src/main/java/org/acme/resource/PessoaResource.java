package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.acme.dto.EnderecoRequest;
import org.acme.dto.PessoaRequest;
import org.acme.service.PessoaService;

@Path("/api/pessoas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PessoaResource {
    @Inject PessoaService service;
    @Context UriInfo uriInfo;

    @POST
    public Response criar(@Valid PessoaRequest request) {
        var pessoa = service.criar(request);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(pessoa.id().toString()).build())
                .entity(pessoa).build();
    }

    @GET
    public Response listar() {
        return Response.ok(service.listar()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        return Response.ok(service.buscar(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid PessoaRequest request) {
        return Response.ok(service.atualizar(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.excluir(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/endereco")
    public Response atualizarEndereco(@PathParam("id") Long id, @Valid EnderecoRequest request) {
        return Response.ok(service.atualizarEndereco(id, request)).build();
    }

    @DELETE
    @Path("/{id}/endereco")
    public Response removerEndereco(@PathParam("id") Long id) {
        return Response.ok(service.removerEndereco(id)).build();
    }
}
