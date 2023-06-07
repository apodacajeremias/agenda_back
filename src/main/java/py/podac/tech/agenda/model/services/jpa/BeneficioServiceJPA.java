package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.Beneficio;
import py.podac.tech.agenda.model.services.interfaces.IBeneficioService;
import py.podac.tech.agenda.model.services.repositories.BeneficioRepository;

@Service
@Primary
public class BeneficioServiceJPA implements IBeneficioService {

	@Autowired
	private BeneficioRepository repo;

	@Override
	public Beneficio registrar(Beneficio registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public Beneficio guardar(Beneficio guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Beneficio> guardarTodos(List<Beneficio> guardarTodos) {
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
	public boolean existe(UUID ID) {
		return this.repo.existsById(ID);
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
	public Beneficio buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Beneficio buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
