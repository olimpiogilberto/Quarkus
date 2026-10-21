package org.acme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 200)
    public String logradouro;

    @Column(length = 20)
    public String numero;

    @Column(length = 100)
    public String complemento;

    @Column(length = 100)
    public String bairro;

    @Column(nullable = false, length = 100)
    public String cidade;

    @Column(nullable = false, length = 2)
    public String estado;

    @Column(length = 9)
    public String cep;

    @Column(nullable = false, length = 100)
    public String pais = "Brasil";
}
