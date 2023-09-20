package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.TransaccionDetalle;
import py.jere.agendate.model.services.interfaces.ITransaccionDetalleService;
import py.jere.agendate.model.services.repositories.TransaccionDetalleRepository;

@Service
@Primary
public class TransaccionDetalleServiceJPA implements ITransaccionDetalleService {

	@Autowired
	private TransaccionDetalleRepository repo;

	@Override
	public TransaccionDetalle registrar(TransaccionDetalle registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<TransaccionDetalle> guardarTodos(List<TransaccionDetalle> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID id) {
		try {
			this.repo.deleteById(id);
			return !this.existe(id);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean eliminar(UUID idTransaccion, UUID id) {
		try {
			this.repo.deleteByTransaccionIdAndId(idTransaccion, id);
			return !this.existe(idTransaccion, id);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
	}
	
	@Override
	public boolean existe(UUID idTransaccion, UUID id) {
		return this.repo.existsByTransaccionIdAndId(idTransaccion, id);
	}

	@Override
	public List<TransaccionDetalle> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<TransaccionDetalle> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<TransaccionDetalle> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public TransaccionDetalle buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public TransaccionDetalle buscar(UUID idTransaccion, UUID id) {
		return this.repo.findByTransaccionIdAndId(idTransaccion, id).orElseThrow();
	}

	@Override
	public TransaccionDetalle buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
