package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.acme.dto.FaturamentoRequest;
import org.acme.dto.FaturamentoResponse;
import org.acme.exception.ApiException;
import org.acme.model.Empresa;
import org.acme.model.Faturamento;
import org.acme.repository.FaturamentoRepository;
import org.acme.validation.Documentos;

@ApplicationScoped
public class FaturamentoService {
    @Inject FaturamentoRepository faturamentos;
    @Inject EmpresaService empresas;

    @Transactional
    public List<FaturamentoResponse> listar(String cnpj) {
        empresas.obterPorCnpj(cnpj);
        return faturamentos.findByCnpj(Documentos.digitos(cnpj)).stream()
                .map(FaturamentoResponse::from).toList();
    }

    @Transactional
    public FaturamentoResponse buscar(String cnpj, int ano) {
        if (ano < 1900 || ano > 9999) {
            throw ApiException.badRequest("Ano deve estar entre 1900 e 9999");
        }
        empresas.obterPorCnpj(cnpj);
        Faturamento faturamento = faturamentos.findByCnpjAndAno(Documentos.digitos(cnpj), ano);
        if (faturamento == null) {
            throw ApiException.notFound("Faturamento não encontrado para o ano " + ano);
        }
        return FaturamentoResponse.from(faturamento);
    }

    @Transactional
    public FaturamentoResponse criar(String cnpj, FaturamentoRequest request) {
        Empresa empresa = empresas.obterPorCnpj(cnpj);
        if (faturamentos.findByCnpjAndAno(empresa.cnpj, request.ano()) != null) {
            throw ApiException.conflict("Faturamento já cadastrado para a empresa e ano");
        }
        Faturamento faturamento = new Faturamento();
        faturamento.empresa = empresa;
        faturamento.ano = request.ano();
        faturamento.valor = request.valor();
        faturamentos.persist(faturamento);
        return FaturamentoResponse.from(faturamento);
    }

    @Transactional
    public FaturamentoResponse atualizar(String cnpj, Long id, FaturamentoRequest request) {
        Empresa empresa = empresas.obterPorCnpj(cnpj);
        Faturamento faturamento = obter(id, empresa);
        Faturamento duplicado = faturamentos.findByCnpjAndAno(empresa.cnpj, request.ano());
        if (duplicado != null && !duplicado.id.equals(id)) {
            throw ApiException.conflict("Faturamento já cadastrado para a empresa e ano");
        }
        faturamento.ano = request.ano();
        faturamento.valor = request.valor();
        return FaturamentoResponse.from(faturamento);
    }

    @Transactional
    public void excluir(String cnpj, Long id) {
        Empresa empresa = empresas.obterPorCnpj(cnpj);
        faturamentos.delete(obter(id, empresa));
    }

    private Faturamento obter(Long id, Empresa empresa) {
        Faturamento faturamento = faturamentos.findByIdOptional(id)
                .orElseThrow(() -> ApiException.notFound("Faturamento não encontrado: " + id));
        if (!faturamento.empresa.id.equals(empresa.id)) {
            throw ApiException.notFound("Faturamento não encontrado para esta empresa");
        }
        return faturamento;
    }
}
