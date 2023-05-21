package py.podac.tech.agenda.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.RepositoryCustom;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID>, RepositoryCustom<Persona> {
	Optional<Persona> findByUserEmail(String email);
}
