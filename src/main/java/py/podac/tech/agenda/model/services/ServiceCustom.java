package py.podac.tech.agenda.model.services;

import java.util.List;
import java.util.UUID;

public interface ServiceCustom<T> {
	T registrar(T registrar) throws Exception;
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
