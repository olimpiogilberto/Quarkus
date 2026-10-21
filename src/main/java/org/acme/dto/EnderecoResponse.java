package org.acme.dto;

import org.acme.model.Endereco;

public record EnderecoResponse(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String pais) {
    public static EnderecoResponse from(Endereco e) {
        return e == null ? null : new EnderecoResponse(e.id, e.logradouro, e.numero, e.complemento,
                e.bairro, e.cidade, e.estado, e.cep, e.pais);
    }
}
