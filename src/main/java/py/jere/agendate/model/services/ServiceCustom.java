package py.jere.agendate.model.services;

import java.util.List;
import java.util.UUID;

public interface ServiceCustom<T> {
	/**
	 * Se usa para ejecutar validaciones y arrojar excepciones, antes de guardar el
	 * registro en la base de datos
	 * 
	 * @param registrar
	 * @return
	 * @throws Exception
	 */
	T registrar(T registrar) throws Exception;

	/**
	 * Se usa para ejecutar el guardado del registro en la base de datos, se
	 * recomienda no usar este metodo directamente a pesar de estar disponible. Ya
	 * que preferentemente se debe guardar el registro a traves del metodo
	 * registrar(T registrar) porque el mismo valida y arroja excepciones
	 * 
	 * @param guardar
	 * @return
	 */
	T guardar(T guardar);

	List<T> guardarTodos(List<T> guardarTodos);

	boolean eliminar(UUID ID);

	boolean existe(UUID ID);

	List<T> buscarActivos();

	List<T> buscarInactivos();

	List<T> buscarTodos();

	T buscar(UUID ID);

	T buscarUltimo();
}
