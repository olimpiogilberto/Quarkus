package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.acme.dto.VinculoRequest;
import org.acme.dto.VinculoResponse;
import org.acme.exception.ApiException;
import org.acme.model.Pessoa;
import org.acme.model.StatusVinculo;
import org.acme.model.Vinculo;
import org.acme.repository.VinculoRepository;

@ApplicationScoped
public class VinculoService {
    @Inject VinculoRepository vinculos;
    @Inject PessoaService pessoas;
    @Inject EmpresaService empresas;

    @Transactional
    public List<VinculoResponse> listar() {
        return vinculos.listAll().stream().map(VinculoResponse::from).toList();
    }

    @Transactional
    public VinculoResponse buscar(Long id) {
        return VinculoResponse.from(obter(id));
    }

    @Transactional
    public VinculoResponse criar(VinculoRequest request) {
        Vinculo vinculo = new Vinculo();
        aplicar(vinculo, request);
        validarVinculo(vinculo, null);
        vinculos.persist(vinculo);
        return VinculoResponse.from(vinculo);
    }

    @Transactional
    public VinculoResponse atualizar(Long id, VinculoRequest request) {
        Vinculo vinculo = obter(id);
        aplicar(vinculo, request);
        validarVinculo(vinculo, id);
        return VinculoResponse.from(vinculo);
    }

    @Transactional
    public void excluir(Long id) {
        vinculos.delete(obter(id));
    }

    public Vinculo obter(Long id) {
        return vinculos.findByIdOptional(id)
                .orElseThrow(() -> ApiException.notFound("Vínculo não encontrado: " + id));
    }

    @Transactional
    public List<Vinculo> ativosPorCpf(String cpf) {
        Pessoa pessoa = pessoas.obterPorCpf(cpf);
        return vinculos.findAtivosByPessoa(pessoa);
    }

    @Transactional
    public List<Vinculo> porCnpj(String cnpj) {
        empresas.obterPorCnpj(cnpj);
        return vinculos.findByCnpj(org.acme.validation.Documentos.digitos(cnpj));
    }

    private void aplicar(Vinculo vinculo, VinculoRequest request) {
        vinculo.pessoa = pessoas.obter(request.pessoaId());
        vinculo.empresa = empresas.obter(request.empresaId());
        vinculo.cargo = request.cargo();
        vinculo.salario = request.salario();
        vinculo.dataInicio = request.dataInicio();
        vinculo.dataFim = request.dataFim();
        vinculo.status = request.status();
    }

    private void validarVinculo(Vinculo vinculo, Long idAtual) {
        if (vinculo.dataInicio == null) {
            throw ApiException.badRequest("Data de início é obrigatória");
        }
        if (vinculo.dataFim != null && vinculo.dataFim.isBefore(vinculo.dataInicio)) {
            throw ApiException.badRequest("Data final não pode ser anterior à data inicial");
        }
        if (vinculo.status == StatusVinculo.ATIVO && vinculo.dataFim != null) {
            throw ApiException.badRequest("Vínculo ativo não pode ter data final");
        }
        if (vinculo.status == StatusVinculo.ENCERRADO && vinculo.dataFim == null) {
            throw ApiException.badRequest("Vínculo encerrado exige data final");
        }
        if (vinculo.status == StatusVinculo.ATIVO
                && vinculos.findAtivosByPessoa(vinculo.pessoa).stream()
                    .anyMatch(existente -> !existente.id.equals(idAtual))) {
            throw ApiException.conflict("Pessoa já possui vínculo ativo");
        }
    }
}
