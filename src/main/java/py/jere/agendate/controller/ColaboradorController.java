package py.jere.agendate.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.jere.agendate.controller.utils.Beans;
import py.jere.agendate.model.entities.Colaborador;
import py.jere.agendate.model.services.interfaces.IAgendaService;
import py.jere.agendate.model.services.interfaces.IColaboradorService;

@RestController
@RequestMapping("/colaboradores")
@CrossOrigin
public class ColaboradorController {

	@Autowired
	IColaboradorService service;

	@Autowired
	IAgendaService agendaService;

	@PostMapping
	private ResponseEntity<?> guardar(Colaborador guardar) throws Exception {
		System.out.println("Guardando Colaborador: " + guardar.toString());
		return ResponseEntity.ok(service.registrar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Colaborador: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Colaborador: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Colaborador: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Colaborador: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, Colaborador actualizar) throws Exception {
		System.out.println("Actualizando Colaborador: " + id + " -> " + actualizar);
		Colaborador existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando Colaborador: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando Colaborador: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}

	@GetMapping("/{id}/agendas")
	private ResponseEntity<?> buscarAgendas(@PathVariable UUID id) {
		System.out.println("Buscando agendamientos de Colaborador: " + id);
		return ResponseEntity.ok(agendaService.buscarPorColaborador(id));
	}
}
