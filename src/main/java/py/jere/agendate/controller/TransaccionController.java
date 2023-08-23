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
	private ResponseEntity<?> guardar(@RequestBody Transaccion guardar) {
		System.out.println("Guardando Transaccion: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
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

	@PutMapping("/{ID}")
	private ResponseEntity<?> actualizar(@PathVariable UUID ID, @RequestBody Transaccion actualizar) {
		System.out.println("Actualizando Transaccion: " + ID + " -> " + actualizar);
		Transaccion existente = service.buscar(ID);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando Transaccion: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<?> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando Transaccion: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}

	/////////////////

	@PostMapping("/{IDTransaccion}/detalles")
	private ResponseEntity<?> guardarDetalle(@PathVariable UUID IDTransaccion,
			@RequestBody TransaccionDetalle guardar) {
		guardar.setTransaccion(Transaccion.builder().ID(IDTransaccion).build());
		System.out.println("Guardando Transaccion Detalle: " + guardar.toString());
		return ResponseEntity.ok(serviceDetalle.guardar(guardar));
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

	@PutMapping("/{IDTransaccion}/detalles/{IDDetalle}")
	private ResponseEntity<?> actualizar(@PathVariable UUID IDTransaccion, @PathVariable UUID IDDetalle,
			@RequestBody TransaccionDetalle actualizar) {
		System.out.println("Actualizando Transaccion Detalle ->" + IDDetalle);
		System.out.println("Actualizando -> " + actualizar);
		TransaccionDetalle existente = serviceDetalle.buscar(IDTransaccion, IDDetalle);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(serviceDetalle.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{IDTransaccion}/detalles/{IDDetalle}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID IDTransaccion, @PathVariable UUID IDDetalle) {
		System.out.println("Eliminando Transaccion: " + IDTransaccion);
		return ResponseEntity.ok(serviceDetalle.eliminar(IDTransaccion, IDDetalle));
	}

	@GetMapping("/{IDTransaccion}/detalles/{IDDetalle}")
	private ResponseEntity<?> buscar(@PathVariable UUID IDTransaccion, @PathVariable UUID IDDetalle) {
		System.out.println("Buscando Transaccion Detalle: " + IDDetalle);
		return ResponseEntity.ok(serviceDetalle.buscar(IDTransaccion, IDDetalle));
	}
}
