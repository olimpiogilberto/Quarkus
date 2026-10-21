package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.acme.dto.EmpresaRequest;
import org.acme.dto.EnderecoRequest;
import org.acme.dto.EmpresaResponse;
import org.acme.exception.ApiException;
import org.acme.model.Empresa;
import org.acme.repository.EmpresaRepository;
import org.acme.repository.FaturamentoRepository;
import org.acme.repository.VinculoRepository;
import org.acme.validation.Documentos;

@ApplicationScoped
public class EmpresaService {
    @Inject EmpresaRepository empresas;
    @Inject FaturamentoRepository faturamentos;
    @Inject VinculoRepository vinculos;

    @Transactional
    public List<EmpresaResponse> listar() {
        return empresas.listAll().stream().map(EmpresaResponse::from).toList();
    }

    @Transactional
    public EmpresaResponse buscar(Long id) {
        return EmpresaResponse.from(obter(id));
    }

    @Transactional
    public EmpresaResponse criar(EmpresaRequest request) {
        String cnpj = Documentos.digitos(request.cnpj());
        if (!Documentos.cnpjValido(cnpj)) {
            throw ApiException.badRequest("CNPJ inválido");
        }
        if (empresas.findByCnpj(cnpj) != null) {
            throw ApiException.conflict("CNPJ já cadastrado");
        }
        Empresa empresa = new Empresa();
        aplicar(empresa, request, cnpj);
        empresas.persist(empresa);
        return EmpresaResponse.from(empresa);
    }

    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaRequest request) {
        Empresa empresa = obter(id);
        String cnpj = Documentos.digitos(request.cnpj());
        if (!Documentos.cnpjValido(cnpj)) {
            throw ApiException.badRequest("CNPJ inválido");
        }
        Empresa existente = empresas.findByCnpj(cnpj);
        if (existente != null && !existente.id.equals(id)) {
            throw ApiException.conflict("CNPJ já cadastrado");
        }
        aplicar(empresa, request, cnpj);
        return EmpresaResponse.from(empresa);
    }

    @Transactional
    public EmpresaResponse atualizarEndereco(Long id, EnderecoRequest request) {
        Empresa empresa = obter(id);
        empresa.endereco = EnderecoService.from(request);
        return EmpresaResponse.from(empresa);
    }

    @Transactional
    public EmpresaResponse removerEndereco(Long id) {
        Empresa empresa = obter(id);
        empresa.endereco = null;
        return EmpresaResponse.from(empresa);
    }

    @Transactional
    public void excluir(Long id) {
        Empresa empresa = obter(id);
        if (!vinculos.find("empresa", empresa).list().isEmpty()
                || !faturamentos.find("empresa", empresa).list().isEmpty()) {
            throw ApiException.conflict("Empresa possui vínculos ou faturamentos associados");
        }
        empresas.delete(empresa);
    }

    public Empresa obterPorCnpj(String valor) {
        String cnpj = Documentos.digitos(valor);
        if (!Documentos.cnpjValido(cnpj)) {
            throw ApiException.badRequest("CNPJ inválido");
        }
        Empresa empresa = empresas.findByCnpj(cnpj);
        if (empresa == null) {
            throw ApiException.notFound("Empresa não encontrada");
        }
        return empresa;
    }

    public Empresa obter(Long id) {
        return empresas.findByIdOptional(id)
                .orElseThrow(() -> ApiException.notFound("Empresa não encontrada: " + id));
    }

    private void aplicar(Empresa empresa, EmpresaRequest request, String cnpj) {
        empresa.cnpj = cnpj;
        empresa.razaoSocial = request.razaoSocial().trim();
        empresa.situacao = request.situacao();
        if (request.endereco() != null) {
            if (empresa.endereco == null) {
                empresa.endereco = EnderecoService.from(request.endereco());
            } else {
                EnderecoService.update(empresa.endereco, request.endereco());
            }
        }
    }
}
