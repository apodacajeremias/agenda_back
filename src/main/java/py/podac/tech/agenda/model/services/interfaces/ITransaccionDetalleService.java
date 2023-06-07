package py.podac.tech.agenda.model.services.interfaces;

import java.util.UUID;

import py.podac.tech.agenda.model.entities.TransaccionDetalle;
import py.podac.tech.agenda.model.services.ServiceCustom;

public interface ITransaccionDetalleService extends ServiceCustom<TransaccionDetalle> {
	TransaccionDetalle buscar(UUID IDTransaccion, UUID IDDetalle);
	boolean eliminar(UUID IDTransaccion, UUID IDDetalle);
	boolean existe(UUID IDTransaccion, UUID IDDetalle);

}
