package py.jere.agendate.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.Grupo;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, UUID>, RepositoryCustom<Grupo> {

}
