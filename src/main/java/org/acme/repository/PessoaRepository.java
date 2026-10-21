package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Pessoa;

@ApplicationScoped
public class PessoaRepository implements PanacheRepository<Pessoa> {
    public Pessoa findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}
