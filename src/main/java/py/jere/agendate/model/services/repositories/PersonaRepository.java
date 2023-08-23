package py.jere.agendate.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID>, RepositoryCustom<Persona> {
}
