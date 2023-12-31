package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Colaborador;
import py.jere.agendate.model.exceptions.DuplicatedFieldException;
import py.jere.agendate.model.services.interfaces.IColaboradorService;
import py.jere.agendate.model.services.repositories.ColaboradorRepository;

@Service
@Primary
public class ColaboradorServiceJPA implements IColaboradorService {

	@Autowired
	private ColaboradorRepository repo;

	@Override
	public Colaborador registrar(Colaborador registrar) throws Exception {
		if (registrar.getRegistroContribuyente() != null
				&& existeRegistroContribuyente(registrar.getRegistroContribuyente()))
			throw new DuplicatedFieldException("El registro de contribuyente ya pertenece a otro colaborador -> "
					+ registrar.getRegistroContribuyente());

		if (registrar.getRegistroProfesional() != null && existeRegistroProfesional(registrar.getRegistroProfesional()))
			throw new DuplicatedFieldException("El registro de profesional ya pertenece a otro colaborador -> "
					+ registrar.getRegistroProfesional());
		return this.repo.save(registrar);
	}

	@Override
	public List<Colaborador> guardarTodos(List<Colaborador> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID id) {
		try {
			this.repo.deleteById(id);
			if (existe(id))
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
	}

	@Override
	public boolean existeRegistroContribuyente(String registroContribuyente) {
		return this.repo.existsByRegistroContribuyente(registroContribuyente);
	}

	@Override
	public boolean existeRegistroProfesional(String registroProfesional) {
		return this.repo.existsByRegistroProfesional(registroProfesional);
	}

	@Override
	public List<Colaborador> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Colaborador> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Colaborador> buscarTodos() {
		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
	}

	@Override
	public Colaborador buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Colaborador buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}

}
