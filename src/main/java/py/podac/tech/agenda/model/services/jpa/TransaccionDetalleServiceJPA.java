package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.TransaccionDetalle;
import py.podac.tech.agenda.model.services.interfaces.ITransaccionDetalleService;
import py.podac.tech.agenda.model.services.repositories.TransaccionDetalleRepository;

@Service
@Primary
public class TransaccionDetalleServiceJPA implements ITransaccionDetalleService {

	@Autowired
	private TransaccionDetalleRepository repo;

	@Override
	public TransaccionDetalle registrar(TransaccionDetalle registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public TransaccionDetalle guardar(TransaccionDetalle guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<TransaccionDetalle> guardarTodos(List<TransaccionDetalle> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID ID) {
		try {
			this.repo.deleteById(ID);
			return !this.existe(ID);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean eliminar(UUID IDTransaccion, UUID IDDetalle) {
		try {
			this.repo.deleteByTransaccionIDAndID(IDTransaccion, IDDetalle);
			return !this.existe(IDTransaccion, IDDetalle);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean existe(UUID ID) {
		return this.repo.existsById(ID);
	}
	
	@Override
	public boolean existe(UUID IDTransaccion, UUID IDDetalle) {
		return this.repo.existsByTransaccionIDAndID(IDTransaccion, IDDetalle);
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
	public TransaccionDetalle buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public TransaccionDetalle buscar(UUID IDTransaccion, UUID IDDetalle) {
		return this.repo.findByTransaccionIDAndID(IDTransaccion, IDDetalle).orElseThrow();
	}

	@Override
	public TransaccionDetalle buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
