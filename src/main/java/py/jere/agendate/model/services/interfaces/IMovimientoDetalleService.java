package py.jere.agendate.model.services.interfaces;

import java.util.UUID;

import py.jere.agendate.model.entities.MovimientoDetalle;
import py.jere.agendate.model.services.ServiceCustom;

public interface IMovimientoDetalleService extends ServiceCustom<MovimientoDetalle> {
	MovimientoDetalle buscar(UUID IDMovimiento, UUID IDDetalle);
	boolean eliminar(UUID IDMovimiento, UUID IDDetalle);
	boolean existe(UUID IDMovimiento, UUID IDDetalle);

}
