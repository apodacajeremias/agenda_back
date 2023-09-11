package py.jere.agendate.model.services.interfaces;

import java.util.List;
import java.util.UUID;

import py.jere.agendate.model.entities.Transaccion;
import py.jere.agendate.model.services.ServiceCustom;

public interface ITransaccionService extends ServiceCustom<Transaccion> {
	List<Transaccion> buscarPorPersona(UUID idPersona);
}
