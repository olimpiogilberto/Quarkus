package org.acme.dto;

import java.time.LocalDate;
import org.acme.model.Pessoa;
import org.acme.model.SituacaoPessoa;

public record PessoaResponse(Long id, String cpf, String nome, LocalDate dataNascimento,
                             SituacaoPessoa situacao, EnderecoResponse endereco) {
    public static PessoaResponse from(Pessoa p) {
        return new PessoaResponse(p.id, p.cpf, p.nome, p.dataNascimento, p.situacao,
                EnderecoResponse.from(p.endereco));
    }
}
