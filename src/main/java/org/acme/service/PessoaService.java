package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.acme.dto.EnderecoRequest;
import org.acme.dto.PessoaRequest;
import org.acme.dto.PessoaResponse;
import org.acme.exception.ApiException;
import org.acme.model.Pessoa;
import org.acme.repository.PessoaRepository;
import org.acme.repository.VinculoRepository;
import org.acme.validation.Documentos;

@ApplicationScoped
public class PessoaService {
    @Inject PessoaRepository pessoas;
    @Inject VinculoRepository vinculos;

    @Transactional
    public List<PessoaResponse> listar() {
        return pessoas.listAll().stream().map(PessoaResponse::from).toList();
    }

    @Transactional
    public PessoaResponse buscar(Long id) {
        return PessoaResponse.from(obter(id));
    }

    @Transactional
    public PessoaResponse criar(PessoaRequest request) {
        String cpf = Documentos.digitos(request.cpf());
        if (!Documentos.cpfValido(cpf)) {
            throw ApiException.badRequest("CPF inválido");
        }
        if (pessoas.findByCpf(cpf) != null) {
            throw ApiException.conflict("CPF já cadastrado");
        }
        Pessoa pessoa = new Pessoa();
        aplicar(pessoa, request, cpf);
        pessoas.persist(pessoa);
        return PessoaResponse.from(pessoa);
    }

    @Transactional
    public PessoaResponse atualizar(Long id, PessoaRequest request) {
        Pessoa pessoa = obter(id);
        String cpf = Documentos.digitos(request.cpf());
        if (!Documentos.cpfValido(cpf)) {
            throw ApiException.badRequest("CPF inválido");
        }
        Pessoa existente = pessoas.findByCpf(cpf);
        if (existente != null && !existente.id.equals(id)) {
            throw ApiException.conflict("CPF já cadastrado");
        }
        aplicar(pessoa, request, cpf);
        return PessoaResponse.from(pessoa);
    }

    @Transactional
    public PessoaResponse atualizarEndereco(Long id, EnderecoRequest request) {
        Pessoa pessoa = obter(id);
        pessoa.endereco = EnderecoService.from(request);
        return PessoaResponse.from(pessoa);
    }

    @Transactional
    public PessoaResponse removerEndereco(Long id) {
        Pessoa pessoa = obter(id);
        pessoa.endereco = null;
        return PessoaResponse.from(pessoa);
    }

    @Transactional
    public void excluir(Long id) {
        Pessoa pessoa = obter(id);
        if (!vinculos.find("pessoa", pessoa).list().isEmpty()) {
            throw ApiException.conflict("Pessoa possui vínculos associados");
        }
        pessoas.delete(pessoa);
    }

    public Pessoa obterPorCpf(String valor) {
        String cpf = Documentos.digitos(valor);
        if (!Documentos.cpfValido(cpf)) {
            throw ApiException.badRequest("CPF inválido");
        }
        Pessoa pessoa = pessoas.findByCpf(cpf);
        if (pessoa == null) {
            throw ApiException.notFound("Pessoa não encontrada");
        }
        return pessoa;
    }

    public Pessoa obter(Long id) {
        return pessoas.findByIdOptional(id)
                .orElseThrow(() -> ApiException.notFound("Pessoa não encontrada: " + id));
    }

    private void aplicar(Pessoa pessoa, PessoaRequest request, String cpf) {
        pessoa.cpf = cpf;
        pessoa.nome = request.nome().trim();
        pessoa.dataNascimento = request.dataNascimento();
        pessoa.situacao = request.situacao();
        if (request.endereco() != null) {
            if (pessoa.endereco == null) {
                pessoa.endereco = EnderecoService.from(request.endereco());
            } else {
                EnderecoService.update(pessoa.endereco, request.endereco());
            }
        }
    }
}
