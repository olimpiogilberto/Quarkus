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
import org.acme.dto.FaturamentoRequest;
import org.acme.service.FaturamentoService;

@Path("/api/empresas/{cnpj}/faturamentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FaturamentoResource {
    @Inject FaturamentoService service;
    @Context UriInfo uriInfo;

    @POST
    public Response criar(@PathParam("cnpj") String cnpj, @Valid FaturamentoRequest request) {
        var faturamento = service.criar(cnpj, request);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                .path("registro").path(faturamento.id().toString()).build()).entity(faturamento).build();
    }

    @GET
    public Response listar(@PathParam("cnpj") String cnpj) {
        return Response.ok(service.listar(cnpj)).build();
    }

    @PUT
    @Path("/registro/{id}")
    public Response atualizar(@PathParam("cnpj") String cnpj, @PathParam("id") Long id,
                              @Valid FaturamentoRequest request) {
        return Response.ok(service.atualizar(cnpj, id, request)).build();
    }

    @DELETE
    @Path("/registro/{id}")
    public Response excluir(@PathParam("cnpj") String cnpj, @PathParam("id") Long id) {
        service.excluir(cnpj, id);
        return Response.noContent().build();
    }
}
