package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Beneficio;
import py.jere.agendate.model.services.interfaces.IBeneficioService;
import py.jere.agendate.model.services.repositories.BeneficioRepository;

@Service
@Primary
public class BeneficioServiceJPA implements IBeneficioService {

	@Autowired
	private BeneficioRepository repo;

	@Override
	public Beneficio registrar(Beneficio registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<Beneficio> guardarTodos(List<Beneficio> guardarTodos) {
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
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
	}

	@Override
	public List<Beneficio> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Beneficio> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Beneficio> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Beneficio buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Beneficio buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
