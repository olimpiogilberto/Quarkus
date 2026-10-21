package org.acme.service;

import org.acme.dto.EnderecoRequest;
import org.acme.model.Endereco;

public final class EnderecoService {
    private EnderecoService() {
    }

    public static Endereco from(EnderecoRequest request) {
        if (request == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        update(endereco, request);
        return endereco;
    }

    public static void update(Endereco endereco, EnderecoRequest request) {
        endereco.logradouro = request.logradouro().trim();
        endereco.numero = trimToNull(request.numero());
        endereco.complemento = trimToNull(request.complemento());
        endereco.bairro = trimToNull(request.bairro());
        endereco.cidade = request.cidade().trim();
        endereco.estado = request.estado().toUpperCase();
        endereco.cep = request.cep() == null ? null : request.cep().replace("-", "");
        endereco.pais = request.pais() == null || request.pais().isBlank() ? "Brasil" : request.pais().trim();
    }

    private static String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
