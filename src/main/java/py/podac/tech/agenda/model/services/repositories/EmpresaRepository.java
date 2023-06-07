package py.podac.tech.agenda.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.podac.tech.agenda.model.entities.Empresa;
import py.podac.tech.agenda.model.services.RepositoryCustom;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID>, RepositoryCustom<Empresa> {

}
