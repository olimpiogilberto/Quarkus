package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import java.util.List;
import org.acme.dto.EmpresaResponse;
import org.acme.dto.EmpresaVinculoResponse;
import org.acme.dto.VinculoResponse;
import org.acme.exception.ApiException;
import org.acme.model.Vinculo;
import org.acme.service.FaturamentoService;
import org.acme.service.VinculoService;

@Path("/api/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class ConsultaResource {
    @Inject VinculoService vinculos;
    @Inject FaturamentoService faturamentos;

    @GET
    @Path("/empresa-por-cpf/{cpf}")
    public Response empresaPorCpf(@PathParam("cpf") String cpf) {
        List<Vinculo> ativos = vinculos.ativosPorCpf(cpf);
        if (ativos.isEmpty()) {
            throw ApiException.notFound("Pessoa não possui vínculo ativo");
        }
        Vinculo vinculo = ativos.get(0);
        return Response.ok(new EmpresaVinculoResponse(
                EmpresaResponse.from(vinculo.empresa), VinculoResponse.from(vinculo))).build();
    }

    @GET
    @Path("/empregados/{cnpj}/contagem")
    public Response contarEmpregados(@PathParam("cnpj") String cnpj) {
        List<Vinculo> ativos = vinculos.porCnpj(cnpj).stream()
                .filter(v -> v.status == org.acme.model.StatusVinculo.ATIVO)
                .toList();
        return Response.ok(ativos.size()).build();
    }

    @GET
    @Path("/salario/{cpf}")
    public Response salario(@PathParam("cpf") String cpf) {
        List<Vinculo> ativos = vinculos.ativosPorCpf(cpf);
        if (ativos.isEmpty()) {
            throw ApiException.notFound("Pessoa não possui vínculo ativo");
        }
        if (ativos.get(0).salario == null) {
            throw ApiException.notFound("Vínculo ativo não possui salário informado");
        }
        return Response.ok(ativos.get(0).salario).build();
    }

    @GET
    @Path("/faturamento/{cnpj}")
    public Response faturamento(@PathParam("cnpj") String cnpj, @QueryParam("ano") Integer ano) {
        if (ano == null) {
            throw ApiException.badRequest("O parâmetro ano é obrigatório");
        }
        return Response.ok(faturamentos.buscar(cnpj, ano)).build();
    }
}
