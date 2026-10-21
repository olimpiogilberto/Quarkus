package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.acme.model.Faturamento;

@ApplicationScoped
public class FaturamentoRepository implements PanacheRepository<Faturamento> {
    public List<Faturamento> findByCnpj(String cnpj) {
        return list("empresa.cnpj = ?1 order by ano", cnpj);
    }

    public Faturamento findByCnpjAndAno(String cnpj, int ano) {
        return find("empresa.cnpj = ?1 and ano = ?2", cnpj, ano).firstResult();
    }
}
