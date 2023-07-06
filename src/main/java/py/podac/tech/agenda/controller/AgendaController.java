package py.podac.tech.agenda.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.podac.tech.agenda.controller.utils.Beans;
import py.podac.tech.agenda.model.entities.Agenda;
import py.podac.tech.agenda.model.services.interfaces.IAgendaService;

@RestController
@RequestMapping("/agendas")
@CrossOrigin
public class AgendaController {

	@Autowired
	IAgendaService service;

	@PostMapping
	private ResponseEntity<?> guardar(@RequestBody Agenda guardar) {
		System.out.println("Guardando Agenda: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(
			@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Agenda: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Agenda: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Agenda: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Agenda: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{ID}")
	private ResponseEntity<?> actualizar(@PathVariable UUID ID, @RequestBody Agenda actualizar) {
		System.out.println("Actualizando Agenda: " + ID + " -> " + actualizar);
		Agenda existente = service.buscar(ID);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando Agenda: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<?> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando Agenda: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
	
	@GetMapping
	private ResponseEntity<?> verificarDisponibilidadDeHorario(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin){
		System.out.println("Verificando disponibilidad de horario entre "+inicio+" y "+fin);
		return ResponseEntity.ok(service.horarioDisponible(inicio, fin));
	}
}
