package org.acme.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

public record FaturamentoRequest(
        @NotNull @Min(1900) @Max(9999) Integer ano,
        @NotNull @PositiveOrZero @Digits(integer = 13, fraction = 2) BigDecimal valor) {
}
