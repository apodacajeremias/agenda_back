package py.jere.agendate.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.Empresa;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID>, RepositoryCustom<Empresa> {

}
