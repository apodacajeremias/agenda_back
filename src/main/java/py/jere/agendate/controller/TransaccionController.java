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
import py.jere.agendate.model.entities.Transaccion;
import py.jere.agendate.model.entities.TransaccionDetalle;
import py.jere.agendate.model.services.interfaces.ITransaccionDetalleService;
import py.jere.agendate.model.services.interfaces.ITransaccionService;

@RestController
@RequestMapping("/transacciones")
@CrossOrigin
public class TransaccionController {

	@Autowired
	ITransaccionService service;

	@Autowired
	ITransaccionDetalleService serviceDetalle;

	@PostMapping
	private ResponseEntity<?> guardar(Transaccion guardar) throws Exception {
		guardar.setNombre("");
		guardar.setActivo(true);
		System.out.println("Guardando Transaccion: " + guardar.toString());
		return ResponseEntity.ok(service.registrar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Transaccion: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Transaccion: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Transaccion: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Transaccion: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, Transaccion actualizar) throws Exception {
		System.out.println("Actualizando Transaccion: " + id + " -> " + actualizar);
		Transaccion existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando Transaccion: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando Transaccion: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}

	/////////////////

	@PostMapping("/{idTransaccion}/detalles")
	private ResponseEntity<?> guardarDetalle(@PathVariable UUID idTransaccion, TransaccionDetalle guardar)
			throws Exception {
		guardar.setTransaccion(Transaccion.builder().id(idTransaccion).build());
		System.out.println("Guardando Transaccion Detalle: " + guardar.toString());
		return ResponseEntity.ok(serviceDetalle.registrar(guardar));
	}

	@GetMapping("/detalles")
	private ResponseEntity<List<?>> buscarDetallesPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Transaccion: Todos");
			return ResponseEntity.ok(serviceDetalle.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Transaccion: Solo activos");
			return ResponseEntity.ok(serviceDetalle.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Transaccion: Solo inactivos");
			return ResponseEntity.ok(serviceDetalle.buscarInactivos());
		} else {
			System.out.println("Buscando Transaccion: Todos");
			return ResponseEntity.ok(serviceDetalle.buscarTodos());
		}
	}

	@PutMapping("/{idTransaccion}/detalles/{idDetalle}")
	private ResponseEntity<?> actualizar(@PathVariable UUID idTransaccion, @PathVariable UUID idDetalle,
			TransaccionDetalle actualizar) throws Exception {
		System.out.println("Actualizando Transaccion Detalle ->" + idDetalle);
		System.out.println("Actualizando -> " + actualizar);
		TransaccionDetalle existente = serviceDetalle.buscar(idTransaccion, idDetalle);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(serviceDetalle.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{idTransaccion}/detalles/{idDetalle}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID idTransaccion, @PathVariable UUID idDetalle) {
		System.out.println("Eliminando Transaccion: " + idTransaccion);
		return ResponseEntity.ok(serviceDetalle.eliminar(idTransaccion, idDetalle));
	}

	@GetMapping("/{idTransaccion}/detalles/{idDetalle}")
	private ResponseEntity<?> buscar(@PathVariable UUID idTransaccion, @PathVariable UUID idDetalle) {
		System.out.println("Buscando Transaccion Detalle: " + idDetalle);
		return ResponseEntity.ok(serviceDetalle.buscar(idTransaccion, idDetalle));
	}
}
