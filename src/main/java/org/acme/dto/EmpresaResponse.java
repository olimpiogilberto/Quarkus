package org.acme.dto;

import org.acme.model.Empresa;
import org.acme.model.SituacaoEmpresa;

public record EmpresaResponse(Long id, String cnpj, String razaoSocial, SituacaoEmpresa situacao,
                              EnderecoResponse endereco) {
    public static EmpresaResponse from(Empresa e) {
        return new EmpresaResponse(e.id, e.cnpj, e.razaoSocial, e.situacao, EnderecoResponse.from(e.endereco));
    }
}
