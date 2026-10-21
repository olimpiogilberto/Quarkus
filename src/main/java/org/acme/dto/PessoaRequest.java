package org.acme.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.acme.model.SituacaoPessoa;

public record PessoaRequest(
        @NotBlank @Size(max = 14) String cpf,
        @NotBlank @Size(max = 150) String nome,
        @Past LocalDate dataNascimento,
        @NotNull SituacaoPessoa situacao,
        @Valid EnderecoRequest endereco) {
}
