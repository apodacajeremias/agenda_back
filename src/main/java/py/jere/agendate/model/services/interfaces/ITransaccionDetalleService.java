package py.jere.agendate.model.services.interfaces;

import java.util.UUID;

import py.jere.agendate.model.entities.TransaccionDetalle;
import py.jere.agendate.model.services.ServiceCustom;

public interface ITransaccionDetalleService extends ServiceCustom<TransaccionDetalle> {
	TransaccionDetalle buscar(UUID IDTransaccion, UUID IDDetalle);
	boolean eliminar(UUID IDTransaccion, UUID IDDetalle);
	boolean existe(UUID IDTransaccion, UUID IDDetalle);

}
