package py.jere.agendate.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.MovimientoDetalle;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface MovimientoDetalleRepository
		extends JpaRepository<MovimientoDetalle, UUID>, RepositoryCustom<MovimientoDetalle> {
	Optional<MovimientoDetalle> findByMovimientoIdAndId(UUID idMovimiento, UUID id);
	void deleteByMovimientoIdAndId(UUID idMovimiento, UUID id);
	boolean existsByMovimientoIdAndId(UUID vMovimiento, UUID id);
	
}
