package org.acme.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.acme.model.SituacaoEmpresa;

public record EmpresaRequest(
        @NotBlank @Size(max = 18) String cnpj,
        @NotBlank @Size(max = 150) String razaoSocial,
        @NotNull SituacaoEmpresa situacao,
        @Valid EnderecoRequest endereco) {
}
