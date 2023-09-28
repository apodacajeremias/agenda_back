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
import py.jere.agendate.model.entities.Movimiento;
import py.jere.agendate.model.entities.MovimientoDetalle;
import py.jere.agendate.model.services.interfaces.IMovimientoDetalleService;
import py.jere.agendate.model.services.interfaces.IMovimientoService;

@RestController
@RequestMapping("/movimientos")
@CrossOrigin
public class MovimientoController {

	@Autowired
	IMovimientoService service;

	@Autowired
	IMovimientoDetalleService serviceDetalle;

	@PostMapping
	private ResponseEntity<?> guardar(Movimiento guardar) throws Exception {
		guardar.setNombre("");
		guardar.setActivo(true);
		System.out.println("Guardando Movimiento: " + guardar.toString());
		return ResponseEntity.ok(service.registrar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Movimiento: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Movimiento: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Movimiento: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Movimiento: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, Movimiento actualizar) throws Exception {
		System.out.println("Actualizando Movimiento: " + id + " -> " + actualizar);
		Movimiento existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando Movimiento: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando Movimiento: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}

	/////////////////

	@PostMapping("/{idMovimiento}/detalles")
	private ResponseEntity<?> guardarDetalle(@PathVariable UUID idMovimiento, MovimientoDetalle guardar)
			throws Exception {
		guardar.setMovimiento(Movimiento.builder().id(idMovimiento).build());
		System.out.println("Guardando Movimiento Detalle: " + guardar.toString());
		return ResponseEntity.ok(serviceDetalle.registrar(guardar));
	}

	@GetMapping("/detalles")
	private ResponseEntity<List<?>> buscarDetallesPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Movimiento: Todos");
			return ResponseEntity.ok(serviceDetalle.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Movimiento: Solo activos");
			return ResponseEntity.ok(serviceDetalle.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Movimiento: Solo inactivos");
			return ResponseEntity.ok(serviceDetalle.buscarInactivos());
		} else {
			System.out.println("Buscando Movimiento: Todos");
			return ResponseEntity.ok(serviceDetalle.buscarTodos());
		}
	}

	@PutMapping("/{idMovimiento}/detalles/{idDetalle}")
	private ResponseEntity<?> actualizar(@PathVariable UUID idMovimiento, @PathVariable UUID idDetalle,
			MovimientoDetalle actualizar) throws Exception {
		System.out.println("Actualizando Movimiento Detalle ->" + idDetalle);
		System.out.println("Actualizando -> " + actualizar);
		MovimientoDetalle existente = serviceDetalle.buscar(idMovimiento, idDetalle);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(serviceDetalle.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{idMovimiento}/detalles/{idDetalle}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID idMovimiento, @PathVariable UUID idDetalle) {
		System.out.println("Eliminando Movimiento: " + idMovimiento);
		return ResponseEntity.ok(serviceDetalle.eliminar(idMovimiento, idDetalle));
	}

	@GetMapping("/{idMovimiento}/detalles/{idDetalle}")
	private ResponseEntity<?> buscar(@PathVariable UUID idMovimiento, @PathVariable UUID idDetalle) {
		System.out.println("Buscando Movimiento Detalle: " + idDetalle);
		return ResponseEntity.ok(serviceDetalle.buscar(idMovimiento, idDetalle));
	}
}
