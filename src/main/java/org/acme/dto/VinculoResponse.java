package org.acme.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.acme.model.Cargo;
import org.acme.model.StatusVinculo;
import org.acme.model.Vinculo;

public record VinculoResponse(Long id, Long pessoaId, Long empresaId, Cargo cargo, BigDecimal salario,
                              LocalDate dataInicio, LocalDate dataFim, StatusVinculo status) {
    public static VinculoResponse from(Vinculo v) {
        return new VinculoResponse(v.id, v.pessoa.id, v.empresa.id, v.cargo, v.salario,
                v.dataInicio, v.dataFim, v.status);
    }
}
