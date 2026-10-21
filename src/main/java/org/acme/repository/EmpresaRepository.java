package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Empresa;

@ApplicationScoped
public class EmpresaRepository implements PanacheRepository<Empresa> {
    public Empresa findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }
}
