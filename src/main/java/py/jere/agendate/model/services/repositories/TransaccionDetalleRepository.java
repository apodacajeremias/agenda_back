package py.jere.agendate.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.jere.agendate.model.entities.TransaccionDetalle;
import py.jere.agendate.model.services.RepositoryCustom;

@Repository
public interface TransaccionDetalleRepository
		extends JpaRepository<TransaccionDetalle, UUID>, RepositoryCustom<TransaccionDetalle> {
	Optional<TransaccionDetalle> findByTransaccionIDAndID(UUID IDTransaccion, UUID IDDetalle);
	void deleteByTransaccionIDAndID(UUID IDTransaccion, UUID IDDetalle);
	boolean existsByTransaccionIDAndID(UUID IDTransaccion, UUID IDDetalle);
	
}
