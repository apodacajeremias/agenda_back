package py.jere.agendate.model.services.interfaces;

import java.util.List;
import java.util.UUID;

import py.jere.agendate.model.entities.Movimiento;
import py.jere.agendate.model.services.ServiceCustom;

public interface IMovimientoService extends ServiceCustom<Movimiento> {
	List<Movimiento> buscarPorPersona(UUID idPersona);
}
