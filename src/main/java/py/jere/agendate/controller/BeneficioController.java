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
import py.jere.agendate.model.entities.Beneficio;
import py.jere.agendate.model.services.interfaces.IBeneficioService;

@RestController
@RequestMapping("/beneficios")
@CrossOrigin
public class BeneficioController {

	@Autowired
	IBeneficioService service;

	@PostMapping
	private ResponseEntity<?> guardar(Beneficio guardar) {
		System.out.println("Guardando Beneficio: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(
			@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Beneficio: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Beneficio: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Beneficio: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Beneficio: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, Beneficio actualizar) {
		System.out.println("Actualizando Beneficio: " + id + " -> " + actualizar);
		Beneficio existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando Beneficio: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando Beneficio: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}
}
