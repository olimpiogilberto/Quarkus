package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa extends PanacheEntity {
    @Column(nullable = false, unique = true, length = 14)
    public String cnpj;

    @Column(nullable = false, length = 150)
    public String razaoSocial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public SituacaoEmpresa situacao;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", unique = true)
    public Endereco endereco;
}
