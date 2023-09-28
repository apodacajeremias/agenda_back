package py.jere.agendate.model.services.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.Movimiento;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, UUID>, RepositoryCustom<Movimiento> {
	List<Movimiento> findByPersonaId(UUID idPersona);
}
