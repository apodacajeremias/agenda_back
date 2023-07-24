package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.Empresa;
import py.podac.tech.agenda.model.services.interfaces.IEmpresaService;
import py.podac.tech.agenda.model.services.repositories.EmpresaRepository;

@Service
@Primary
public class EmpresaServiceJPA implements IEmpresaService {

	@Autowired
	private EmpresaRepository repo;

	@Override
	public Empresa registrar(Empresa registrar) throws Exception {
		return null;
	}

	@Override
	public Empresa guardar(Empresa guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Empresa> guardarTodos(List<Empresa> guardarTodos) {
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
	public List<Empresa> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Empresa> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Empresa> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Empresa buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Empresa buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
