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

	List<T> guardarTodos(List<T> guardarTodos);

	boolean eliminar(UUID id);

	boolean existe(UUID id);

	List<T> buscarActivos();

	List<T> buscarInactivos();

	List<T> buscarTodos();

	T buscar(UUID id);

	T buscarUltimo();
}
