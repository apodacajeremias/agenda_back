package py.jere.agendate.model.services.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.Transaccion;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID>, RepositoryCustom<Transaccion> {
	List<Transaccion> findByPersonaId(UUID idPersona);
}
