package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.acme.model.Pessoa;
import org.acme.model.StatusVinculo;
import org.acme.model.Vinculo;

@ApplicationScoped
public class VinculoRepository implements PanacheRepository<Vinculo> {
    public List<Vinculo> findAtivosByPessoa(Pessoa pessoa) {
        return list("pessoa = ?1 and status = ?2", pessoa, StatusVinculo.ATIVO);
    }

    public List<Vinculo> findByCnpj(String cnpj) {
        return list("empresa.cnpj = ?1", cnpj);
    }
}
