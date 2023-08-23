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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.jere.agendate.controller.utils.Beans;
import py.jere.agendate.model.entities.Colaborador;
import py.jere.agendate.model.services.interfaces.IColaboradorService;

@RestController
@RequestMapping("/colaboradores")
@CrossOrigin
public class ColaboradorController {

	@Autowired
	IColaboradorService service;

	@PostMapping
	private ResponseEntity<?> guardar(@RequestBody Colaborador guardar) {
		System.out.println("Guardando Colaborador: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
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

	@PutMapping("/{ID}")
	private ResponseEntity<?> actualizar(@PathVariable UUID ID, @RequestBody Colaborador actualizar) {
		System.out.println("Actualizando Colaborador: " + ID + " -> " + actualizar);
		Colaborador existente = service.buscar(ID);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando Colaborador: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<?> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando Colaborador: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
}
