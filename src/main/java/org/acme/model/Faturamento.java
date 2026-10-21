package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(name = "faturamento_anual", uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_id", "ano"}))
public class Faturamento extends PanacheEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    public Empresa empresa;

    @Column(nullable = false)
    public Integer ano;

    @Column(nullable = false, precision = 15, scale = 2)
    public BigDecimal valor;
}
