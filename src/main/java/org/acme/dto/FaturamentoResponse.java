package org.acme.dto;

import java.math.BigDecimal;
import org.acme.model.Faturamento;

public record FaturamentoResponse(Long id, Long empresaId, Integer ano, BigDecimal valor) {
    public static FaturamentoResponse from(Faturamento f) {
        return new FaturamentoResponse(f.id, f.empresa.id, f.ano, f.valor);
    }
}
