package org.acme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.acme.model.Cargo;
import org.acme.model.StatusVinculo;

public record VinculoRequest(
        @NotNull @Positive Long pessoaId,
        @NotNull @Positive Long empresaId,
        @NotNull Cargo cargo,
        @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal salario,
        @PastOrPresent LocalDate dataInicio,
        @PastOrPresent LocalDate dataFim,
        @NotNull StatusVinculo status) {
}
